package com.gaalmeida.bookstoreeventsproducer.service.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaalmeida.bookstoreeventsproducer.domain.BookStore;

public interface BookStoreService {
    public BookStore create(BookStore bookStore) throws JsonProcessingException;
    public BookStore update(BookStore bookStore) throws JsonProcessingException;
}
