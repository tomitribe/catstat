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
/**
 * @author WalmartLabs
 * @author Tomitribe
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 * Created: 5/19/15
 *
 * <p>The annotations found in this package are used for creating and automating metric production and consumption
 * </p>
 *
 * <ul>
 *     <li>Counted - Provides a simple counter used for method invocation counts</li>
 *     <li>Exception Metered - Provides a Meter for specific throwable classes during invocation processing</li>
 *     <li>Histogram - Provides a decaying backed histogram</li>
 *     <li>Metered - Provides a simple meter</li>
 *     <li>Timed - Provides a timer around invocation</li>
 *     <li>Metric Aware - Is used as the decorator annotation for observing decorated methods</li>
 * </ul>
 */
package org.tomitribe.jeewiz.metrics.annotations;