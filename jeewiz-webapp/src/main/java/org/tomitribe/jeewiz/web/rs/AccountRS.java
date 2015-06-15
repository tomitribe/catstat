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

import org.tomitribe.jeewiz.web.model.Account;
import org.tomitribe.jeewiz.web.services.AccountService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/7/15
 */
@Path(AccountRS.PATH_V1)
public class AccountRS {
    public static final String PATH_V1 = "/accounts/v1";
    public static final String PARAM_ID = "id";

    @Inject
    private AccountService accountService;

    @GET
    @Path("/activation/{id}")
    public void activate(@PathParam(PARAM_ID) String id) {
        accountService.activate(id);

    }

    @GET
    @Path("/{id}")
    public Account findById(@PathParam(PARAM_ID) String id) {
        return accountService.findById(id);
    }

    @POST
    public Account findByCriteria(Account account) {
        return accountService.findAccountByUsernameAndPassword(account.getUserName(), account.getPassword());
    }

    @POST
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    public Account update(Account account) {
        return accountService.saveOrUpdate(account);
    }

    @PUT
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    public Account create(Account account) {
        return accountService.createAccount(account.getUserName(), account.getPassword(), account.getEmail());
    }
}