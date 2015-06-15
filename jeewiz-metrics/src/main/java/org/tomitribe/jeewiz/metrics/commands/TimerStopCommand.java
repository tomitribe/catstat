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

import com.codahale.metrics.Timer;
import org.tomitribe.jeewiz.metrics.annotations.Timed;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

/**
 * @author WalmartLabs
 * @author Tomitribe
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/19/15
 */
public class TimerStopCommand implements Command {

    @Override
    public boolean isExecutable(Method method) {
        return method.isAnnotationPresent(Timed.class);
    }

    @Override
    public void execute(InvocationContext context) {
        Timed t = context.getMethod().getAnnotation(Timed.class);
        String name = getName(context, t);
        Timer.Context tc = (Timer.Context) context.getContextData().get(Command.METRIC_TIMER+"-"+name);
        if (tc != null) {
            tc.stop();
        }
    }
}
