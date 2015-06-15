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
package org.tomitribe.jeewiz.metrics.interceptors;

import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomitribe.jeewiz.metrics.annotations.ExceptionMetered;
import org.tomitribe.jeewiz.metrics.annotations.MetricAware;
import org.tomitribe.jeewiz.metrics.commands.Chain;
import org.tomitribe.jeewiz.metrics.commands.Command;
import org.tomitribe.jeewiz.metrics.commands.NamingStrategy;
import org.tomitribe.jeewiz.metrics.qualifiers.QMetricRegistry;
import org.tomitribe.jeewiz.metrics.qualifiers.QNamingStrategy;
import org.tomitribe.jeewiz.metrics.qualifiers.QPostExecuteChain;
import org.tomitribe.jeewiz.metrics.qualifiers.QPreExecuteChain;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author WalmartLabs
 * @author Tomitribe
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/19/15
 */
@MetricAware
@Interceptor
public class MetricInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(MetricInterceptor.class);
    @Inject
    @QMetricRegistry
    private MetricRegistry metricRegistry;

    @Inject
    @QPreExecuteChain
    private Chain preExecutionChain;

    @Inject
    @QPostExecuteChain
    private Chain postExecutionChain;

    @Inject
    @QNamingStrategy
    private NamingStrategy namingStrategy;

    private void prepareContext(InvocationContext context) {
        if (!context.getContextData().containsKey(Command.METRIC_REGISTRY)) {
            context.getContextData().put(Command.METRIC_REGISTRY, metricRegistry);
        }
        if (!context.getContextData().containsKey(Command.NAMING_STRATEGY)) {
            context.getContextData().put(Command.NAMING_STRATEGY, namingStrategy);
        }
        if (!context.getContextData().containsKey(Command.LOGGER)) {
            context.getContextData().put(Command.LOGGER, LOG);
        }
    }

    @AroundInvoke
    public Object around(InvocationContext context) throws Exception {
        prepareContext(context);
        preExecutionChain.execute(context);
        try {
            return context.proceed();
        } catch (Throwable t) {
            if (context.getMethod().isAnnotationPresent(ExceptionMetered.class)) {
                ExceptionMetered meterd = context.getMethod().getAnnotation(ExceptionMetered.class);
                metricRegistry.meter(namingStrategy.getName(context, meterd)).mark();
            }
            throw t;
        } finally {
            postExecutionChain.execute(context);
        }
    }

    @AroundTimeout
    public void timedout(InvocationContext context) throws Exception {
        prepareContext(context);
        postExecutionChain.execute(context);
    }
}
