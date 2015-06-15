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
package org.tomitribe.jeewiz.metrics.commands;

import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/19/15
 */
public interface Command {
    String LOGGER = "__METRIC_LOGGER__";
    String NAMING_STRATEGY = "__Naming_Strategy__";
    String METRIC_REGISTRY = "__Metric_Registry__";
    String METRIC_TIMER = "__Metric_Timer__";
    boolean isExecutable(Method method);
    void execute(InvocationContext context);

    default MetricRegistry getRegistry(InvocationContext context) {
        return (MetricRegistry) context.getContextData().get(METRIC_REGISTRY);
    }

    default NamingStrategy getNamingStrategy(InvocationContext context) {
        return (NamingStrategy) context.getContextData().get(NAMING_STRATEGY);
    }

    default String getName(InvocationContext context, Annotation a) {
        return getNamingStrategy(context).getName(context, a);
    }

    default Logger getLogger(InvocationContext context) {
        return (Logger) context.getContextData().get(LOGGER);
    }
}