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
package org.tomitribe.jeewiz.metrics.cdi;

import org.tomitribe.jeewiz.metrics.commands.BaseChain;
import org.tomitribe.jeewiz.metrics.commands.Chain;
import org.tomitribe.jeewiz.metrics.commands.CounterDecrementCommand;
import org.tomitribe.jeewiz.metrics.commands.CounterIncrementCommand;
import org.tomitribe.jeewiz.metrics.commands.DefaultNamingStrategy;
import org.tomitribe.jeewiz.metrics.commands.HistogramCommand;
import org.tomitribe.jeewiz.metrics.commands.MeterCommand;
import org.tomitribe.jeewiz.metrics.commands.NamingStrategy;
import org.tomitribe.jeewiz.metrics.commands.TimerStartCommand;
import org.tomitribe.jeewiz.metrics.commands.TimerStopCommand;
import org.tomitribe.jeewiz.metrics.qualifiers.QNamingStrategy;
import org.tomitribe.jeewiz.metrics.qualifiers.QPostExecuteChain;
import org.tomitribe.jeewiz.metrics.qualifiers.QPreExecuteChain;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author WalmartLabs
 * @author Tomitribe
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/19/15
 *
 * <p>The default factory class for creating the common objects required for annotation processing</p>
 */
public class CommandProducer {
    @Inject
    private Instance<BaseChain> chainInstance;

    @Produces
    @Singleton
    @QNamingStrategy
    private NamingStrategy getNamingStrategy() {
        return new DefaultNamingStrategy();
    }

    @Produces
    @Singleton
    @QPreExecuteChain
    public Chain getPreExecuteChain() {
        Chain c = chainInstance.get();
        c.addCommand(new CounterIncrementCommand());
        c.addCommand(new TimerStartCommand());
        c.addCommand(new MeterCommand());
        c.addCommand(new HistogramCommand());
        return c;
    }

    @Produces
    @Singleton
    @QPostExecuteChain
    public Chain getPostExecureChain() {
        Chain c = chainInstance.get();
        c.addCommand(new CounterDecrementCommand());
        c.addCommand(new TimerStopCommand());

        return c;
    }
}
