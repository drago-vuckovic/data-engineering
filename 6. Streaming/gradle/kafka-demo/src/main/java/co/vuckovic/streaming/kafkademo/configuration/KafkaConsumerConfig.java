package co.vuckovic.streaming.kafkademo.configuration;

import co.vuckovic.streaming.kafkademo.YourMessageListener;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import schemaregistry.RideRecord;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.schema.registry.registry-url}")
    private String schemaRegistry;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
//    public KafkaConsumer<String, Object> kafkaConsumerConfig() {
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

        // Specify the schema registry URL
//        consumerProps.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        consumerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);


//        return new KafkaConsumer<>(consumerProps);
        return consumerProps;
    }

    @Bean
    public ConsumerFactory<String, RideRecord> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfig());
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
    @Bean
    public ConcurrentMessageListenerContainer<String, RideRecord> messageListenerContainer() {
        ContainerProperties containerProps = new ContainerProperties("topic-1");
        containerProps.setMessageListener(new YourMessageListener()); // Implement your message listener

        // Configure any additional container properties as needed
//        ListenerContainerProperties listenerContainerProperties = containerProps.getListenerContainerProperties();
//        MethodOrClassLevelMetadata methodOrClassLevelMetadata = listenerContainerProperties.getMetadata();
        // Configure additional metadata if necessary

        return new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProps);
    }
}