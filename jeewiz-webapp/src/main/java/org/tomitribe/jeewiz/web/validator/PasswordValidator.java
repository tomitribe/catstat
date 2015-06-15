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
package org.tomitribe.jeewiz.web.validator;

import org.tomitribe.jeewiz.web.annotations.Password;
import org.tomitribe.jeewiz.web.util.HashAlgorithm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [ rmcguinness@walmartlabs.com ]
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {
    private static final String STRONG_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!#$%^&+=])(?=\\S+$).{8,}$";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void initialize(Password password) {
        pattern = Pattern.compile(STRONG_PASSWORD_REGEX);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value != null && !value.trim().isEmpty()) {
            if (value.trim().length()== HashAlgorithm.SHA256.getLength()) {
                return true;
            }
            CharSequence pwdStr = value.trim();
            matcher = pattern.matcher(pwdStr);
            return matcher.matches();
        }
        return false;
    }
}