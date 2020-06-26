package com.example.library.controller

import com.example.library.constants.PAGINATION_PAGE_MESSAGE
import com.example.library.constants.PAGINATION_SIZE_MESSAGE
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema


// Custom version of springdoc's @PageableAsQueryParam annotation to be able to change names and descriptions
// Excluding sort parameter since it isn't well-supported in the application currently

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Parameters(value = [
    Parameter(`in` = ParameterIn.QUERY,
        description = PAGINATION_PAGE_MESSAGE,
        name = "\${spring.data.web.pageable.page-parameter}",
        content = [Content(schema = Schema(type = "integer"))]),
    Parameter(`in` = ParameterIn.QUERY,
        description = PAGINATION_SIZE_MESSAGE,
        name = "\${spring.data.web.pageable.size-parameter}",
        content = [Content(schema = Schema(type = "integer"))])
])
annotation class PageableObjectAsParams