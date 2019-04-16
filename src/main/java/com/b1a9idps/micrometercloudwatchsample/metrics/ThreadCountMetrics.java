package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.stereotype.Component;

import com.amazonaws.util.EC2MetadataUtils;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@ConditionalOnAwsCloudEnvironment
@Component
public class ThreadCountMetrics {
    public ThreadCountMetrics(MeterRegistry registry) {
        Gauge.builder("ThreadCount", this, ThreadCountMetrics::invoke)
                .tag("InstanceId", EC2MetadataUtils.getInstanceId())
                .baseUnit("Count")
                .register(registry);
    }

    private Integer invoke() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        return threadBean.getThreadCount();
    }
}
