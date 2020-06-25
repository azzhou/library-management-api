package com.example.library.assembler

import com.example.library.controller.LoanController
import com.example.library.controller.UserController
import com.example.library.dto.UserAuthenticationResponse
import com.example.library.dto.UserResponse
import com.example.library.dto.UserSimpleResponse
import com.example.library.entity.User
import com.example.library.entity.UserAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component


@Component
class UserResponseAssembler : RepresentationModelAssemblerSupport<User, UserResponse>(
    UserController::class.java,
    UserResponse::class.java
) {

    override fun toModel(entity: User): UserResponse {
        val userResponse = UserResponse(
            email = entity.email,
            firstName = entity.firstName,
            lastName = entity.lastName,
            permission = entity.permission,
            numBooksBorrowed = entity.loans.size,
            fineBalanceCents = entity.fineBalanceCents,
            userId = entity.id
        )

        return userResponse
            .add(linkTo(methodOn(UserController::class.java).getUserWithId(entity.id)).withSelfRel())
            .add(linkTo(methodOn(LoanController::class.java).getLoansWithUserId(entity.id)).withRel("loans"))
    }
}


@Component
class UserSimpleResponseAssembler : RepresentationModelAssemblerSupport<User, UserSimpleResponse>(
    UserController::class.java,
    UserSimpleResponse::class.java
) {

    override fun toModel(entity: User): UserSimpleResponse {
        return UserSimpleResponse(entity.email, entity.id)
            .add(linkTo(methodOn(UserController::class.java).getUserWithId(entity.id)).withSelfRel())
    }
}


@Component
class UserAuthenticationResponseAssembler @Autowired constructor(
    private val userResponseAssembler: UserResponseAssembler
): RepresentationModelAssemblerSupport<UserAuthentication, UserAuthenticationResponse>(
    UserController::class.java,
    UserAuthenticationResponse::class.java
) {
    override fun toModel(entity: UserAuthentication): UserAuthenticationResponse {
        return UserAuthenticationResponse(userResponseAssembler.toModel(entity.user), entity.authentication)
        // no links since this response comes from a POST request
    }
}