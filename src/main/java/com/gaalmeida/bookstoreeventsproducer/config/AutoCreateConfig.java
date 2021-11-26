package com.gaalmeida.bookstoreeventsproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateConfig {

    // criando a configuração e adicionando as propriedades de administrador em application.yml
    // isso não é recomendado para ambiente de produção
    @Bean
    public NewTopic booksStoreEvents() {

        return TopicBuilder.name("bookstore-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
