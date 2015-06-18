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
package org.tomitribe.jeewiz.web.controller;

import com.codahale.metrics.MetricRegistry;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.tomitribe.jeewiz.metrics.qualifiers.QMetricRegistry;
import org.tomitribe.jeewiz.web.ejb.TemporalCollector;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/5/15
 */
@Named
@RequestScoped
public class DashboardController {
    private MeterGaugeChartModel meterGaugeChartModel;
    private LineChartModel lineChartModel;

    @Inject
    private TemporalCollector collector;

    @Inject
    @QMetricRegistry
    private MetricRegistry metricRegistry;

    @PostConstruct
    public void init() {
        meterGaugeChartModel = new MeterGaugeChartModel();

        long maxMemory = Long.parseLong(metricRegistry.getGauges().get("jvm.memory.total.max").getValue().toString());
        long currenUsed = Long.parseLong(metricRegistry.getGauges().get("jvm.memory.total.used").getValue().toString());

        double usedMB = (currenUsed/1024)/1024;
        double maxMB = (maxMemory/1024)/1024;
        double max90 = maxMB*.9;
        double max80 = maxMB*.8;
        double max70 = maxMB*.7;
        List<Number> intervals = new ArrayList<Number>(){{
            add(max70);
            add(max80);
            add(max90);
            add(maxMB);
        }};
        meterGaugeChartModel = new MeterGaugeChartModel(100, intervals);

        meterGaugeChartModel.setTitle("JVM Memory");
        meterGaugeChartModel.setSeriesColors("66cc66,93b75f,E7E658,cc6666");
        meterGaugeChartModel.setGaugeLabel("Memory Used");
        meterGaugeChartModel.setGaugeLabelPosition("bottom");
        meterGaugeChartModel.setShadow(true);
        meterGaugeChartModel.setShowTickLabels(false);
        meterGaugeChartModel.setLabelHeightAdjust(110);
        meterGaugeChartModel.setIntervalOuterRadius(100);

        meterGaugeChartModel.setValue(usedMB);

        lineChartModel = new LineChartModel();
        lineChartModel.setTitle("Linear Chart");
        lineChartModel.setLegendPosition("e");
        Axis yAxis = lineChartModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(20);

        ChartSeries series = new ChartSeries("Login/min");
        for (int i=0;i<collector.getValues().size(); i++) {
            series.set(i, collector.getValues().get(i));
        }
        lineChartModel.addSeries(series);
    }

    public MeterGaugeChartModel getMeterGaugeChartModel() {
        return meterGaugeChartModel;
    }

    public LineChartModel getLineChartModel() {
        return lineChartModel;
    }
}