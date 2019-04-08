package com.b1a9idps.micrometercloudwatchsample.metrics.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("aws.ec2")
public class MetricsProps {
    private String metaDataUrl;

    public String getMetaDataUrl() {
        return metaDataUrl;
    }

    public void setMetaDataUrl(String metaDataUrl) {
        this.metaDataUrl = metaDataUrl;
    }
}
