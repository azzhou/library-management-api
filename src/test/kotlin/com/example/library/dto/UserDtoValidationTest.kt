package com.example.library.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

class UserDtoValidationTest() {
    private lateinit var validatorFactory: ValidatorFactory
    private lateinit var validator: Validator

    @BeforeAll
    fun setup() {
        validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.validator
    }

    @AfterAll
    fun teardown() {
        validatorFactory.close()
    }

    @Test
    fun `when UserLoginDto is valid then no validation errors`() {
        val dto = UserLogin("email@test.com", "password")
        assertThat(validator.validate(dto).isEmpty()).isEqualTo(true)
    }

    @Test
    fun `when UserLoginDto has empty email then validator has email violation`() {
        val dto = UserLogin("", "password")
        val violations = validator.validate(dto)
        assertThat(violations.size).isEqualTo(1)
        assertThat(violations.any { it.propertyPath.toString() == "email" }).isEqualTo(true)
    }

    @Test
    fun `when UserRegisterDto is valid then no validation errors`() {
        val dto = UserRegistration("email@test.com", "first", "last","password")
        assertThat(validator.validate(dto).isEmpty()).isEqualTo(true)
    }

    @Test
    fun `when UserRegisterDto has invalid email then validator has email violation`() {
        val dto = UserRegistration("bad email", "first", "last","password")
        val violations = validator.validate(dto)
        assertThat(violations.size).isEqualTo(1)
        assertThat(violations.any { it.propertyPath.toString() == "email" }).isEqualTo(true)
    }

    @Test
    fun `when UserRegisterDto has invalid firstName size then validator has firstName violation`() {
        // Below minimum length
        val dto = UserRegistration("test@email.com", "", "last","password")
        var violations = validator.validate(dto)
        assertThat(violations.size).isEqualTo(1)
        assertThat(violations.any { it.propertyPath.toString() == "firstName" }).isEqualTo(true)

        // Above maximum length
        dto.firstName = "a".repeat(31)
        violations = validator.validate(dto)
        assertThat(violations.size).isEqualTo(1)
        assertThat(violations.any { it.propertyPath.toString() == "firstName" }).isEqualTo(true)
    }

    @Test
    fun `when UserUpdateDto is valid then no validation errors`() {
        // All fields included
        var dto = UserUpdate(email = "email@test.com", firstName = "first", lastName = "last",
            password = "password", permission = "LIBRARIAN")
        assertThat(validator.validate(dto).isEmpty()).isEqualTo(true)

        // Exclude some optional fields
        dto = UserUpdate(firstName = "first", permission = "ADMIN")
        assertThat(validator.validate(dto).isEmpty()).isEqualTo(true)
    }

    @Test
    fun `when UserUpdateDto permission field fails regex then validator has permission violation`() {
        val dto = UserUpdate(permission = "bad permission")
        val violations = validator.validate(dto)
        assertThat(violations.size).isEqualTo(1)
        assertThat(violations.any { it.propertyPath.toString() == "permission" }).isEqualTo(true)
    }

}