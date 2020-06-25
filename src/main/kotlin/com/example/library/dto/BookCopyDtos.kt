package com.example.library.dto

import com.example.library.constants.*
import com.example.library.entity.BookFormat
import com.example.library.entity.BookStatus
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


// DTOs used to return data in http response body

@Schema(description = BOOK_COPY_DESCRIPTION)
@Relation(collectionRelation = "copies")
open class BookCopyResponse (
    @Schema(description = BOOK_DESCRIPTION)
    var book: BookSimpleResponse,

    @Schema(description = BOOK_COPY_FORMAT_DESCRIPTION, example = BOOK_COPY_FORMAT_EXAMPLE)
    var format: BookFormat,

    @Schema(description = BOOK_COPY_STATUS_DESCRIPTION, example = BOOK_COPY_STATUS_EXAMPLE)
    var status: BookStatus,

    @Schema(description = LOAN_DATE_BORROWED_DESCRIPTION, example = DATE_EXAMPLE)
    var dateBorrowed: LocalDateTime?,

    @Schema(description = LOAN_DATE_DUE_DESCRIPTION, example = DATE_EXAMPLE)
    var dateDue: LocalDateTime?,

    @Schema(description = BOOK_COPY_DATE_ADDED_DESCRIPTION, example = DATE_EXAMPLE)
    var dateAdded: LocalDateTime,

    @Schema(description = BOOK_COPY_BARCODE_DESCRIPTION, example = BOOK_COPY_BARCODE_EXAMPLE)
    var barcode: Long
) : RepresentationModel<BookCopyResponse>()

@Schema(description = BOOK_COPY_DESCRIPTION)
@Relation(collectionRelation = "copies")
open class BookCopySimpleResponse (
    @Schema(description = BOOK_TITLE_DESCRIPTION, example = BOOK_TITLE_EXAMPLE)
    var title: String,

    @Schema(description = BOOK_COPY_FORMAT_DESCRIPTION, example = BOOK_COPY_FORMAT_EXAMPLE)
    var format: BookFormat,

    @Schema(description = BOOK_COPY_STATUS_DESCRIPTION, example = BOOK_COPY_STATUS_EXAMPLE)
    var status: BookStatus,

    @Schema(description = BOOK_COPY_BARCODE_DESCRIPTION, example = "5")
    var barcode: Long
) : RepresentationModel<BookCopySimpleResponse>()


// DTOs used for collecting info from client

@Schema(description = BOOK_COPY_REGISTRATION_DESCRIPTION)
data class BookCopyRegistration (

    // Todo: Change from regex validation to custom bean validation to avoid hardcoded values
    @Schema(description = BOOK_ISBN_DESCRIPTION, example = BOOK_ISBN_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Pattern(regexp = "^\\d{13}\$", message = ISBN_VALIDATION_MESSAGE)
    var isbn: String = "",

    // Todo: Change from regex validation to custom bean validation to avoid hardcoded values
    @Schema(description = "$BOOK_COPY_FORMAT_DESCRIPTION. $BOOK_COPY_FORMAT_VALIDATION_MESSAGE",
        example = BOOK_COPY_FORMAT_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Pattern(regexp = "^(HARDCOVER|PAPERBACK|AUDIO|EBOOK)\$", message = BOOK_COPY_FORMAT_VALIDATION_MESSAGE)
    // String instead of enum to be able to validate input
    var format: String = "",

    // Todo: Change from regex validation to custom bean validation to avoid hardcoded values
    @Schema(description = "$BOOK_COPY_STATUS_DESCRIPTION. $BOOK_COPY_STATUS_VALIDATION_MESSAGE",
        example = BOOK_COPY_STATUS_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Pattern(regexp = "^(AVAILABLE|BORROWED|PROCESSING)\$", message = BOOK_COPY_STATUS_VALIDATION_MESSAGE)
    // String instead of enum to be able to validate input
    var status: String = ""
)