package com.hualala.demo.kafka;

import org.apache.coyote.Processor;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@EnableScheduling
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private KafkaConfiguration kafkaConfiguration;


    @Scheduled(fixedDelay = 2 * 1000 * 5000, initialDelay = 1 * 1000)
    public void schedule() throws Exception{
        singleProducer();
    }

    private void singleProducerCurrent() throws Exception{
        logger.info("cpu core {}", Runtime.getRuntime().availableProcessors());
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        org.apache.kafka.clients.producer.KafkaProducer kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaConfiguration.producerConfigs());
        int i;
        for (i = 0; i<100;i++) {
            Integer integer = i;
            executorService.execute(() -> {
                logger.info("producer " + " msg" + integer + " start this is data");
                ProducerRecord producerRecord = new ProducerRecord(kafkaProperties.getTopic(), 0, "key", "this is data" + integer);
                kafkaProducer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        logger.info("producer" + " msg" + integer + " finish this is data " + metadata);
                    }
                });
                logger.info("producer" + " msg" + integer + " end this is data");
            });
            Thread.sleep(5);
        }
    }

    private void singleProducer() throws Exception{
        org.apache.kafka.clients.producer.KafkaProducer kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaConfiguration.producerConfigs());
        Thread thread = new Thread() {
            @Override
            public void run() {
                int i;
                for (i = 0; i<100;i++) {
                    Integer integer = i;
                    logger.info("producer " + " msg" + integer + " start this is data");
                    ProducerRecord producerRecord = new ProducerRecord(kafkaProperties.getTopic(), 0, "key", "this is data" + integer);
                    kafkaProducer.send(producerRecord, new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            logger.info("producer" + " msg" + integer + " finish this is data " + metadata);
                        }
                    });
                    logger.info("producer" + " msg" + integer + " end this is data");
                }
            }
        };
        thread.start();
        thread.join();
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                int i;
                for (i = 100; i<200;i++) {
                    Integer integer = i;
                    logger.info("producer " + " msg" + integer + " start this is data");
                    ProducerRecord producerRecord = new ProducerRecord(kafkaProperties.getTopic(), 0, "key", "this is data" + integer);
                    kafkaProducer.send(producerRecord, new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            logger.info("producer" + " msg" + integer + " finish this is data " + metadata);
                        }
                    });
                    logger.info("producer" + " msg" + integer + " end this is data");
                }
            }
        };
        thread2.start();
        thread2.join();


    }

    private void mulProducerAync() {
        int i;
        org.apache.kafka.clients.producer.KafkaProducer[] kafkaProducers = new org.apache.kafka.clients.producer.KafkaProducer[3];
        kafkaProducers[0] = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaConfiguration.producerConfigs());
        kafkaProducers[1] = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaConfiguration.producerConfigs());
        kafkaProducers[2] = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaConfiguration.producerConfigs());
        for (i = 0; i<100;i++) {

            Integer integer = i;
            int offset = integer % 3;
            logger.info("producer"+offset+" msg"+integer+" start this is data");
            ProducerRecord producerRecord = new ProducerRecord(kafkaProperties.getTopic(), 0,"key","this is data" + integer);
            kafkaProducers[offset].send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    logger.info("producer"+offset+" msg"+integer+" finish this is data "+ metadata);
                }
            });
            logger.info("producer"+offset+" msg"+integer+" end this is data");
        }
    }


    private void mulProducerSync() throws Exception{
        int i;
        org.apache.kafka.clients.producer.KafkaProducer[] kafkaProducers = new org.apache.kafka.clients.producer.KafkaProducer[3];
        kafkaProducers[0] = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaConfiguration.producerConfigs());
        kafkaProducers[1] = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaConfiguration.producerConfigs());
        kafkaProducers[2] = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaConfiguration.producerConfigs());
        for (i = 0; i<100;i++) {

            Integer integer = i;
            int offset = integer % 3;
            logger.info("producer"+offset+" msg"+integer+" start this is data");
            ProducerRecord producerRecord = new ProducerRecord(kafkaProperties.getTopic(), 0,"key","this is data" + integer);
            kafkaProducers[offset].send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    logger.info("producer"+offset+" msg"+integer+" finish this is data "+ metadata);
                }
            }).get();
            logger.info("producer"+offset+" msg"+integer+" end this is data");
        }
    }

}
