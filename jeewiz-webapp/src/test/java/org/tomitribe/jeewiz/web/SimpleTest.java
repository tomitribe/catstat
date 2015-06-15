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
package org.tomitribe.jeewiz.web;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/7/15
 */
public class SimpleTest extends ArquillianTest {
    @BeforeClass
    public static void setSystemVariable() {
        System.setProperty("runOnEnv", "dev");
    }

    @Test
    @InSequence(0)
    public void testArchive() {
        Assert.assertEquals("dev", System.getProperty("runOnEnv"));
    }
}