package com.example.library.dto

import com.example.library.constants.*
import com.example.library.entity.Author
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


// DTOs used to return data in http response body

@Schema(description = AUTHOR_DESCRIPTION)
@Relation(collectionRelation = "authors")
open class AuthorResponse (
    @Schema(description = AUTHOR_FIRST_NAME_DESCRIPTION, example = AUTHOR_FIRST_NAME_EXAMPLE)
    var firstName: String,

    @Schema(description = AUTHOR_LAST_NAME_DESCRIPTION, example = AUTHOR_LAST_NAME_EXAMPLE)
    var lastName: String,

    @Schema(description = AUTHOR_BOOKS_DESCRIPTION)
    var books: List<BookSimpleResponse>,

    @Schema(description = AUTHOR_ID_DESCRIPTION, example = AUTHOR_ID_EXAMPLE)
    var authorId: Long
) : RepresentationModel<AuthorResponse>()

@Schema(description = AUTHOR_DESCRIPTION)
@Relation(collectionRelation = "authors")
open class AuthorSimpleResponse (
    @Schema(description = AUTHOR_FIRST_NAME_DESCRIPTION, example = AUTHOR_FIRST_NAME_EXAMPLE)
    var firstName: String,

    @Schema(description = AUTHOR_LAST_NAME_DESCRIPTION, example = AUTHOR_LAST_NAME_EXAMPLE)
    var lastName: String,

    @Schema(description = AUTHOR_ID_DESCRIPTION, example = AUTHOR_ID_EXAMPLE)
    var authorId: Long
) : RepresentationModel<AuthorSimpleResponse>()


// DTOs used for collecting info from client

@Schema(description = AUTHOR_REGISTRATION_DESCRIPTION)
data class AuthorRegistration (
    @Schema(description = AUTHOR_FIRST_NAME_DESCRIPTION, example = AUTHOR_FIRST_NAME_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var firstName: String = "",

    @Schema(description = AUTHOR_LAST_NAME_DESCRIPTION, example = AUTHOR_LAST_NAME_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var lastName: String = ""
)


// Extension functions to avoid including logic directly within DTOs, as they are only meant to contain data
// Convert from input DTO to Entity

fun AuthorRegistration.asEntity(): Author {
    return Author(firstName, lastName)
}

