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
package org.tomitribe.jeewiz.metrics.listeners;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import org.tomitribe.jeewiz.metrics.qualifiers.JEEWizMetricRegistry;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

/**
 * @author WalmartLabs
 * @author Tomitribe
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/16/15
 */
@WebListener
public class MetricServletContextListener extends MetricsServlet.ContextListener {
    @Inject @JEEWizMetricRegistry
    private MetricRegistry metricRegistry;

    @Override
    protected MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }
}