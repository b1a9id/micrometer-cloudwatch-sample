package com.b1a9idps.micrometercloudwatchsample.metrics;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class HeapUtilizationMetrics {

    public HeapUtilizationMetrics(MeterRegistry registry) {
        registry.gauge("jvm.memory.utilization", this, HeapUtilizationMetrics::invoke);
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
}
