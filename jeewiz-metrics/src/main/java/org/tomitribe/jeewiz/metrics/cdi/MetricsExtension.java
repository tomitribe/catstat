/**
 * Copyright (C) 2013 Antonin Stefanutti (antonin.stefanutti@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tomitribe.jeewiz.metrics.cdi;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomitribe.jeewiz.metrics.annotations.Counted;
import org.tomitribe.jeewiz.metrics.annotations.ExceptionMetered;
import org.tomitribe.jeewiz.metrics.annotations.Histogram;
import org.tomitribe.jeewiz.metrics.annotations.Metered;
import org.tomitribe.jeewiz.metrics.annotations.MetricAware;
import org.tomitribe.jeewiz.metrics.annotations.Timed;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.lang.annotation.Annotation;

/**
 * @author WalmartLabs
 * @author Tomitribe
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 5/19/15
 *
 * <p>Creates a HealthCheckRegistry bean for the context</p>
 */
public class MetricsExtension implements Extension {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsExtension.class);

    private void addInterceptorBindings(@Observes BeforeBeanDiscovery bbd, BeanManager manager) {
        LOG.debug("Initialized Metric Extension");
    }

    private <T> void metricsAnnotations(@Observes ProcessAnnotatedType<T> pat) {
        AnnotatedType<T> at = pat.getAnnotatedType();
        String className = at.getJavaClass().getName();

        if (!className.startsWith("com.walmart.platform.metrics") ||
            !className.startsWith("org.apache")) {

            LOG.debug("Observed Type: {}", pat.getAnnotatedType().getJavaClass().getName());
            boolean isMetricAnnotated = false;
            for (AnnotatedMethod<? super T> m : at.getMethods()) {
                if (m.isAnnotationPresent(Counted.class) ||
                    m.isAnnotationPresent(Timed.class) ||
                    m.isAnnotationPresent(Metered.class) ||
                    m.isAnnotationPresent(ExceptionMetered.class) ||
                    m.isAnnotationPresent(Histogram.class)) {
                    isMetricAnnotated = true;
                    break;
                }
            }

            if (isMetricAnnotated) {
                Annotation metricAware = new Annotation() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return MetricAware.class;
                    }
                };

                AnnotatedTypeWrapper<T> wrapper = new AnnotatedTypeWrapper<T>(at, at.getAnnotations());
                wrapper.addAnnotation(metricAware);

                pat.setAnnotatedType(wrapper);

                LOG.debug("@@@--> Created wrapped class: {}", at.getJavaClass().getName());
            }
        }
    }

    private void defaultMetricRegistry(@Observes AfterBeanDiscovery abd, BeanManager manager) {
        if (manager.getBeans(MetricRegistry.class).isEmpty())
            abd.addBean(new MetricRegistryBean(manager));

        if (manager.getBeans(HealthCheckRegistry.class).isEmpty())
            abd.addBean(new HealthRegistryBean(manager));

        LOG.debug("Beans Discovered");
    }
}