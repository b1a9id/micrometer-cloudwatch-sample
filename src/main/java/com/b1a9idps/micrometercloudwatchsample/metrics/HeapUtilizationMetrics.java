package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.b1a9idps.micrometercloudwatchsample.metrics.props.MetricsProps;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

@Component
public class HeapUtilizationMetrics {
    private final RestTemplate ec2RestTemplate;
    private final MetricsProps metricsProps;

    public HeapUtilizationMetrics(MeterRegistry registry, MetricsProps metricsProps) {
        this.ec2RestTemplate = new RestTemplate();
        this.metricsProps = metricsProps;
        registry.gauge(
                "HeapUtilization",
                Tags.concat(Collections.emptyList(), "InstanceId", getInstanceId()),
                this,
                HeapUtilizationMetrics::invoke);
    }

    private Long invoke() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long max = memoryMXBean.getHeapMemoryUsage().getMax();
        long used = memoryMXBean.getHeapMemoryUsage().getUsed();
        return used * 100 / max;
    }

    private String getInstanceId() {
        return ec2RestTemplate.getForObject(metricsProps.getMetaDataUrl() + "/instance-id", String.class);
    }
}
