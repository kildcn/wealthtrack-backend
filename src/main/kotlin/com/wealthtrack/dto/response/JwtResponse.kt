package com.wealthtrack.dto.response

import java.util.UUID

data class JwtResponse(
    val id: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: List<String>,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer"
)
