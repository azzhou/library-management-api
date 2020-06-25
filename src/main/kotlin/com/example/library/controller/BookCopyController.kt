package com.example.library.controller

import com.example.library.assembler.BookCopyResponseAssembler
import com.example.library.assembler.BookCopySimpleResponseAssembler
import com.example.library.constants.*
import com.example.library.dto.BookCopyResponse
import com.example.library.dto.BookCopyRegistration
import com.example.library.dto.BookCopySimpleResponse
import com.example.library.entity.BookCopy
import com.example.library.service.BookCatalogService
import com.example.library.service.BookManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/book-copies")
class BookCopyController @Autowired constructor(
    private val bookManagementService: BookManagementService,
    private val bookCatalogService: BookCatalogService,
    private val pagedResourcesAssembler: PagedResourcesAssembler<BookCopy>,
    private val bookCopyResponseAssembler: BookCopyResponseAssembler,
    private val bookCopySimpleResponseAssembler: BookCopySimpleResponseAssembler
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for book copies")
    @PageableAsQueryParam
    fun getAllBookCopies(
        @Parameter(hidden = true) pageable: Pageable? = null
    ): PagedModel<BookCopySimpleResponse> {
        val bookCopies = bookCatalogService.getAllBookCopies(pageable!!)
        return pagedResourcesAssembler.toModel(bookCopies, bookCopySimpleResponseAssembler)
    }

    @GetMapping(params = ["isbn"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PageableAsQueryParam
    fun getBookCopiesWithIsbn(
        @Parameter(description = FILTER_BOOK_COPIES_BY_ISBN_MESSAGE, example = BOOK_ISBN_EXAMPLE)
        @RequestParam(required = false)
        isbn: String,

        @Parameter(hidden = true)
        pageable: Pageable? = null
    ): PagedModel<BookCopySimpleResponse> {
        val bookCopies = bookCatalogService.getBookCopiesWithIsbn(isbn, pageable!!)
        val response = pagedResourcesAssembler.toModel(bookCopies, bookCopySimpleResponseAssembler)

        // add additional links (self link created automatically by assembler)
        // this returns a CollectionModel, so don't replace the original variable, which needs to be PagedModel
        response.add(linkTo(methodOn(BookController::class.java).getBookWithIsbn(isbn)).withRel("book"))

        return response
    }

    @GetMapping("/{barcode}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for book copy by barcode")
    fun getBookCopyWithBarcode(
        @Parameter(description = BOOK_COPY_BARCODE_DESCRIPTION, example = BOOK_COPY_BARCODE_EXAMPLE)
        @PathVariable
        barcode: Long
    ): BookCopyResponse {
        val bookCopy = bookCatalogService.getBookCopyWithBarcode(barcode)
        return bookCopyResponseAssembler.toModel(bookCopy)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Add new book copy to the database")
    @ResponseStatus(HttpStatus.CREATED)
    fun addBookCopy(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = BOOK_COPY_REGISTRATION_DESCRIPTION + ". " +
            REQUEST_BODY_VALIDATION_MESSAGE)
        @Valid @RequestBody
        bookCopyRegistration: BookCopyRegistration):
        BookCopyResponse {
        val bookCopy = bookManagementService.addBookCopy(bookCopyRegistration)
        return bookCopyResponseAssembler.toModel(bookCopy)
    }
}