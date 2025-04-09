package com.wealthtrack.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {
    @Value("\${app.security.jwt.secret}")
    lateinit var secret: String

    @Value("\${app.security.jwt.expiration}")
    var expirationTime: Long = 0

    @Value("\${app.security.jwt.refresh-expiration}")
    var refreshExpirationTime: Long = 0
}
