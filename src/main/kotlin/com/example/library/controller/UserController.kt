package com.example.library.controller

import com.example.library.assembler.UserAuthenticationResponseAssembler
import com.example.library.assembler.UserResponseAssembler
import com.example.library.assembler.UserSimpleResponseAssembler
import com.example.library.constants.*
import com.example.library.dto.*
import com.example.library.entity.User
import com.example.library.entity.UserPermission
import com.example.library.service.UserService
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
import javax.validation.Valid


@RestController
@RequestMapping("/users")
class UserController @Autowired constructor(
    private val userService: UserService,
    private val pagedResourcesAssembler: PagedResourcesAssembler<User>,
    private val userResponseAssembler: UserResponseAssembler,
    private val userAuthenticationResponseAssembler: UserAuthenticationResponseAssembler,
    private val userSimpleResponseAssembler: UserSimpleResponseAssembler
) {

    @GetMapping
    @Operation(summary = "Search for loans of book copies to users",
        description = AUTHENTICATION_REQUIRED_MESSAGE + LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    @PageableAsQueryParam
    fun getAllUsers(
        @Parameter(hidden = true) pageable: Pageable? = null
    ): PagedModel<UserSimpleResponse> {
        val users = userService.getAllUsers(pageable!!)
        return pagedResourcesAssembler.toModel(users, userSimpleResponseAssembler)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Register a new user account",
        description = "$REDIRECT_TO_LOGIN_MESSAGE $AUTHORIZATION_RESPONSE_MESSAGE")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = USER_REGISTRATION_DESCRIPTION + ". " +
            REQUEST_BODY_VALIDATION_MESSAGE)
        @Valid @RequestBody
        userRegistration: UserRegistration
    ): UserAuthenticationResponse {
        val userAuth = userService.registerNewUser(userRegistration)
        return userAuthenticationResponseAssembler.toModel(userAuth)
    }

    @GetMapping("/{user_id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Search for user by identification number",
        description = AUTHENTICATION_REQUIRED_MESSAGE + SELF_OR_LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    fun getUserWithId(
        @Parameter(description = USER_ID_DESCRIPTION, example = USER_ID_EXAMPLE)
        @PathVariable(name = "user_id")
        userId: Long
    ): UserResponse {
        val user = userService.getUserWithId(userId)
        return userResponseAssembler.toModel(user)
    }

    @PatchMapping("/{user_id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Update user account details",
        description = AUTHENTICATION_REQUIRED_MESSAGE + SELF_OR_LIBRARIAN_REQUIRED_MESSAGE + AUTHENTICATION_STEPS_MESSAGE,
        security = [SecurityRequirement(name = "bearer-jwt")])
    fun updateUserInfo(
        @Parameter(description = USER_ID_DESCRIPTION, example = USER_ID_EXAMPLE)
        @PathVariable(name = "user_id")
        userId: Long,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = USER_UPDATE_DESCRIPTION + ". " +
            REQUEST_BODY_VALIDATION_MESSAGE)
        @Valid @RequestBody
        userUpdate: UserUpdate
    ): UserResponse {
        var user = userService.getUserWithId(userId)

        // Permission update must come first because it has the stricter role requirement
        // PATCH should not result in a partial success
        userUpdate.permission?.let {
            user = userService.updateUserPermission(user, UserPermission.valueOf(it))
        }

        // Update the rest of the account information
        user = userService.updateAccountInfo(user, userUpdate)

        return userResponseAssembler.toModel(user)
    }

    @PostMapping("/token", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Sign in (i.e. obtain authentication token)",
        description = "$REDIRECT_TO_SIGNUP_MESSAGE $AUTHORIZATION_RESPONSE_MESSAGE")
    fun login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = USER_LOGIN_DESCRIPTION + ". " +
            REQUEST_BODY_VALIDATION_MESSAGE)
        @Valid @RequestBody
        userLogin: UserLogin
    ): UserAuthenticationResponse {
        val userAuth = userService.login(userLogin.email, userLogin.password)
        return userAuthenticationResponseAssembler.toModel(userAuth)
    }
}