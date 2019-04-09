package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Collections;

import org.springframework.stereotype.Component;

import com.amazonaws.util.EC2MetadataUtils;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

@Component
public class HeapUtilizationMetrics {
    public HeapUtilizationMetrics(MeterRegistry registry) {
        registry.gauge(
                "HeapUtilization",
                Tags.concat(Collections.emptyList(), "InstanceId", EC2MetadataUtils.getInstanceId()),
                this,
                HeapUtilizationMetrics::invoke);
    }

    private Long invoke() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long max = memoryMXBean.getHeapMemoryUsage().getMax();
        long used = memoryMXBean.getHeapMemoryUsage().getUsed();
        return used * 100 / max;
    }
}
