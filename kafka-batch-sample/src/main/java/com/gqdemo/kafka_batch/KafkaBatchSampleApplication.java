package com.gqdemo.kafka_batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@SpringBootApplication
public class KafkaBatchSampleApplication {


    public static void main(String[] args) {
        SpringApplication.run(KafkaBatchSampleApplication.class, args);
    }

    @KafkaListener(id = "batch-out", topics = "batch-out")
    public void listen(String in) {
        System.out.println(in);
    }

    @Bean
    public ApplicationRunner runner(KafkaTemplate<byte[], byte[]> template) {
        return args -> IntStream.range(0, 10).forEach(i -> template.send("batch-in", ("\"test" + i + "\"").getBytes()));
    }
}

//@Configuration
//class KafkaConfig {
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServerAddress;
//
//    private ProducerFactory<byte[], byte[]> producerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerAddress);
//        configProps.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<byte[], byte[]> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//}

class Base {

    @Autowired
    StreamBridge bridge;
}

@Component
@Profile("default")
class NoTransactions extends Base {

    @Bean
    Consumer<List<String>> consumer() {
        return list -> list.forEach(str -> bridge.send("output-out-0", str.toUpperCase(Locale.ROOT)));
    }
}