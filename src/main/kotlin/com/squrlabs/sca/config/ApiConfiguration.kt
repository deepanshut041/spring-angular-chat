package com.squrlabs.sca.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ApiConfiguration {
    @Bean
    fun customOpenAPI(@Value("\${springdoc.version}") appVersion: String?): OpenAPI? {
        return OpenAPI()
                .components(Components().addSecuritySchemes("basicScheme",
                        SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                .info(Info().title("SpringShop API").version(appVersion)
                        .license(License().name("Apache 2.0").url("http://springdoc.org")))
    }
}