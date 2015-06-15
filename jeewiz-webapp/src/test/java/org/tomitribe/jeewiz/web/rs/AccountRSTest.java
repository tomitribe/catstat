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
package org.tomitribe.jeewiz.web.rs;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Before;
import org.junit.Test;
import org.tomitribe.jeewiz.web.ArquillianTest;
import org.tomitribe.jeewiz.web.model.Account;
import org.tomitribe.jeewiz.web.services.AccountService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/7/15
 */
public class AccountRSTest extends ArquillianTest {
    private static final String accountUserName = "TestUser";

    @Inject
    private AccountService accountService;

    private Account testAccount;

    @Before
    public void before() {
        Account existing = accountService.findByUserName(accountUserName);
        if (existing != null) {
            testAccount = existing;
        } else {
            testAccount = new Account();
            testAccount.setUserName("TestUser");
            testAccount.setEmail("tester@walmartlabs.com");
            testAccount.setPassword("T3st3r!!");
        }
    }

    @Test
    public void create() {
        if (testAccount!=null && testAccount.getId() == null) {
            Account newAccount = WebClient.create(getRsUrl(), "admin", "admin1234", null).path(AccountRS.PATH_V1).put(testAccount, Account.class);

            assertNotNull(newAccount);
            assertNotNull(newAccount.getId());
            accountService.activate(newAccount.getId());

            Response activationResponse = WebClient.create(getRsUrl(), "admin", "admin1234", null)
                .path(AccountRS.PATH_V1).path("/activation/").path(newAccount.getId()).get();

            //Expect 204 - No Content but status ok
            assertEquals(204, activationResponse.getStatus());

            Account activated = WebClient.create(getRsUrl(), "admin", "admin1234", null)
                .path(AccountRS.PATH_V1)
                .path("/")
                .path(newAccount.getId()).get(Account.class);

            assertTrue(activated.isEnabled());
            assertFalse(activated.isCredentialsExpired());
            assertFalse(activated.isExpired());
            assertFalse(activated.isLocked());
        } else {
            assertTrue(testAccount.isEnabled());
            assertFalse(testAccount.isCredentialsExpired());
            assertFalse(testAccount.isExpired());
            assertFalse(testAccount.isLocked());
        }
    }
}
