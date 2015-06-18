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
package org.tomitribe.jeewiz.web.ejb;

import com.codahale.metrics.MetricRegistry;
import org.tomitribe.jeewiz.metrics.qualifiers.QMetricRegistry;
import org.tomitribe.jeewiz.web.services.AccountService;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/16/15
 */
@Startup
@Singleton
public class TemporalCollector {
    private static final int MIN_PER_DAY = 1440;
    private static final int HOURS_PER_DAY = 24;

    private List<Long> values = new ArrayList<>();

    @Inject
    @QMetricRegistry
    private MetricRegistry metricRegistry;

    @Inject
    private AccountService accountService;

    @PostConstruct
    public void init() {
        syntheticLogin();
    }

    @Schedule(second = "*/45", minute = "*", hour = "*")
    public void syntheticLogin() {
        Random r = new Random();
        int max = r.nextInt(20);
        for (int i = 0; i<max; i++) {
            accountService.findAccountByUsernameAndPassword("admin", "admin1234");
        }
    }

    @Schedule(second = "0", minute = "*/1", hour = "*")
    public void update() {
        long cCount = metricRegistry.getCounters().get("Counted.findAccountByUsernameAndPassword.find-account-counted").getCount();
        values.add(cCount);
        metricRegistry.getCounters().get("Counted.findAccountByUsernameAndPassword.find-account-counted").dec(cCount);
    }

    public List<Long> getValues() {
        return values;
    }
}
