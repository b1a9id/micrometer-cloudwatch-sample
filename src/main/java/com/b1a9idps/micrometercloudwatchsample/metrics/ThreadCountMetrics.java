package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Collections;

import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.stereotype.Component;

import com.amazonaws.util.EC2MetadataUtils;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

@ConditionalOnAwsCloudEnvironment
@Component
public class ThreadCountMetrics {
    public ThreadCountMetrics(MeterRegistry registry) {
        registry.gauge(
                "ThreadCount",
                Tags.concat(Collections.emptyList(), "InstanceId", EC2MetadataUtils.getInstanceId()),
                this,
                ThreadCountMetrics::invoke);
    }

    private Integer invoke() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        return threadBean.getThreadCount();
    }
}
