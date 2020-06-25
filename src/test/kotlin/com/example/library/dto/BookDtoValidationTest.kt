package com.example.library.dto

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory


class BookDtoValidationTest() {
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
    fun `when BookRegisterDto is valid then no validation errors`() {
//        val dto = BookRegisterDto("title", )
//        Assertions.assertThat(validator.validate(dto).isEmpty()).isEqualTo(true)
    }
}