/*
 * ******************************************************************************
 *  * Copyright 2016 Anteros Tecnologia
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.linkedin.platform.errors;

import java.util.HashMap;
import java.util.Map;

public enum LIAppErrorCode {
    NONE("none"),
    INVALID_REQUEST("Invalid request"),
    NETWORK_UNAVAILABLE("Unavailable network connection"),
    USER_CANCELLED("User canceled action"),
    UNKNOWN_ERROR("Unknown or not defined error"),
    SERVER_ERROR("Server side error"),
    LINKEDIN_APP_NOT_FOUND("LinkedIn application not found"),
    NOT_AUTHENTICATED("User is not authenticated in LinkedIn app"),
    ;

    private static Map<String, LIAppErrorCode> liAuthErrorCodeHashMap = buildMap();

    private static Map<String, LIAppErrorCode> buildMap() {
        HashMap<String, LIAppErrorCode> map = new HashMap<String, LIAppErrorCode>();
        for (LIAppErrorCode code : LIAppErrorCode.values()) {
            map.put(code.name(), code);
        }
        return map;
    }

    private String description;

    LIAppErrorCode(String name) {
        this.description = name;
    }

    public String getDescription() {
        return description;
    }

    public static LIAppErrorCode findErrorCode(String errorCode) {
        LIAppErrorCode liAuthErrorCode = liAuthErrorCodeHashMap.get(errorCode);
        return liAuthErrorCode == null ? UNKNOWN_ERROR : liAuthErrorCode;
    }
}
