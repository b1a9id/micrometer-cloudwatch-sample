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
public class HeapMemoryUsageMetrics {
    private final RestTemplate ec2RestTemplate;
    private final MetricsProps metricsProps;

    public HeapMemoryUsageMetrics(MeterRegistry registry, MetricsProps metricsProps) {
        this.ec2RestTemplate = new RestTemplate();
        this.metricsProps = metricsProps;
        registry.gauge(
                "HeapMemoryUsage",
                Tags.concat(Collections.emptyList(), "InstanceId", getInstanceId()),
                this,
                HeapMemoryUsageMetrics::invoke);
    }

    private Long invoke() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    private String getInstanceId() {
        return ec2RestTemplate.getForObject(metricsProps.getMetaDataUrl() + "/instance-id", String.class);
    }
}
