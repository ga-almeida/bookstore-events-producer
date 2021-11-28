package com.gaalmeida.bookstoreeventsproducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStore;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEventType;
import com.gaalmeida.bookstoreeventsproducer.provider.KafkaProvider;
import org.springframework.stereotype.Service;

@Service
public class BookStoreService {

    private KafkaProvider kafkaProvider;

    public BookStoreService(KafkaProvider kafkaProvider) {
        this.kafkaProvider = kafkaProvider;
    }

    public BookStore create(BookStore bookStore) throws JsonProcessingException {
        // dispara o evento
        kafkaProvider.senBookStoreEvent(bookStore, BookStoreEventType.NEW);
        return bookStore;
    }
}
