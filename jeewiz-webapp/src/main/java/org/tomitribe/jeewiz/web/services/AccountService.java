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
package org.tomitribe.jeewiz.web.services;


import org.slf4j.Logger;
import org.tomitribe.jeewiz.metrics.annotations.Counted;
import org.tomitribe.jeewiz.metrics.annotations.Metered;
import org.tomitribe.jeewiz.metrics.annotations.Timed;
import org.tomitribe.jeewiz.web.annotations.QLogger;
import org.tomitribe.jeewiz.web.model.Account;
import org.tomitribe.jeewiz.web.util.HashAlgorithm;
import org.tomitribe.jeewiz.web.util.HashUtil;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;


/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/5/15
 */
@Named
@Singleton
@Lock(LockType.WRITE)
public class AccountService {
    @Inject
    @QLogger
    private Logger log;

    @Inject
    private DataService dataService;

    public Account findById(String id) {
        return dataService.find(Account.class, id);
    }

    @Metered(name = "find-by-username")
    public Account findByUserName(String userName) {
        return  dataService.findUniqueByQueryWithParams(Account.class, Account.QRY_FIND_BY_USERNAME, userName);
    }

    @Counted(name ="find-account-counted")
    @Timed(name="find-account")
    public Account findAccountByUsernameAndPassword(String userName, String password) {
        Account out = null;
        if (userName!=null && !userName.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            Account temp = findByUserName(userName);
            if (temp != null &&
                temp.getPassword() != null &&
                (temp.getPassword().equals(password) || temp.getPassword().equals(HashUtil.hashSHA256(password)))) {
                out = temp;
            }
        }
        return out;
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    public Account saveOrUpdate(Account account) {
        if (account!=null) {
            if (account.getPassword()!=null && account.getPassword().trim().length()!= HashAlgorithm.SHA256.getLength()) {
                account.setPassword(HashUtil.hashSHA256(account.getPassword()));
            }
            return dataService.saveOrUpdate(account);
        }
        return account;
    }

    public Account createAccount(String userName, String password, String email) {
        Account a = new Account();
        a.setUserName(userName);
        a.setPassword(password);
        a.setCredentialsExpired(false);
        a.setEnabled(false);
        a.setExpired(false);
        a.setLocked(false);
        a.setEmail(email);
        return saveOrUpdate(a);
    }

    public void delete(Account account) {
        account.setEnabled(false);
        saveOrUpdate(account);
    }

    public void activate(String id) {
        Account a = findById(id);
        if (a != null) {
            a.setEnabled(true);
            saveOrUpdate(a);
        }
    }
}