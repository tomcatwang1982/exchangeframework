package com.tomcatwang.core.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.producer.retries}")
    private int retries;
    @Value("${spring.kafka.producer.batch-size}")
    private int batchSize;
    //@Value("${spring.kafka.producer.linger}")
    //private int linger;
    @Value("${spring.kafka.producer.buffer-memory}")
    private int bufferMemory;

    @Value("${spring.kafka.consumer.keyDeserializer}")
    private String consumerKeyDeserializer;
    @Value("${spring.kafka.consumer.valueDeserializer}")
    private String consumerValueDeserializer;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String consumerEnableAutoCommit;

    @Value("${spring.kafka.consumer.concurrency}")
    private int consumeroncurrency;
    @Value("${spring.kafka.consumer.polltimeout}")
    private long consumerPolltimeout;
    @Value("${spring.kafka.consumer.batch.listener}")
    private boolean consumerBatchListener = true;


    public Map<String, Object> producerConfigs() {
        Map<String, Object> producerProps = new HashMap<>();

        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        producerProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        //producerProps.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return producerProps;
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> consumerProps = new HashMap<>();

        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerEnableAutoCommit);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerValueDeserializer);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerKeyDeserializer);

        return consumerProps;
    }


    public ProducerFactory<Object, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<Object, Object>(consumerConfigs());
    }

    @Bean("kafkaTemplate")
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return new KafkaTemplate<Object, Object>(producerFactory());
    }


    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(consumeroncurrency);
        factory.setBatchListener(consumerBatchListener);
        factory.getContainerProperties().setPollTimeout(consumerPolltimeout);
        return factory;
    }

}
