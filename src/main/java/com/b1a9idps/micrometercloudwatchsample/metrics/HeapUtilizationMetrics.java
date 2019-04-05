package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

@Component
public class HeapUtilizationMetrics {

    private final RestTemplate ec2RestTemplate;

    public HeapUtilizationMetrics(MeterRegistry registry) {
        this.ec2RestTemplate = new RestTemplate();
        registry.gauge(
                "HeapUtilization",
                Tags.concat(Collections.emptyList(), "InstanceId", getInstanceId()),
                this,
                HeapUtilizationMetrics::invoke);
    }

    private Double invoke() {
        Runtime runtime = Runtime.getRuntime();
        long freeMemorySize = runtime.freeMemory() / 1024;
        long totalMemorySize = runtime.totalMemory() / 1024;
        long usedMemorySize = totalMemorySize - freeMemorySize;
        double usedMemoryPercentage = usedMemorySize * 100 / (double) totalMemorySize;
        BigDecimal result =
                BigDecimal.valueOf(usedMemoryPercentage).setScale(1 , RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    private String getInstanceId() {
        return ec2RestTemplate.getForObject("http://169.254.169.254/latest/meta-data/instance-id", String.class);
    }
}
