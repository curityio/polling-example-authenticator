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

import com.google.common.collect.ImmutableMap;
import com.google.common.html.HtmlEscapers;
import io.curity.identityserver.plugin.authentication.models.FailedRequestModel;
import io.curity.identityserver.plugin.config.PollingPluginConfig;
import se.curity.identityserver.sdk.authentication.AuthenticationResult;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.authentication.AuthenticatorInformationProvider;
import se.curity.identityserver.sdk.web.Request;
import se.curity.identityserver.sdk.web.Response;

import java.util.Optional;

import static se.curity.identityserver.sdk.http.HttpStatus.OK;
import static se.curity.identityserver.sdk.web.Response.ResponseModelScope.NOT_FAILURE;
import static se.curity.identityserver.sdk.web.ResponseModel.mapResponseModel;
import static se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel;

public class FailedAuthenticationRequestHandler implements AuthenticatorRequestHandler<FailedRequestModel> {

    private final AuthenticatorInformationProvider _authInfoProvider;
    private final ExceptionFactory _exceptionFactory;

    public FailedAuthenticationRequestHandler(PollingPluginConfig config) {
        _authInfoProvider = config.getAuthenticatorInformationProvider();
        _exceptionFactory = config.getExceptionFactory();

    }

    @Override
    public Optional<AuthenticationResult> get(FailedRequestModel failedRequestModel, Response response) {

        FailedRequestModel.Get model = failedRequestModel.getGetRequestModel();

        response.setResponseModel(mapResponseModel(ImmutableMap.of(
                "_errorMessage", HtmlEscapers.htmlEscaper().escape(model.getErrorMessage()),
                "_restartUrl", _authInfoProvider.getAuthenticationBaseUri().getPath()
        )), OK);
        return Optional.empty();
    }

    @Override
    public Optional<AuthenticationResult> post(FailedRequestModel failedRequestModel, Response response) {
        throw _exceptionFactory.methodNotAllowed("HTTP POST not supported");

    }

    @Override
    public FailedRequestModel preProcess(Request request, Response response) {
        response.setResponseModel(templateResponseModel(ImmutableMap.of(),
                "failed/index"), NOT_FAILURE);
        return new FailedRequestModel(request);
    }
}
