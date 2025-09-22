package com.bq.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.RecordInterceptor;

import java.util.HashMap;

@Slf4j
@Configuration
public class StringConsumerConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        var configs = new HashMap<String,Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaProperties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,String> validMessageContainerFactory(ConsumerFactory<String, String> consumerFactory){
        var factory = new ConcurrentKafkaListenerContainerFactory<String,String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordInterceptor(validMessage());
        return factory;
    }

    private RecordInterceptor<String, String> validMessage(){
        return (record, consumer) -> {
            if(record.value().contains("Test")){
                log.info("Containing Test word, skipping...");
                return record;
            }
            return record;
        };
    }

}
