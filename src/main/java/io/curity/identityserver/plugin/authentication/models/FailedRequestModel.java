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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import se.curity.identityserver.sdk.web.Request;

import java.util.List;

public final class FailedRequestModel
{
    @Valid
    private final Get _getRequestModel;

    public FailedRequestModel(Request request)
    {
        _getRequestModel = request.isGetRequest() ? new Get(request) : null;
    }

    public Get getGetRequestModel()
    {
        if (_getRequestModel == null)
        {
            throw new IllegalStateException("Get RequestModel is not available for this request type");
        }
        return _getRequestModel;
    }

    public static final class Get
    {
        private final String _errorMessage;

        Get(Request request)
        {
            List<String> messages = (List<String>) request.getQueryParameterValues("_errorMessage");
            if (messages != null && !messages.isEmpty())
            {
                if (messages.size() > 1)
                {
                    throw new IllegalArgumentException("Multiple error messages provided; only one is allowed");
                }
                _errorMessage = messages.getFirst();
            }
            else
            {
                _errorMessage = "unknown";
            }
        }

        @NotEmpty
        public String getErrorMessage()
        {
            return _errorMessage;
        }
    }
}

