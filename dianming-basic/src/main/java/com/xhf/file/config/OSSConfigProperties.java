package com.xhf.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OSSConfigProperties {
    private String bucketName;
    private String accessKeyId;
    private String secretAccessKey;
    private String endpoint;
    private String bucketEndpoint;
}
