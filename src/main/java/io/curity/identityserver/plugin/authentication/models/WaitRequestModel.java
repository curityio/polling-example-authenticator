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
import se.curity.identityserver.sdk.web.Request;

import java.util.Optional;

public final class WaitRequestModel {
    @Valid
    private final Post _postRequestModel;

    public WaitRequestModel(Request request) {
        _postRequestModel = request.isPostRequest() ? new Post(request) : null;
    }

    public Post getPostRequestModel() {
        return Optional.ofNullable(_postRequestModel).orElseThrow(() ->
                new IllegalStateException("Post RequestModel does not exist"));
    }

    public static final class Post {
        private final boolean _isPollingDone;

        public Post(Request request) {
            _isPollingDone = Optional.ofNullable(request.getFormParameterValueOrError("_pollingDone"))
                    .orElse("").equals("true");
        }

        public boolean isPollingDone() {
            return _isPollingDone;
        }
    }
}
