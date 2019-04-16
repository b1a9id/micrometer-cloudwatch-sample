package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.stereotype.Component;

import com.amazonaws.util.EC2MetadataUtils;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@ConditionalOnAwsCloudEnvironment
@Component
public class HeapMemoryUsageMetrics {
    public HeapMemoryUsageMetrics(MeterRegistry registry) {
        Gauge.builder("HeapMemoryUsage", this, HeapMemoryUsageMetrics::invoke)
                .tag("InstanceId", EC2MetadataUtils.getInstanceId())
                .baseUnit("bytes")
                .register(registry);
    }

    private Long invoke() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }
}
