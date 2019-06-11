package com.hualala.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class KafkaComsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaComsumer.class);

    private static AtomicInteger i = new AtomicInteger();

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "${spring.kafka.template.default-topic}", partitions = {"0"})}, containerFactory = "kafkaListenerContainerFactory")
//    @KafkaListener(topics = {"${spring.kafka.template.default-topic}"}, containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<?,?> list) {

        logger.info(Thread.currentThread().getName() + ": "+i.get() + "list:{}", list);
        i.incrementAndGet();
//        ack.acknowledge();
    }
}
