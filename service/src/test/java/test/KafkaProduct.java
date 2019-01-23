package test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaProduct {
    @Test
    public void test() {
        try{
            KafkaProducer<String, String> producer;

            Properties props = new Properties();
            props.put("bootstrap.servers", "172.16.33.28:9092,172.16.33.37:9092,172.16.33.39:9092");
//            props.put("bootstrap.servers","172.16.32.32:9092,172.16.32.162:9092,172.16.32.221:9092");
            props.put("acks", "all");
            props.put("retries", "0");
            props.put("batch.size", "16384");
            props.put("linger.ms", "1");
            props.put("buffer.memory", "33554432");
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("max.request.size", "10485760");
            props.put("compression.type", "snappy");

            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            props.put("group.id", "shopDohkoDataSynGroupLocal");
            props.put("enable.auto.commit", "true");
            props.put("auto.commit.interval.ms", "1000");


            //---------------------------------------------
           producer = new KafkaProducer<String, String>(props);
            String topic = "db_shop_temp";
            int partition = 0;
            String key = "zhang";
            String value = "shidong";
            ProducerRecord<String, String> record = new ProducerRecord(topic, partition, key, value);
            producer.send(record).get();

            //---------------------------------------------
            System.out.println("");
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList("db_shop_temp"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record_t : records) {
                    System.out.println(record_t);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
