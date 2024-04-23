package com.xhf.user.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean("myRedissonConfig")
//    @Bean
    public Redisson redissonConfig() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort).setDatabase(0);
        if (StringUtils.isNotBlank(redisPassword)) {
            config.useSingleServer().setPassword(redisPassword);
        }
        RedissonClient redisson = Redisson.create(config);
        return (Redisson) redisson;
    }
}
