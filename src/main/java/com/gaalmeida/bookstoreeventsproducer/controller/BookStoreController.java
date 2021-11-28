package com.gaalmeida.bookstoreeventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaalmeida.bookstoreeventsproducer.builder.BookStoreBuilder;
import com.gaalmeida.bookstoreeventsproducer.controller.resource.BookStoreResource;
import com.gaalmeida.bookstoreeventsproducer.service.contract.BookStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class BookStoreController {

    private final BookStoreService bookStoreService;
    private final BookStoreBuilder bookStoreBuilder;

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

    @PutMapping("/v1/bookstore")
    public ResponseEntity<BookStoreResource> putLibraryEvent(@RequestBody @Valid BookStoreResource bookStoreResource) throws JsonProcessingException {
        var bookStore = bookStoreBuilder.resourceToEntity(bookStoreResource);
        var bookStoreUpdate = bookStoreService.update(bookStore);
        var bookStoreResourceUpdate = bookStoreBuilder.entityToResource(bookStoreUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(bookStoreResourceUpdate);
    }
}
