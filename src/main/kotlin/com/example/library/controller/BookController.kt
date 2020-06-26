package com.example.library.controller

import com.example.library.assembler.BookResponseAssembler
import com.example.library.assembler.BookSimpleResponseAssembler
import com.example.library.constants.*
import com.example.library.dto.*
import com.example.library.entity.Book
import com.example.library.service.BookCatalogService
import com.example.library.service.BookManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@RestController
@RequestMapping("/books")
class BookController @Autowired constructor(
    private val bookManagementService: BookManagementService,
    private val bookCatalogService: BookCatalogService,
    private val pagedResourcesAssembler: PagedResourcesAssembler<Book>,
    private val bookResponseAssembler: BookResponseAssembler,
    private val bookSimpleResponseAssembler: BookSimpleResponseAssembler
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for books")
    fun getAllBooks(
        @Parameter(hidden = true) pageable: Pageable? = null
    ): PagedModel<BookSimpleResponse> {
        val books = bookCatalogService.getAllBooks(pageable!!)
        return pagedResourcesAssembler.toModel(books, bookSimpleResponseAssembler)
    }

    // Spring boot has difficulty with overloading, so need to include "title_contains" despite it not being
    // part of this method because otherwise a request including both parameters causes issues
    @GetMapping(params = ["author_id", "title_contains"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBooksWithAuthorId(
        @Parameter(description = FILTER_BOOKS_BY_AUTHOR_ID_MESSAGE, example = AUTHOR_ID_EXAMPLE)
        @RequestParam(name = "author_id", required = false)
        authorId: Long,

        @Parameter(hidden = true)
        pageable: Pageable? = null
    ): PagedModel<BookSimpleResponse> {
        val books = bookCatalogService.getBooksWithAuthorId(authorId, pageable!!)
        return pagedResourcesAssembler.toModel(books, bookSimpleResponseAssembler)
    }

    @GetMapping(params = ["title_contains"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PageableObjectAsParams
    fun getBooksWithTitleContaining(
        @Parameter(description = FILTER_BOOKS_BY_TITLE_MESSAGE, example = BOOK_TITLE_EXAMPLE)
        @RequestParam(name = "title_contains", required = false)
        title: String,

        @Parameter(hidden = true)
        pageable: Pageable? = null
    ): PagedModel<BookSimpleResponse> {
        // parameters received should have been encoded to be URL-safe, so need to decode before using
        val titleDecoded = URLDecoder.decode(title, StandardCharsets.UTF_8.toString())

        val books = bookCatalogService.getBooksWithTitleContaining(titleDecoded, pageable!!)
        return pagedResourcesAssembler.toModel(books, bookSimpleResponseAssembler)
    }

    @GetMapping("/{isbn}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for a specific book by ISBN-13")
    fun getBookWithIsbn(
        @Parameter(description = BOOK_ISBN_DESCRIPTION, example = BOOK_ISBN_EXAMPLE)
        @PathVariable
        isbn: String
    ): BookResponse {
        val book = bookCatalogService.getBookWithIsbn(isbn)
        return bookResponseAssembler.toModel(book)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Add new book to the database",
        description = AUTHENTICATION_REQUIRED_MESSAGE + LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = BOOK_REGISTRATION_DESCRIPTION + ". " +
            REQUEST_BODY_VALIDATION_MESSAGE)
        @Valid @RequestBody
        bookRegistration: BookRegistration): BookResponse {
        val book = bookManagementService.addBook(bookRegistration)
        return bookResponseAssembler.toModel(book)
    }
}