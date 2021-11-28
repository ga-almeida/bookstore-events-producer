package com.gaalmeida.bookstoreeventsproducer.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStore;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEventType;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface KafkaProvider {
    public void sendLibraryEvent(BookStore bookStore, BookStoreEventType bookStoreEventType) throws JsonProcessingException;
    public ListenableFuture<SendResult<Integer, String>> senBookStoreEvent(BookStore bookStore, BookStoreEventType bookStoreEventType) throws JsonProcessingException;
    public SendResult<Integer, String> sendBookStoreEventSynchronous(BookStore bookStore, BookStoreEventType bookStoreEventType) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException;
}
