package co.vuckovic.streaming.kafkademo;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.GenericMessageListener;
import org.springframework.stereotype.Service;
import schemaregistry.RideRecord;

import java.io.IOException;

@Service // or @Component, depending on your use case
public class KafkaMessageListener {

    @KafkaListener(topics = "topic-1", groupId = "myGroup")
    public void listen(ConsumerRecord<String, byte[]> record) throws IOException {
        String key = record.key();
        byte[] avroData = record.value();

        // Deserialize the Avro data without the need for a schema registry
        DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(RideRecordAvroSchema.SCHEMA);
        Decoder decoder = DecoderFactory.get().binaryDecoder(avroData, null);
        GenericRecord avroRecord = datumReader.read(null, decoder);

        // Process the Avro record using the Avro schema embedded in the data
        System.out.println("*** Key: " + key + " , " + "Value: " + avroRecord + " ***");
    }
}


//public class KafkaMessageListener implements GenericMessageListener<RideRecord> {
//    @Override
//    public void onMessage(ConsumerRecord<String, RideRecord> record) {
//        String key = record.key();
//        RideRecord value = record.value();
//        System.out.println("*** Key: " + key + " , " + "Value: " + value + " ***");
//    }

//    @Override
//    public void onMessage(RideRecord data) {
//
//    }
//}



//@Service
//public class KafkaMessageListener {
//
//    @KafkaListener(topics = "topic-1", groupId = "myGroup")
//    public void listen (ConsumerRecord<String, RideRecord> record) {
//        String key = record.key();
//        RideRecord value = record.value();
//        System.out.println("*** Key: " + key + " , " + "Value: " + value + " ***");
//    }
//}
