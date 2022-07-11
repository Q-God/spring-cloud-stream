package org.gqdemo.kafka_native_serialization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class KafkaNativeSerializationApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaNativeSerializationApplication.class,args);
    }

    @Bean
    public Function<String,Person> process()
    {
        return str ->{
            Person item = new Person(str);
            return item;
        };
    }

}
