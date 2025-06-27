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


import io.curity.identityserver.plugin.authentication.models.StartAuthenticatorRequestModel;
import io.curity.identityserver.plugin.config.PollingPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.attribute.Attribute;
import se.curity.identityserver.sdk.authentication.AuthenticationResult;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.http.HttpRequest;
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
import java.util.Map;
import java.util.Optional;

import static se.curity.identityserver.sdk.http.HttpStatus.BAD_REQUEST;
import static se.curity.identityserver.sdk.web.Response.ResponseModelScope.NOT_FAILURE;

public final class StartAuthenticatorRequestHandler implements AuthenticatorRequestHandler<StartAuthenticatorRequestModel> {
    private static final Logger _logger = LoggerFactory.getLogger(StartAuthenticatorRequestHandler.class);
    private static final URI NOTIFICATION_URI = URI.create("http://localhost:3000/send-notification");

    private final AuthenticatorInformationProvider _authInfoProvider;
    private final ExceptionFactory _exceptionFactory;
    private final HttpClient _httpClient;
    private final Json _json;
    private final SessionManager _sessionManager;

    public StartAuthenticatorRequestHandler(PollingPluginConfig config) {
        _exceptionFactory = config.getExceptionFactory();
        _authInfoProvider = config.getAuthenticatorInformationProvider();
        _httpClient = config.getHttpClient();
        _json = config.getJson();
        _sessionManager = config.getSessionManager();
    }

    @Override
    public Optional<AuthenticationResult> get(StartAuthenticatorRequestModel requestModel, Response response) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthenticationResult> post(StartAuthenticatorRequestModel requestModel, Response response) {
        String username = requestModel.getPostRequestModel().getUsername();
        _logger.info("Initiating push authentication for user '{}'", username);

        // Sending a mock push notification, waiting for user action, etc.
        Map<String, String> parameters = Map.of("username", username);

        HttpResponse apiResponse = _httpClient.request(NOTIFICATION_URI)
                .contentType("application/json")
                .body(HttpRequest.fromJson(parameters, _json))
                .post()
                .response();

        String requestId = (String) apiResponse.body(HttpResponse.asJsonObject(_json)).get("requestId");
        _logger.info("Notification sent, requestId: {}", requestId);

        // Store the requestId & username in the session for later retrieval
        _sessionManager.put(Attribute.of("requestId", requestId));
        _sessionManager.put(Attribute.of("username", username));

        // redirecting to wait/polling page
        throw _exceptionFactory.redirectException(_authInfoProvider.getFullyQualifiedAuthenticationUri() + "/wait");
    }

    @Override
    public StartAuthenticatorRequestModel preProcess(Request request, Response response) {
        response.setResponseModel(ResponseModel.templateResponseModel(Collections.emptyMap(), "authenticate/get"), NOT_FAILURE);
        response.setResponseModel(ResponseModel.templateResponseModel(Collections.emptyMap(), "authenticate/get"), BAD_REQUEST);
        return new StartAuthenticatorRequestModel(request);
    }
}
