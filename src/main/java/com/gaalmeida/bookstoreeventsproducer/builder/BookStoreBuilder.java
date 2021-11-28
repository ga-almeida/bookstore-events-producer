package com.gaalmeida.bookstoreeventsproducer.builder;

import com.gaalmeida.bookstoreeventsproducer.domain.BookStore;
import com.gaalmeida.bookstoreeventsproducer.controller.resource.BookStoreResource;

public class BookStoreBuilder {

    public BookStore resourceToEntity(BookStoreResource bookStoreResource) {
        return BookStore
                .builder()
                .id(bookStoreResource.getId())
                .bookAuthor(bookStoreResource.getBookAuthor())
                .build();
    }

    public BookStoreResource entityToResource(BookStore bookStore) {
        return BookStoreResource
                .builder()
                .id(bookStore.getId())
                .bookAuthor(bookStore.getBookAuthor())
                .build();
    }
}
