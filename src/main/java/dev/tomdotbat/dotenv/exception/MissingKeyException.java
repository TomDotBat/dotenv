/*
 *  Copyright 2022 Thomas (Tom.bat) O'Sullivan
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at:
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package dev.tomdotbat.dotenv.exception;

/**
 * An Exception used when the Configuration tries to access a key that isn't present.
 */
public class MissingKeyException extends Exception {
    /**
     * Constructs a Missing Key Exception.
     * @param errorMessage the error message to show in the Exception.
     */
    public MissingKeyException(String errorMessage) {
        super(errorMessage);
    }
}