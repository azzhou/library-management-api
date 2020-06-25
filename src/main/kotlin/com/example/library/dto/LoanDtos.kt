package com.example.library.dto

import com.example.library.constants.*
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank


// DTOs used to return data in http response body

@Schema(description = LOAN_DESCRIPTION)
@Relation(collectionRelation = "loans")
open class LoanResponse (

    var user: UserSimpleResponse,

    var bookCopy: BookCopySimpleResponse,

    @Schema(description = LOAN_DATE_BORROWED_DESCRIPTION, example = DATE_EXAMPLE)
    var dateBorrowed: LocalDateTime,

    @Schema(description = LOAN_DATE_DUE_DESCRIPTION, example = DATE_EXAMPLE)
    var dateDue: LocalDateTime,

    @Schema(description = LOAN_RENEWALS_DESCRIPTION, example = LOAN_RENEWALS_EXAMPLE)
    var numRenewals: Int,

    @Schema(description = LOAN_ID_DESCRIPTION, example = LOAN_ID_EXAMPLE)
    var loanId: Long
) : RepresentationModel<LoanResponse>()


// DTOs used for collecting info from client

@Schema(description = LOAN_REGISTRATION_DESCRIPTION)
data class LoanRegistration (
    @Schema(description = USER_ID_DESCRIPTION, example = USER_ID_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Min(value = 0, message = USER_ID_VALIDATION_MESSAGE)
    var userId: Long = -1,

    @Schema(description = BOOK_COPY_BARCODE_DESCRIPTION, example = BOOK_COPY_BARCODE_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Min(value = 0, message = BOOK_COPY_BARCODE_VALIDATION_MESSAGE)
    var barcode: Long = -1
)