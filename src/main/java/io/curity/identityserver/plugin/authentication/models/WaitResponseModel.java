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

package io.curity.identityserver.plugin.authentication.models;

import se.curity.identityserver.sdk.web.ResponseModel;

import java.util.Map;

public final class WaitResponseModel implements ResponseModel
{
    private final Map<String, Object> _map;

    public WaitResponseModel(Map<String, Object> map)
    {
        _map = map;
    }

    public String getRestartUrl()
    {

        return _map.get("_restartUrl").toString();
    }

    public String getFailureUrl()
    {
        return _map.get("_failureUrl").toString();
    }

    @Override
    public Map<String, Object> getViewData()
    {
        return _map;
    }
}
