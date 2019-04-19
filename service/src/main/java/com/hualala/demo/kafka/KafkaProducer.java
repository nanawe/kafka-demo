package com.hualala.demo.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class KafkaProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    private int i = 1;

    @Scheduled(fixedDelay = 2 * 1000, initialDelay = 1 * 1000)
    public void schedule() {
        i++;
        kafkaTemplate.send("bd_canal_passport", "this is data"+i);
    }

}
