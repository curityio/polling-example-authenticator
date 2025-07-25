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

package io.curity.identityserver.plugin.config;

import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.config.annotation.DefaultInteger;
import se.curity.identityserver.sdk.config.annotation.DefaultService;
import se.curity.identityserver.sdk.config.annotation.Description;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.HttpClient;
import se.curity.identityserver.sdk.service.Json;
import se.curity.identityserver.sdk.service.SessionManager;
import se.curity.identityserver.sdk.service.authentication.AuthenticatorInformationProvider;

public interface PollingPluginConfig extends Configuration
{

    @DefaultInteger(300)
    @Description("Maximum time (in seconds), the authenticator waits for a response")
    int getPollMaxWaitTime();

    @DefaultInteger(2)
    @Description("Initial poll interval (in seconds). The poller includes automatic backoff, increasing the interval by 1 second after every third poll")
    int getInitialPollingInterval();

    @DefaultService
    HttpClient getHttpClient();

    Json getJson();

    SessionManager getSessionManager();

    ExceptionFactory getExceptionFactory();

    AuthenticatorInformationProvider getAuthenticatorInformationProvider();
}
