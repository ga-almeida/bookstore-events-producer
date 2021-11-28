package com.gaalmeida.bookstoreeventsproducer.provider.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStore;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEvent;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEventType;
import com.gaalmeida.bookstoreeventsproducer.provider.contract.KafkaProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class BookStoreEventImpl implements KafkaProvider {

    private static final int RANDOM_MIN = 1;
    private static final int RANDOM_MAX = 100;

    @Value(value = "${spring.kafka.template.default-topic}")
    private String topic;

    private KafkaTemplate<Integer, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public BookStoreEventImpl(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // mandar uma mensagem para o topico de forma assincrona
    @Override
    public void sendLibraryEvent(BookStore bookStore, BookStoreEventType bookStoreEventType) throws JsonProcessingException {
        BookStoreEvent bookStoreEvent = getBookStoreEvent(bookStore, bookStoreEventType);

        Integer key = bookStoreEvent.getBookStoreEventId();
        String value = objectMapper.writeValueAsString(bookStoreEvent);

        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    @Override
    public ListenableFuture<SendResult<Integer, String>> senBookStoreEvent(BookStore bookStore, BookStoreEventType bookStoreEventType) throws JsonProcessingException {
        BookStoreEvent bookStoreEvent = getBookStoreEvent(bookStore, bookStoreEventType);

        Integer key = bookStoreEvent.getBookStoreEventId();
        String value = objectMapper.writeValueAsString(bookStoreEvent);

        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topic);

        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });

        return listenableFuture;
    }

    @Override
    public SendResult<Integer, String> sendBookStoreEventSynchronous(BookStore bookStore, BookStoreEventType bookStoreEventType) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        BookStoreEvent bookStoreEvent = getBookStoreEvent(bookStore, bookStoreEventType);

        Integer key = bookStoreEvent.getBookStoreEventId();
        String value = objectMapper.writeValueAsString(bookStoreEvent);
        SendResult<Integer, String> sendResult = null;

        try {
            sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            log.error("ExecutionException/InterruptedException Mandando uma mensagem com a exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Mandando uma mensagem com a exception {}", e.getMessage());
            throw e;
        }

        return sendResult;
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Mandando uma mensagem com a exception {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {

        // Manda um KafkaRecord com Headers usando o KafkaTemplate
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Mensagem enviada com sucesso com o a key : {}  e com o valor {} , e com a partition {}", key, value, result.getRecordMetadata().partition());
    }

    private int getRandomEventId() {
        return new Random().ints(RANDOM_MIN, RANDOM_MAX).findFirst().getAsInt();
    }

    private BookStoreEvent getBookStoreEvent(BookStore bookStore, BookStoreEventType bookStoreEventType) {
        return BookStoreEvent
                .builder()
                .bookStore(bookStore)
                .bookStoreEventId(getRandomEventId())
                .bookStoreEventType(bookStoreEventType)
                .build();
    }
}
