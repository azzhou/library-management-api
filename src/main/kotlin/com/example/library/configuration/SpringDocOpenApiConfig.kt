package com.example.library.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SpringDocOpenApiConfig {

    companion object {
        init {
            // enable the support of spring Pageable Type in Swagger ui
            SpringDocUtils.getConfig().replaceWithClass(org.springframework.data.domain.Pageable::class.java,
                org.springdoc.core.converters.models.Pageable::class.java)
        }
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        // add security
        return OpenAPI()
            .components(Components()
                .addSecuritySchemes("bearer-jwt",
                    SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
    }

    // Pass object mapper to swagger so that API documentation shows proper naming convention
    @Bean
    fun modelResolver(objectMapper: ObjectMapper): ModelResolver {
        return ModelResolver(objectMapper)
    }

}