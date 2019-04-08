package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.b1a9idps.micrometercloudwatchsample.metrics.props.MetricsProps;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

@Component
public class ThreadCountMetrics {
    private final RestTemplate ec2RestTemplate;
    private final MetricsProps metricsProps;

    public ThreadCountMetrics(MeterRegistry registry, MetricsProps metricsProps) {
        this.ec2RestTemplate = new RestTemplate();
        this.metricsProps = metricsProps;
        registry.gauge(
                "ThreadCount",
                Tags.concat(Collections.emptyList(), "InstanceId", getInstanceId()),
                this,
                ThreadCountMetrics::invoke);
    }

    private Integer invoke() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        return threadBean.getThreadCount();
    }

    private String getInstanceId() {
        return ec2RestTemplate.getForObject(metricsProps.getMetaDataUrl() + "/instance-id", String.class);
    }
}
