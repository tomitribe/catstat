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

import org.apache.catalina.realm.GenericPrincipal;
import org.ietf.jgss.GSSCredential;

import javax.security.auth.login.LoginContext;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/5/15
 */
public class ExamplePrincipal extends GenericPrincipal implements Serializable {
    public ExamplePrincipal(String name, String password, List<String> roles) {
        super(name, password, roles);
    }

    public ExamplePrincipal(String name, String password, List<String> roles, Principal userPrincipal) {
        super(name, password, roles, userPrincipal);
    }

    public ExamplePrincipal(String name, String password, List<String> roles, Principal userPrincipal, LoginContext loginContext) {
        super(name, password, roles, userPrincipal, loginContext);
    }

    public ExamplePrincipal(String name, String password, List<String> roles, Principal userPrincipal, LoginContext loginContext, GSSCredential gssCredential) {
        super(name, password, roles, userPrincipal, loginContext, gssCredential);
    }
}