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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/15/15
 */
@RunWith(Arquillian.class)
public abstract class ArquillianTest {
    @Deployment
    public static WebArchive createDeployment() {

        WebArchive war = ShrinkWrap.create(WebArchive.class, "jeewiz.war")
            .addPackages(true, "org.tomitribe.jeewiz.web")
            .addAsResource(new File("src/main/resources/config"))
            .addAsResource(new File("src/main/resources/messages"))
            .addAsResource(new File("src/main/resources/logging.properties"));

        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("src/main/webapp").as(GenericArchive.class),
            "/", Filters.includeAll());

        //Jar the metrics library and include as a jar file
        JavaArchive metricsLib = ShrinkWrap.create(JavaArchive.class, "jeewiz-metrics.jar")
            .addPackages(true, "org.tomitribe.jeewiz.metrics");

        war.addAsLibrary(metricsLib);

        System.out.println(war.toString(true));
        return war;
    }

    @ArquillianResource
    private URL webapp;

    protected String getHostAndPortRaw() {
        return webapp.getHost() + ":" + webapp.getPort();
    }

    //TODO - Change the "/example" URI
    protected String getHostAndPort() {
        return "http://" + getHostAndPortRaw() + "/jeewiz";
    }

    protected String getRsUrl() {
        return getHostAndPort() + "/rs";
    }
}
