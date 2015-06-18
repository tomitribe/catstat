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
package org.tomitribe.jeewiz.web.cdi;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.tomitribe.jeewiz.web.annotations.QHazelcastInstance;
import org.tomitribe.jeewiz.web.annotations.QLogger;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/18/15
 */
public class HazelcastProducer {
    @Inject
    @QLogger
    private Logger log;

    @Produces
    @Singleton
    @QHazelcastInstance
    public HazelcastInstance createHazelcastInstance() {
        log.info("Creating Hazelcast Instances");
        String environment = System.getProperty("runOnEnv", "local");
        String configFile = "META-INF/" + environment + "/hazelcast.xml";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL location = loader.getResource(configFile);
        Config config = new Config();
        config.setConfigurationUrl(location);
        final HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(config);
        return hazelcast;
    }

    private void closeHazelCast(@Disposes @QHazelcastInstance HazelcastInstance hazelcastInstance) {
        hazelcastInstance.getLifecycleService().shutdown();
        while (hazelcastInstance.getLifecycleService().isRunning()) {
            log.info("waiting for hazelcast to shut down...");
        }
    }


}
