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
package org.tomitribe.jeewiz.metrics;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 12/14/15
 */

public abstract class ArquillianTest {
    @Deployment
    public static WebArchive buildDeployment() {
        final File[] jars = Maven.resolver()
            .resolve(
                "io.dropwizard.metrics:metrics-core:3.1.2",
                "io.dropwizard.metrics:metrics-healthchecks:3.1.2",
                "io.dropwizard.metrics:metrics-json:3.1.2",
                "io.dropwizard.metrics:metrics-servlets:3.1.2")
            .withTransitivity().asFile();

        final WebArchive war = ShrinkWrap.create(WebArchive.class, "metrics-test.war")
            .addPackages(true, "org.tomitribe.jeewiz.test");

        final JavaArchive metricsJar = ShrinkWrap.create(JavaArchive.class, "jeewiz.jar")
            .addPackages(true, "org.tomitribe.jeewiz.metrics")
            .addAsManifestResource(new File("src/main/resources/META-INF"));

        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).as(GenericArchive.class),
            "/", Filters.includeAll());

        war.addAsLibraries(jars);
        war.addAsLibrary(metricsJar);

        System.out.println(war.toString(true));
        return war;
    }
}
