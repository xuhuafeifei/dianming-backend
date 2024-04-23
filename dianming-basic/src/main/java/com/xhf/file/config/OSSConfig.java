package com.xhf.file.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.xhf.file.service.OSSService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties({OSSConfigProperties.class})
@ConditionalOnClass(OSSService.class)
public class OSSConfig {
    @Autowired
    private OSSConfigProperties properties;

    @Bean
    public OSS buildOOSClient() {
        return new OSSClientBuilder().build(
                properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getSecretAccessKey()
        );
    }
}
