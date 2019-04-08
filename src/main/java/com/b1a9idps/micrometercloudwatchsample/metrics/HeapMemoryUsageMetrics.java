package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.function.DoubleUnaryOperator;

import javax.swing.plaf.synth.Region;

import org.springframework.boot.web.client.RestTemplateBuilder;
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

    private Double invoke() {
        Runtime runtime = Runtime.getRuntime();
        long freeMemorySize = runtime.freeMemory() / 1024;
        long totalMemorySize = runtime.totalMemory() / 1024;
        long usedMemorySize = totalMemorySize - freeMemorySize;
        return (double) usedMemorySize;
    }

    private String getInstanceId() {
        return ec2RestTemplate.getForObject(metricsProps.getMetaDataUrl() + "/instance-id", String.class);
    }
}
