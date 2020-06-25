package com.example.library.dto

import com.example.library.constants.*
import com.example.library.entity.UserPermission
import com.example.library.entity.User
import com.example.library.security.AuthenticationDetails
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import javax.validation.constraints.*


// DTOs used to return data in http response body

@Schema(description = USER_DESCRIPTION)
@Relation(collectionRelation = "users")
open class UserResponse (
    @Schema(description = USER_EMAIL_DESCRIPTION, example = USER_EMAIL_EXAMPLE)
    var email: String,

    @Schema(description = USER_FIRST_NAME_DESCRIPTION, example = USER_FIRST_NAME_EXAMPLE)
    var firstName: String,

    @Schema(description = USER_LAST_NAME_DESCRIPTION, example = USER_LAST_NAME_EXAMPLE)
    var lastName: String,

    @Schema(description = USER_PERMISSION_DESCRIPTION, example = USER_PERMISSION_EXAMPLE)
    var permission: UserPermission,

    @Schema(description = USER_BOOKS_BORROWED_DESCRIPTION, example = USER_BOOKS_BORROWED_EXAMPLE)
    var numBooksBorrowed: Int,

    @Schema(description = USER_FINE_DESCRIPTION, example = USER_FINE_EXAMPLE)
    var fineBalanceCents: Long,

    @Schema(description = USER_ID_DESCRIPTION, example = USER_ID_EXAMPLE)
    var userId: Long
) : RepresentationModel<UserResponse>()

@Schema(description = USER_DESCRIPTION)
@Relation(collectionRelation = "users")
open class UserSimpleResponse (
    @Schema(description = USER_EMAIL_DESCRIPTION, example = USER_EMAIL_EXAMPLE)
    var email: String,

    @Schema(description = USER_ID_DESCRIPTION, example = USER_ID_EXAMPLE)
    var userId: Long
) : RepresentationModel<UserSimpleResponse>()

open class UserAuthenticationResponse (
    @Schema(description = AUTHENTICATION_USER_DESCRIPTION)
    var user: UserResponse,

    @Schema(description = AUTHENTICATION_DETAILS_DESCRIPTION)
    var authentication: AuthenticationDetails
) : RepresentationModel<UserAuthenticationResponse>()


// DTOs used for collecting info from client

@Schema(description = USER_LOGIN_DESCRIPTION)
data class UserLogin (
    @Schema(description = USER_EMAIL_DESCRIPTION, example = USER_EMAIL_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    var email: String = "",

    @Schema(description = USER_PASSWORD_DESCRIPTION, example = USER_PASSWORD_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    var password: String = ""
)

@Schema(description = USER_UPDATE_DESCRIPTION)
data class UserUpdate (
    // Allowing for partial updates by using null defaults

    @Schema(description = USER_EMAIL_DESCRIPTION, example = USER_EMAIL_EXAMPLE)
    @field:Email(message = USER_EMAIL_VALIDATION_MESSAGE)
    var email: String? = null,

    @Schema(description = USER_FIRST_NAME_DESCRIPTION, example = USER_FIRST_NAME_EXAMPLE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var firstName: String? = null,

    @Schema(description = USER_LAST_NAME_DESCRIPTION, example = USER_LAST_NAME_EXAMPLE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var lastName: String? = null,

    @Schema(description = USER_PASSWORD_DESCRIPTION,
        example = USER_PASSWORD_EXAMPLE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var password: String? = null,

    // Todo: Change from regex validation to custom bean validation to avoid hardcoded values
    @Schema(description = USER_PERMISSION_DESCRIPTION + USER_PERMISSION_VALIDATION_MESSAGE,
        example = USER_PERMISSION_EXAMPLE)
    @field:Pattern(regexp = "^(MEMBER|LIBRARIAN|ADMIN)\$",
        message = USER_PERMISSION_VALIDATION_MESSAGE)
    // String instead of enum to be able to validate input
    var permission: String? = null
)

@Schema(description = USER_REGISTRATION_DESCRIPTION)
data class UserRegistration (
    @Schema(description = USER_EMAIL_DESCRIPTION, example = USER_EMAIL_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Email(message = USER_EMAIL_VALIDATION_MESSAGE)
    var email: String = "",

    @Schema(description = USER_FIRST_NAME_DESCRIPTION, example = USER_FIRST_NAME_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var firstName: String = "",

    @Schema(description = USER_LAST_NAME_DESCRIPTION, example = USER_LAST_NAME_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var lastName: String = "",

    @Schema(description = USER_PASSWORD_DESCRIPTION,
        example = USER_PASSWORD_EXAMPLE)
    @field:NotBlank(message = REQUIRED_FIELD_VALIDATION_MESSAGE)
    @field:Size(message = SIZE_VALIDATION_MESSAGE, min = 1, max = 30)
    var password: String = ""
)


// Extension functions to avoid including logic directly within DTOs, as they are only meant to contain data
// Convert from input DTO to Entity

fun UserRegistration.asEntity(): User {
    return User(email, firstName, lastName, password)
}
