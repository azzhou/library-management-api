package com.example.library.assembler

import com.example.library.controller.BookCopyController
import com.example.library.controller.LoanController
import com.example.library.dto.BookCopyResponse
import com.example.library.dto.BookCopySimpleResponse
import com.example.library.entity.BookCopy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component


@Component
class BookCopyResponseAssembler @Autowired constructor(
    // other assemblers used for nested entities; only use simple versions to avoid circular dependencies
    private val bookSimpleResponseAssembler: BookSimpleResponseAssembler
) : RepresentationModelAssemblerSupport<BookCopy, BookCopyResponse>(
    BookCopyController::class.java,
    BookCopyResponse::class.java
) {

    override fun toModel(entity: BookCopy): BookCopyResponse {
        val bookCopyResponse = BookCopyResponse(
            book = bookSimpleResponseAssembler.toModel(entity.book),
            format = entity.format,
            status = entity.status,
            dateBorrowed = entity.loan?.dateBorrowed,
            dateDue = entity.loan?.dateDue,
            dateAdded = entity.dateAdded,
            barcode = entity.barcode
        )

        // self link
        bookCopyResponse.add(linkTo(methodOn(BookCopyController::class.java)
            .getBookCopyWithBarcode(entity.barcode)).withSelfRel())

        // add link to loan if a loan exists
        entity.loan?.let {
            bookCopyResponse.add(linkTo(methodOn(LoanController::class.java)
                .getLoanWithId(it.id)).withSelfRel())
        }

        return bookCopyResponse
    }
}


@Component
class BookCopySimpleResponseAssembler : RepresentationModelAssemblerSupport<BookCopy, BookCopySimpleResponse>(
    BookCopyController::class.java,
    BookCopySimpleResponse::class.java
) {

    override fun toModel(entity: BookCopy): BookCopySimpleResponse {
        return BookCopySimpleResponse(entity.book.title, entity.format, entity.status, entity.barcode)
            .add(linkTo(methodOn(BookCopyController::class.java).getBookCopyWithBarcode(entity.barcode)).withSelfRel())
    }
}