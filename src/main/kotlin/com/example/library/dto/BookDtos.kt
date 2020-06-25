package com.example.library.dto

import com.example.library.constants.*
import com.example.library.entity.Book
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import javax.validation.constraints.*


// DTOs used to return data in http response body

@Schema(description = BOOK_DESCRIPTION)
@Relation(collectionRelation = "books")
open class BookResponse (
    @Schema(description = BOOK_TITLE_DESCRIPTION, example = BOOK_TITLE_EXAMPLE)
    var title: String,

    var authors: List<AuthorSimpleResponse>,

    @Schema(description = BOOK_ISBN_DESCRIPTION, example = BOOK_ISBN_EXAMPLE)
    var isbn: String,

    @Schema(description = BOOK_PUBLISHER_DESCRIPTION, example = BOOK_PUBLISHER_EXAMPLE)
    var publisher: String,

    @Schema(description = BOOK_LANGUAGE_DESCRIPTION, example = BOOK_LANGUAGE_EXAMPLE)
    var language: String,

    @Schema(description = BOOK_AVAILABLE_COPIES_DESCRIPTION, example = BOOK_AVAILABLE_COPIES_EXAMPLE)
    var copiesAvailable: Int,

    @Schema(description = BOOK_TOTAL_COPIES_DESCRIPTION, example = BOOK_TOTAL_COPIES_EXAMPLE)
    var copiesTotal: Int,

    var bookCopies: List<BookCopySimpleResponse>
) : RepresentationModel<BookResponse>()

@Schema(description = BOOK_DESCRIPTION)
@Relation(collectionRelation = "books")
open class BookSimpleResponse (
    @Schema(description = BOOK_TITLE_DESCRIPTION, example = BOOK_TITLE_EXAMPLE)
    var title: String,

    @Schema(description = BOOK_ISBN_DESCRIPTION, example = BOOK_ISBN_EXAMPLE)
    var isbn: String
) : RepresentationModel<BookSimpleResponse>()


// DTOs used for collecting info from client

@Schema(description = BOOK_REGISTRATION_DESCRIPTION)
data class BookRegistration (
    @Schema(description = BOOK_TITLE_DESCRIPTION, example = BOOK_TITLE_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var title: String = "",

    @Schema(description = BOOK_AUTHOR_IDS_DESCRIPTION, example = BOOK_AUTHOR_IDS_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:NotEmpty(message = AUTHOR_ID_LIST_VALIDATION_MESSAGE)
    var authorIds: List<Long>,

    // Todo: Change from regex validation to custom bean validation to avoid hardcoded values
    @Schema(description = BOOK_ISBN_DESCRIPTION, example = BOOK_ISBN_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Pattern(regexp = "^\\d{13}\$", message = ISBN_VALIDATION_MESSAGE)
    var isbn: String = "",

    @Schema(description = BOOK_PUBLISHER_DESCRIPTION, example = BOOK_PUBLISHER_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var publisher: String = "",

    @Schema(description = BOOK_LANGUAGE_DESCRIPTION, example = BOOK_LANGUAGE_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var language: String = ""
)


// Extension functions to avoid including logic directly within DTOs, as they are only meant to contain data
// Convert from input DTO to Entity

fun BookRegistration.asEntity(): Book {
    return Book(isbn, title, publisher, language)
}