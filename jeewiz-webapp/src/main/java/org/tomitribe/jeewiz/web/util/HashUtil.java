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

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {
    private HashUtil() {

    }
    public static String hash(final String stringToEncrypt, final String encType) {
        String hex = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(encType);
            messageDigest.reset();
            messageDigest.update(stringToEncrypt.getBytes( StandardCharsets.UTF_8));
            hex = new String(Hex.encodeHex(messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            hex = Hex.encodeHexString(stringToEncrypt.getBytes(StandardCharsets.UTF_8));
        }
        return hex;
    }

    public static String hashSHA1(final String string) {
        return hash(string, HashAlgorithm.SHA1.getAlgorithm());
    }

    public static String hashSHA256(final String string) {
        return hash(string, HashAlgorithm.SHA256.getAlgorithm());
    }

    public static String hashMD5(final String string) {
        return hash(string, HashAlgorithm.MD5.getAlgorithm());
    }

    private static void printUsage() {
        System.out.println("Usage: HashUtil <algorithm> [<values-to-encrypt>]");
    }

    public static void main(String... args) {
        if (args != null && args.length > 1) {
            String algorithm = args[0];
            HashAlgorithm hashWith = null;
            for (HashAlgorithm ha : HashAlgorithm.values()) {
                if (ha.getAlgorithm().equalsIgnoreCase(algorithm)) {
                    hashWith = ha;
                    break;
                }
            }
            if (hashWith == null) {
                System.out.println("Invalid Algorithm. " +  algorithm);
            } else {
                for (int i = 1; i < args.length; i++) {
                    System.out.println("Original Value: [" + args[i] + "] Hashed Value: [" + hash(args[i],  hashWith.getAlgorithm()) + "]");
                }
            }
        } else {
            printUsage();
        }
    }
}
