package com.gaalmeida.bookstoreeventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEvent;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEventType;
import com.gaalmeida.bookstoreeventsproducer.producer.BookStoreEventProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
public class BookStoreEventController {

    private BookStoreEventProducer bookStoreEventProducer;

    public BookStoreEventController(BookStoreEventProducer bookStoreEventProducer) {
        this.bookStoreEventProducer = bookStoreEventProducer;
    }

    @PostMapping("/v1/bookstore-events")
    public ResponseEntity<BookStoreEvent> postBookStoreEvent(@RequestBody @Valid BookStoreEvent bookStoreEvent) throws JsonProcessingException {

        // um novo livro e adicionado na bilioca online
        bookStoreEvent.setBookStoreEventType(BookStoreEventType.NEW);

        // dispara o evento
        bookStoreEventProducer.senBookStoreEvent_Approach2(bookStoreEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookStoreEvent);
    }

    @PutMapping("/v1/bookstore-events")
    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid BookStoreEvent bookStoreEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        if(bookStoreEvent.getBookStoreEventId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eai passa por favor o EventId mano");
        }

        bookStoreEvent.setBookStoreEventType(BookStoreEventType.UPDATE);
        bookStoreEventProducer.senBookStoreEvent_Approach2(bookStoreEvent);
        return ResponseEntity.status(HttpStatus.OK).body(bookStoreEvent);
    }
}
