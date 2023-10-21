package co.vuckovic.streaming.kafkademo.configuration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import schemaregistry.RideRecord;

@Service
public class KafkaMessageListener {

    @KafkaListener(topics = "topic-1", groupId = "myGroup")
    public void listen (ConsumerRecord<String, RideRecord> record) {
        String key = record.key();
        RideRecord value = record.value();
        System.out.println("*** Key: " + key + " , " + "Value: " + value + " ***");
    }
}
