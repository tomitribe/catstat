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
package org.tomitribe.jeewiz.web.security;

import org.slf4j.Logger;
import org.tomitribe.jeewiz.metrics.annotations.Timed;
import org.tomitribe.jeewiz.web.annotations.QLogger;
import org.tomitribe.jeewiz.web.model.Account;
import org.tomitribe.jeewiz.web.services.AccountService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/5/15
 */
@RequestScoped
public class ExampleRealm {
    @Inject
    @QLogger
    private Logger log;

    @Inject
    private AccountService accountService;

    @Inject
    private ActiveAccount activeAccount;

    @Timed(name = "auth-timer")
    public Principal authenticate(final String username, String password) {
        Account account = accountService.findAccountByUsernameAndPassword(username, password);
        if (account != null) {
            if (account.isEnabled() && !(account.isCredentialsExpired() || account.isExpired() || account.isLocked())) {
                List<String> roleNames = new ArrayList<>();
                account.getRoles().forEach(r -> roleNames.add(r.getRoleName()));
                activeAccount.setAccount(account);
                return new ExamplePrincipal(account.getName(), account.getPassword(), roleNames, account);
            }
        }
        return null;
    }

    public boolean hasRole(Principal principal, String role) {
        if (principal instanceof ExamplePrincipal) {
            return ((ExamplePrincipal) principal).hasRole(role);
        }
        return false;
    }
}