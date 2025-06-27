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

import io.curity.identityserver.plugin.config.PollingPluginConfig;
import se.curity.identityserver.sdk.authentication.AuthenticationResult;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.authentication.AuthenticatorInformationProvider;
import se.curity.identityserver.sdk.web.Request;
import se.curity.identityserver.sdk.web.Response;

import java.util.Optional;

public class CancelAuthenticationRequestHandler implements AuthenticatorRequestHandler<Request>
{
    private final AuthenticatorInformationProvider _authInfoProvider;
    private final ExceptionFactory _exceptionFactory;

    public CancelAuthenticationRequestHandler(PollingPluginConfig config)
    {
        _authInfoProvider = config.getAuthenticatorInformationProvider();
        _exceptionFactory = config.getExceptionFactory();
    }

    @Override
    public Request preProcess(Request request, Response response)
    {
        return request;
    }

    @Override
    public Optional<AuthenticationResult> get(Request request, Response response)
    {
        throw _exceptionFactory.redirectException(_authInfoProvider.getFullyQualifiedAuthenticationUri() + "/failed?_errorMessage=user_cancelled");
    }

    @Override
    public Optional<AuthenticationResult> post(Request request, Response response)
    {
        throw _exceptionFactory.methodNotAllowed("HTTP POST not supported for this url.");
    }
}
