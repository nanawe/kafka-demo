package com.hualala.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaComsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaComsumer.class);

    @KafkaListener(topics = {"${spring.kafka.template.default-topic}"}, containerFactory = "kafkaListenerContainerFactory")
    public void listen(List<ConsumerRecord> list, Acknowledgment ack) {
        logger.info("list:{}", list);
        ack.acknowledge();
    }
}
