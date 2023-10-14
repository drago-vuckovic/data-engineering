package co.vuckovic.streaming.kafkademo;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaSenderExample {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private KafkaTemplate<String, String> kafkaTemplate;
    private RoutingKafkaTemplate routingKafkaTemplate;
    private KafkaTemplate<String, User> userKafkaTemplate;

    void sendMessage(String message, String topicName) {
        LOG.info("Sending : {}", message);
        LOG.info("--------------------------------");

        kafkaTemplate.send(topicName, message);
    }

    void sendWithRoutingTemplate(String message, String topicName) {
        LOG.info("Sending : {}", message);
        LOG.info("--------------------------------");

        routingKafkaTemplate.send(topicName, message.getBytes());
    }

    void sendCustomMessage(User user, String topicName) {
        LOG.info("Sending Json Serializer : {}", user);
        LOG.info("--------------------------------");

        userKafkaTemplate.send(topicName, user);
    }

    void sendMessageWithCallback(String message, String topicName) {
        LOG.info("Sending : {}", message);
        LOG.info("---------------------------------");

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                LOG.warn(
                        "Failure Callback: Unable to deliver message [{}]. {}",
                        message, ex.getMessage());
            } else {
                LOG.info(
                        "Success Callback: [{}] delivered with offset -{}",
                        message, result.getRecordMetadata().offset());
            }
        });
    }
}