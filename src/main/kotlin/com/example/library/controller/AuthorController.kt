package com.example.library.controller

import com.example.library.dto.AuthorRegistration
import com.example.library.dto.AuthorResponse
import com.example.library.assembler.AuthorResponseAssembler
import com.example.library.assembler.AuthorSimpleResponseAssembler
import com.example.library.constants.*
import com.example.library.dto.AuthorSimpleResponse
import com.example.library.entity.Author
import com.example.library.service.BookCatalogService
import com.example.library.service.BookManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.validation.Valid


@RestController
@RequestMapping("/authors")
class AuthorController @Autowired constructor(
    private val bookManagementService: BookManagementService,
    private val bookCatalogService: BookCatalogService,
    private val pagedResourcesAssembler: PagedResourcesAssembler<Author>,
    private val authorResponseAssembler: AuthorResponseAssembler,
    private val authorSimpleResponseAssembler: AuthorSimpleResponseAssembler
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for authors")
    @PageableAsQueryParam
    fun getAuthorsWithName(
        @Parameter(description = FILTER_AUTHORS_BY_FIRST_NAME_MESSAGE, example = AUTHOR_FIRST_NAME_EXAMPLE)
        @RequestParam(name = "first_name", required = false)
        firstName: String? = null,

        @Parameter(description = FILTER_AUTHORS_BY_LAST_NAME_MESSAGE, example = AUTHOR_LAST_NAME_EXAMPLE)
        @RequestParam(name = "last_name", required = false)
        lastName: String? = null,

        @Parameter(hidden = true)
        pageable: Pageable? = null
    ): PagedModel<AuthorSimpleResponse> {
        // parameters received should have been encoded to be URL-safe, so need to decode before using
        val firstNameDecoded = firstName?.let { URLDecoder.decode(firstName, StandardCharsets.UTF_8.toString()) }
        val lastNameDecoded = lastName?.let { URLDecoder.decode(lastName, StandardCharsets.UTF_8.toString()) }

        val authors = bookCatalogService.getAuthorsWithName(firstNameDecoded, lastNameDecoded, pageable!!)
        return pagedResourcesAssembler.toModel(authors, authorSimpleResponseAssembler)
    }

    @GetMapping("/{author_id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for specific author by id number")
    fun getAuthorWithId(
        @Parameter(description = AUTHOR_ID_DESCRIPTION, example = AUTHOR_ID_EXAMPLE)
        @PathVariable(name = "author_id")
        authorId: Long
    ): AuthorResponse {
        val author = bookCatalogService.getAuthorWithId(authorId)
        return authorResponseAssembler.toModel(author)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Add new author to the database",
        description = AUTHENTICATION_REQUIRED_MESSAGE + LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    @ResponseStatus(HttpStatus.CREATED)
    fun addAuthor(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = AUTHOR_REGISTRATION_DESCRIPTION + ". " +
            REQUEST_BODY_VALIDATION_MESSAGE)
        @Valid @RequestBody
        authorRegistration: AuthorRegistration
    ): AuthorResponse {
        val author = bookManagementService.addAuthor(authorRegistration)
        return authorResponseAssembler.toModel(author)
    }

}