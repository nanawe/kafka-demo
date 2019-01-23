package com.hualala.demo.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListenerAdapter;

public class MineProducerListener<K, V> extends ProducerListenerAdapter<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MineProducerListener.class);

    @Override
    public void onSuccess(String topic, Integer partition, K key, V value, RecordMetadata recordMetadata) {
        logger.info("topic: {},partition: {} key: {}, value: {} data: {}", topic, partition, key, value, recordMetadata);
    }

    @Override
    public void onError(String topic, Integer partition, K key, V value, Exception exception) {
        logger.error("topic: {},partition: {} key: {}, value: {} ", topic, partition, key, value, exception);
    }
}
