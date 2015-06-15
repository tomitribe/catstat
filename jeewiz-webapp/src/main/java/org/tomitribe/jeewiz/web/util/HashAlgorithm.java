/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tomitribe.jeewiz.web.util;

/**
 * @author Walmartlabs
 * @author Ryan McGuinness [ rmcguinness@walmartlabs ]
 *
 * An enumeration used to describe hashing algorithims, including the name and the length.
 */
public enum HashAlgorithm {
    MD5("MD5", 32), SHA1("SHA-1", 40), SHA256("SHA-256", 64);
    private final String algorithm;
    private final int length;

    HashAlgorithm(String algorithm, int length) {
        this.algorithm = algorithm;
        this.length = length;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public int getLength() {
        return length;
    }
}
