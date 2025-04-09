package com.wealthtrack.dto.response

import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: List<String>
)
