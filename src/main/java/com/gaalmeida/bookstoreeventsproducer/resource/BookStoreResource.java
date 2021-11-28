package com.gaalmeida.bookstoreeventsproducer.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookStoreResource {

    @NotNull
    private Integer id;

    @NotNull
    private String bookAuthor;
}
