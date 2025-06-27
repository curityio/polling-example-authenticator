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

package io.curity.identityserver.plugin.descriptor;

import com.google.common.collect.ImmutableMap;
import io.curity.identityserver.plugin.authentication.handlers.CancelAuthenticationRequestHandler;
import io.curity.identityserver.plugin.authentication.handlers.FailedAuthenticationRequestHandler;
import io.curity.identityserver.plugin.authentication.handlers.StartAuthenticatorRequestHandler;
import io.curity.identityserver.plugin.authentication.handlers.WaitAuthenticationRequestHandler;
import io.curity.identityserver.plugin.config.PollingPluginConfig;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.plugin.descriptor.AuthenticatorPluginDescriptor;

import java.util.Map;

public final class PollingExampleAuthenticatorPluginDescriptor implements AuthenticatorPluginDescriptor<PollingPluginConfig>
{
    @Override
    public String getPluginImplementationType()
    {
        return "polling-example";
    }

    @Override
    public Class<? extends PollingPluginConfig> getConfigurationType()
    {
        return PollingPluginConfig.class;
    }

    @Override
    public Map<String, Class<? extends AuthenticatorRequestHandler<?>>> getAuthenticationRequestHandlerTypes()
    {
        return ImmutableMap.of("index", StartAuthenticatorRequestHandler.class,
                "wait", WaitAuthenticationRequestHandler.class,
                "failed", FailedAuthenticationRequestHandler.class,
                "cancel", CancelAuthenticationRequestHandler.class
        );
    }
}
