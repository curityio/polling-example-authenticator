/*
 *  Copyright 2025 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.identityserver.plugin.authentication.handlers;

import io.curity.identityserver.plugin.authentication.models.WaitRequestModel;
import io.curity.identityserver.plugin.authentication.models.WaitResponseModel;
import io.curity.identityserver.plugin.config.PollingPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.attribute.Attribute;
import se.curity.identityserver.sdk.attribute.Attributes;
import se.curity.identityserver.sdk.attribute.AuthenticationAttributes;
import se.curity.identityserver.sdk.attribute.ContextAttributes;
import se.curity.identityserver.sdk.attribute.SubjectAttributes;
import se.curity.identityserver.sdk.authentication.AuthenticationResult;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.http.HttpResponse;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.HttpClient;
import se.curity.identityserver.sdk.service.Json;
import se.curity.identityserver.sdk.service.SessionManager;
import se.curity.identityserver.sdk.service.authentication.AuthenticatorInformationProvider;
import se.curity.identityserver.sdk.web.Request;
import se.curity.identityserver.sdk.web.Response;
import se.curity.identityserver.sdk.web.ResponseModel;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static se.curity.identityserver.sdk.http.HttpStatus.ACCEPTED;
import static se.curity.identityserver.sdk.http.HttpStatus.OK;
import static se.curity.identityserver.sdk.web.Response.ResponseModelScope.NOT_FAILURE;

public class WaitAuthenticationRequestHandler implements AuthenticatorRequestHandler<WaitRequestModel>
{
    private static final Logger _logger = LoggerFactory.getLogger(WaitAuthenticationRequestHandler.class);
    private static final String NOTIFICATION_URL = "http://localhost:3000/status";
    private static final String STATUS_APPROVED = "approved";
    private static final String STATUS_DECLINED = "declined";

    private final int _pollMaxWaitTimeInSeconds;
    private final int _pollIntervalInSeconds ;
    private final SessionManager _sessionManager;
    private final AuthenticatorInformationProvider _authInfoProvider;
    private final HttpClient _httpClient;
    private final Json _json;
    private final ExceptionFactory _exceptionFactory;

    public WaitAuthenticationRequestHandler(PollingPluginConfig config)
    {
        _sessionManager = config.getSessionManager();
        _authInfoProvider = config.getAuthenticatorInformationProvider();
        _pollMaxWaitTimeInSeconds = config.getPollMaxWaitTime();
        _pollIntervalInSeconds = config.getInitialPollingInterval();
        _httpClient = config.getHttpClient();
        _json = config.getJson();
        _exceptionFactory = config.getExceptionFactory();
    }

    @Override
    public Optional<AuthenticationResult> get(WaitRequestModel waitRequestModel, Response response)
    {
        URI authUri = _authInfoProvider.getFullyQualifiedAuthenticationUri();
        Map<String, Object> model = Map.of(
                "_restartUrl", authUri.getPath(),
                "_failureUrl", authUri + "/failed",
                "_maxWaitTime", _pollMaxWaitTimeInSeconds,
                "_pollInterval", _pollIntervalInSeconds
        );
        response.setResponseModel(new WaitResponseModel(model), OK);
        return Optional.empty();
    }

    @Override
    public Optional<AuthenticationResult> post(WaitRequestModel waitRequestModel, Response response)
    {
        _logger.debug("Processing POST request for WaitAuthenticationRequestHandler");
        String requestId = Optional.ofNullable(_sessionManager.get("requestId"))
                .map(attribute -> attribute.getOptionalValueOfType(String.class))
                .orElse("");
        // Query the status of the request
        HttpResponse statusResponse = _httpClient.request(URI.create(NOTIFICATION_URL + "/" + requestId)).get().response();
        String status = (String) statusResponse.body(HttpResponse.asJsonObject(_json)).get("status");

        _logger.debug("Request status: {}", status);

        if (waitRequestModel.getPostRequestModel().isPollingDone())
        {
            _logger.debug("Polling has already been done, complete the authentication process.");

            if (STATUS_DECLINED.equals(status))
            {
                throw _exceptionFactory.redirectException(
                        _authInfoProvider.getFullyQualifiedAuthenticationUri() + "/failed?_errorMessage=user_declined"
                );
            }

            String username = Optional.ofNullable(_sessionManager.get("username"))
                    .map(attribute -> attribute.getOptionalValueOfType(String.class))
                    .orElse("");

            return Optional.of(new AuthenticationResult(AuthenticationAttributes.of(
                    SubjectAttributes.of(username),
                    ContextAttributes.of(Attributes.of(Attribute.of("iat", new Date().getTime()))))));
        }

        if (STATUS_APPROVED.equals(status) || STATUS_DECLINED.equals(status))
        {
            _logger.debug("Polling ended with status '{}', sending ACCEPTED", status);
            response.setHttpStatus(ACCEPTED);
        }
        else
        {
            _logger.debug("Still waiting for user approval...");
        }

        return Optional.empty();
    }

    @Override
    public WaitRequestModel preProcess(Request request, Response response)
    {
        response.setResponseModel(ResponseModel.templateResponseModel(Collections.emptyMap(),
                "wait/index"), NOT_FAILURE);
        return new WaitRequestModel(request);
    }
}
