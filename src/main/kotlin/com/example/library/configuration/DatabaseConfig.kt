package com.example.library.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.net.URI
import javax.sql.DataSource


@Configuration
class DatabaseConfig {
    @Bean
    @Profile("prod")
    fun dataSource(): DataSource {
        val dbUri = URI(System.getenv("DATABASE_URL"))

        val config = HikariConfig()
        config.jdbcUrl = "jdbc:postgresql://" + dbUri.host + ':' + dbUri.port + dbUri.getPath()
        config.username = dbUri.userInfo.split(":")[0]
        config.password = dbUri.userInfo.split(":")[1]

        return HikariDataSource(config)
    }
}