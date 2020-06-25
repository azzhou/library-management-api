package com.example.library.assembler

import com.example.library.controller.AuthorController
import com.example.library.controller.BookController
import com.example.library.dto.AuthorResponse
import com.example.library.dto.AuthorSimpleResponse
import com.example.library.entity.Author
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component


@Component
class AuthorResponseAssembler @Autowired constructor(
    // other assemblers used for nested entities; only use simple versions to avoid circular dependencies
    private val bookSimpleResponseAssembler: BookSimpleResponseAssembler
) : RepresentationModelAssemblerSupport<Author, AuthorResponse>(
    AuthorController::class.java,
    AuthorResponse::class.java
) {

    override fun toModel(entity: Author): AuthorResponse {
        // convert list of Book entities to list of BookSimpleResponse DTOs
        val bookList = entity.books.map { bookSimpleResponseAssembler.toModel(it) }

        return AuthorResponse(entity.firstName, entity.lastName, bookList, entity.id)
            // Add self link and link to books written by the author
            .add(linkTo(methodOn(AuthorController::class.java).getAuthorWithId(entity.id)).withSelfRel())
            .add(linkTo(methodOn(BookController::class.java)
                .getBooksWithAuthorId(entity.id)).withRel("books"))
    }
}


@Component
class AuthorSimpleResponseAssembler : RepresentationModelAssemblerSupport<Author, AuthorSimpleResponse>(
    AuthorController::class.java,
    AuthorSimpleResponse::class.java
) {

    override fun toModel(entity: Author): AuthorSimpleResponse {
        return AuthorSimpleResponse(entity.firstName, entity.lastName, entity.id)
            .add(linkTo(methodOn(AuthorController::class.java).getAuthorWithId(entity.id)).withSelfRel())
    }
}