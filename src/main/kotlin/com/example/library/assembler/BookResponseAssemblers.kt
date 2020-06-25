package com.example.library.assembler

import com.example.library.controller.BookController
import com.example.library.controller.BookCopyController
import com.example.library.dto.BookResponse
import com.example.library.dto.BookSimpleResponse
import com.example.library.entity.Book
import com.example.library.entity.BookStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component


@Component
class BookResponseAssembler @Autowired constructor(
    // other assemblers used for nested entities; only use simple versions to avoid circular dependencies
    private val authorSimpleResponseAssembler: AuthorSimpleResponseAssembler,
    private val bookCopySimpleResponseAssembler: BookCopySimpleResponseAssembler
) : RepresentationModelAssemblerSupport<Book, BookResponse>(
    BookController::class.java,
    BookResponse::class.java
) {

    override fun toModel(entity: Book): BookResponse {
        val bookResponse = BookResponse(
            title = entity.title,
            authors = entity.authors.map { authorSimpleResponseAssembler.toModel(it) },
            isbn = entity.isbn,
            publisher = entity.publisher,
            language = entity.language,
            copiesAvailable = entity.bookCopies.count { it.status == BookStatus.AVAILABLE },
            copiesTotal = entity.bookCopies.size,
            bookCopies = entity.bookCopies.map { bookCopySimpleResponseAssembler.toModel(it) }
        )

        return bookResponse
            .add(linkTo(methodOn(BookController::class.java).getBookWithIsbn(entity.isbn)).withSelfRel())
            .add(linkTo(methodOn(BookCopyController::class.java).getBookCopiesWithIsbn(entity.isbn)).withRel("copies"))
    }
}


@Component
class BookSimpleResponseAssembler : RepresentationModelAssemblerSupport<Book, BookSimpleResponse>(
    BookController::class.java,
    BookSimpleResponse::class.java
) {

    override fun toModel(entity: Book): BookSimpleResponse {
        return BookSimpleResponse(entity.title, entity.isbn)
            // Add self link and link to copies of this book
            .add(linkTo(methodOn(BookController::class.java).getBookWithIsbn(entity.isbn)).withSelfRel())
    }
}