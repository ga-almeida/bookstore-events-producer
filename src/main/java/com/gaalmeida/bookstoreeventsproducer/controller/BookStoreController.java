package com.gaalmeida.bookstoreeventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaalmeida.bookstoreeventsproducer.builder.BookStoreBuilder;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEvent;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStoreEventType;
import com.gaalmeida.bookstoreeventsproducer.resource.BookStoreResource;
import com.gaalmeida.bookstoreeventsproducer.service.BookStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
public class BookStoreController {

    private BookStoreService bookStoreService;
    private BookStoreBuilder bookStoreBuilder;

    public BookStoreController(BookStoreService bookStoreService, BookStoreBuilder bookStoreBuilder) {
        this.bookStoreService = bookStoreService;
        this.bookStoreBuilder = bookStoreBuilder;
    }

    @PostMapping("/v1/bookstore")
    public ResponseEntity<BookStoreResource> postBookStoreEvent(@RequestBody @Valid BookStoreResource bookStoreResource) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        bookStoreBuilder.entityToResource(
                                bookStoreService.create(
                                        bookStoreBuilder.resourceToEntity(bookStoreResource))
                        )
                );
    }

    @PutMapping("/v1/bookstore-events")
    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid BookStoreEvent bookStoreEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        if (bookStoreEvent.getBookStoreEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eai passa por favor o EventId mano");
        }

        bookStoreEvent.setBookStoreEventType(BookStoreEventType.UPDATE);
        bookStoreEventProducer.senBookStoreEvent_Approach2(bookStoreEvent);
        return ResponseEntity.status(HttpStatus.OK).body(bookStoreEvent);
    }
}
