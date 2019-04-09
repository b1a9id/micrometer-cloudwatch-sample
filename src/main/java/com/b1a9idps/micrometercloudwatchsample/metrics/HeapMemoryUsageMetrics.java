package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Collections;

import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.stereotype.Component;

import com.amazonaws.util.EC2MetadataUtils;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

@ConditionalOnAwsCloudEnvironment
@Component
public class HeapMemoryUsageMetrics {
    public HeapMemoryUsageMetrics(MeterRegistry registry) {
        registry.gauge(
                "HeapMemoryUsage",
                Tags.concat(Collections.emptyList(), "InstanceId", EC2MetadataUtils.getInstanceId()),
                this,
                HeapMemoryUsageMetrics::invoke);
    }

    private Long invoke() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }
}
