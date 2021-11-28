package com.gaalmeida.bookstoreeventsproducer.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStore;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEventType;
import com.gaalmeida.bookstoreeventsproducer.provider.contract.KafkaProvider;
import com.gaalmeida.bookstoreeventsproducer.service.contract.BookStoreService;
import org.springframework.stereotype.Service;

@Service
public class BookStoreServiceImpl implements BookStoreService {

    private final KafkaProvider kafkaProvider;

    public BookStoreServiceImpl(KafkaProvider kafkaProvider) {
        this.kafkaProvider = kafkaProvider;
    }

    @Override
    public BookStore create(BookStore bookStore) throws JsonProcessingException {
        // dispara o evento
        kafkaProvider.senBookStoreEvent(bookStore, BookStoreEventType.NEW);
        return bookStore;
    }

    @Override
    public BookStore update(BookStore bookStore) throws JsonProcessingException {
        // dispara o evento
        kafkaProvider.sendLibraryEvent(bookStore, BookStoreEventType.UPDATE);
        return bookStore;
    }
}
