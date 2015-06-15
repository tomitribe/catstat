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

import com.codahale.metrics.ExponentiallyDecayingReservoir;
import org.tomitribe.jeewiz.metrics.annotations.Histogram;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author WalmartLabs
 * @author Tomitribe
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/19/15
 */
public class HistogramCommand implements Command {
    private static final Set<String> existing = new HashSet<>();

    @Override
    public boolean isExecutable(Method method) {
        return method.isAnnotationPresent(Histogram.class);
    }

    @Override
    public void execute(InvocationContext context) {
        Histogram h = context.getMethod().getAnnotation(Histogram.class);
        String name = getName(context, h);
        if (!existing.contains(name)) {
            getLogger(context).debug("--@--> Creating histogram: {}", name);
            com.codahale.metrics.Histogram histogram =
                new com.codahale.metrics.Histogram(new ExponentiallyDecayingReservoir(h.size(), h.alpha()));
            getRegistry(context).register(name, histogram);
            histogram.update(h.incrementBy());
            existing.add(name);
        } else {
            if (h.autoIncrement()) {
                com.codahale.metrics.Histogram histogram = getRegistry(context).histogram(name);
                histogram.update(h.incrementBy());
            }
        }
    }
}
