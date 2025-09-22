package com.bq.backend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StringProducerService {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendMessage(String message){
        kafkaTemplate.send("str-topic",message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Error sending message", ex);
            }
            log.info("Message sent: {}", message);
            log.info("Partition: {}, Offset: {}", result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
        });
    }
}
