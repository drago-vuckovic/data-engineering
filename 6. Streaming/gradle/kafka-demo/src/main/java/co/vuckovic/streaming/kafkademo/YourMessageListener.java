package co.vuckovic.streaming.kafkademo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import schemaregistry.RideRecord;

public class YourMessageListener {

    @KafkaListener(topics = "topic-1", groupId = "myGroup")
    public void listen (ConsumerRecord<String, RideRecord> record) {
        String key = record.key();
        RideRecord value = record.value();
        System.out.println("*** Key: " + key + " , " + "Value: " + value + " ***");
    }
}
