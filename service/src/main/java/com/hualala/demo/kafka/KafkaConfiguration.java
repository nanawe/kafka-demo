package com.hualala.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.ProducerListener;

import java.util.HashMap;
import java.util.Map;


/**
 * kafka配置
 **/
@Configuration
@EnableKafka
public class KafkaConfiguration {
    @Autowired
    private KafkaProperties environment;


//<entry key="bootstrap.servers" value="${bootstrap.servers}"/>
//		     	<entry key="group.id" value="0"/>
//		     	<entry key="retries" value="10"/>
//		     	<entry key="batch.size" value="16384"/>
//		     	<entry key="linger.ms" value="1"/>
//		     	<entry key="buffer.memory" value="33554432"/>
//		     	<entry key="key.serializer" value="org.apache.kafka.common.serialization.IntegerSerializer"/>
//		     	<entry key="value.serializer" value="org.apache.kafka.common.serialization.StringSerializer"/>



    @Bean
    public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setBatchListener(false);
        factory.setConcurrency(7);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getServices());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getGroupID());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, environment.getTimeOut());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,1000);
        props.put("zookeeper.sync.time.ms", environment.getTimeMs());
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, environment.getIntervalMs());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 600000);
        //--------kafka集群2.0 授权需增加的配置-----------------
//        props.put("sasl.mechanism", "PLAIN");
//        props.put("security.protocol", "SASL_PLAINTEXT");
        return props;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> producerProperties = new HashMap<>();
//        producerProperties.put("bootstrap.servers", "172.16.33.28:9092");
//        producerProperties.put("sasl.mechanism", "PLAIN");
//        producerProperties.put("security.protocol", "SASL_PLAINTEXT");
        producerProperties.put("bootstrap.servers", "172.16.33.28:9092,172.16.33.37:9092,172.16.33.39:9092");//,172.16.0.31:9092,172.16.0.32:9092
//        producerProperties.put("group.id", "0");
//        producerProperties.put("retries", "10");
//        producerProperties.put("batch.size", "16384");
//        producerProperties.put("linger.ms", "1");
//        producerProperties.put("buffer.memory", "33554432");
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return producerProperties;
    }

    @Bean
    @ConditionalOnMissingBean({ProducerFactory.class})
    public ProducerFactory<Object, Object> kafkaProducerFactory() {
        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory(producerConfigs());
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean({KafkaTemplate.class})
    public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory, ProducerListener<Object, Object> kafkaProducerListener) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate(kafkaProducerFactory);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
//        kafkaTemplate.setDefaultTopic("db_shop_basedoc_test");
        return kafkaTemplate;
    }

    @Bean
    @ConditionalOnMissingBean({ProducerListener.class})
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new MineProducerListener();
    }
}
