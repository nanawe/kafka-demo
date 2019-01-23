package com.hualala.demo.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * Created by zhengzuowei on 2019/12/21.
 */
@lombok.Data
@Component
@ConfigurationProperties
public class KafkaProperties {
    @Value("${spring.kafka.bootstrap-servers}")
    private String services;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupID;
    @Value("${spring.kafka.consumer.timeOut}")
    private String timeOut;
    //默认1000
    @Value("${spring.kafka.consumer.time.ms}")
    private String timeMs;
    @Value("${spring.kafka.consumer.interval.ms}")
    private String intervalMs;
}
