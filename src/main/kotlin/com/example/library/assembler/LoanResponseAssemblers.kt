package com.example.library.assembler

import com.example.library.controller.LoanController
import com.example.library.dto.LoanResponse
import com.example.library.entity.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component


@Component
class LoanResponseAssembler @Autowired constructor(
    // other assemblers used for nested entities; only use simple versions to avoid circular dependencies
    private val bookCopySimpleResponseAssembler: BookCopySimpleResponseAssembler,
    private val userSimpleResponseAssembler: UserSimpleResponseAssembler
) : RepresentationModelAssemblerSupport<Loan, LoanResponse>(
    LoanController::class.java,
    LoanResponse::class.java
) {

    override fun toModel(entity: Loan): LoanResponse {
        val loanResponse = LoanResponse(
            user = userSimpleResponseAssembler.toModel(entity.user),
            bookCopy = bookCopySimpleResponseAssembler.toModel(entity.bookCopy),
            dateBorrowed = entity.dateBorrowed,
            dateDue = entity.dateDue,
            numRenewals = entity.numRenewals,
            loanId = entity.id
        )

        return loanResponse
            .add(linkTo(methodOn(LoanController::class.java).getLoanWithId(entity.id)).withSelfRel())
    }
}