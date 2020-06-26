package com.example.library.controller

import com.example.library.assembler.LoanResponseAssembler
import com.example.library.constants.*
import com.example.library.dto.LoanResponse
import com.example.library.dto.LoanRegistration
import com.example.library.entity.Loan
import com.example.library.service.BookLoanService
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


@RestController
@RequestMapping("/loans")
class LoanController @Autowired constructor(
    private val bookLoanService: BookLoanService,
    private val pagedResourcesAssembler: PagedResourcesAssembler<Loan>,
    private val loanResponseAssembler: LoanResponseAssembler
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for loans of book copies to users",
        description = AUTHENTICATION_REQUIRED_MESSAGE + SELF_OR_LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    fun getAllLoans(
        @Parameter(hidden = true) pageable: Pageable? = null
    ): PagedModel<LoanResponse> {
        val loans = bookLoanService.getAllLoans(pageable!!)
        return pagedResourcesAssembler.toModel(loans, loanResponseAssembler)
    }

    @GetMapping(params = ["user_id"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @PageableObjectAsParams
    fun getLoansWithUserId(
        @Parameter(description = FILTER_LOANS_BY_USER_ID_MESSAGE, example = LOAN_ID_EXAMPLE)
        @RequestParam(name = "user_id", required = false)
        userId: Long,

        @Parameter(hidden = true)
        pageable: Pageable? = null
    ): PagedModel<LoanResponse> {
        val loans = bookLoanService.getLoansWithUserId(userId, pageable!!)
        return pagedResourcesAssembler.toModel(loans, loanResponseAssembler)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Borrow a book copy",
        description = AUTHENTICATION_REQUIRED_MESSAGE + SELF_OR_LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    fun borrowBook(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = LOAN_REGISTRATION_DESCRIPTION + ". " +
            REQUEST_BODY_VALIDATION_MESSAGE)
        @Valid @RequestBody
        loanRegistration: LoanRegistration): LoanResponse {
        val loan = bookLoanService.borrowBook(loanRegistration.userId, loanRegistration.barcode)
        return loanResponseAssembler.toModel(loan)
    }

    @GetMapping("/{loan_id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for a specific loan by id number",
        description = AUTHENTICATION_REQUIRED_MESSAGE + SELF_OR_LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    fun getLoanWithId(
        @Parameter(description = LOAN_ID_DESCRIPTION, example = LOAN_ID_EXAMPLE)
        @PathVariable(name = "loan_id")
        loanId: Long
    ): LoanResponse {
        val loan = bookLoanService.getLoanWithLoanId(loanId)
        return loanResponseAssembler.toModel(loan)
    }

    @DeleteMapping("/{loan_id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Return a book copy to the library",
        description = AUTHENTICATION_REQUIRED_MESSAGE + SELF_OR_LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    fun returnBook(
        @Parameter(description = LOAN_ID_DESCRIPTION, example = LOAN_ID_EXAMPLE)
        @PathVariable(name = "loan_id")
        loanId: Long) {
        val loan = bookLoanService.getLoanWithLoanId(loanId)
        bookLoanService.returnBook(loan)
    }

    @PostMapping("/{loan_id}/renewal", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Renew a borrowed book",
        description = AUTHENTICATION_REQUIRED_MESSAGE + SELF_OR_LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    fun renewBook(
        @Parameter(description = LOAN_ID_DESCRIPTION, example = LOAN_ID_EXAMPLE)
        @PathVariable(name = "loan_id")
        loanId: Long
    ): LoanResponse {
        var loan = bookLoanService.getLoanWithLoanId(loanId)
        loan = bookLoanService.renewBook(loan)
        return loanResponseAssembler.toModel(loan)
    }

}