package com.wealthtrack.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PortfolioRequest(
    @field:NotBlank(message = "Portfolio name is required")
    @field:Size(min = 2, max = 100, message = "Portfolio name must be between 2 and 100 characters")
    val name: String,

    @field:Size(max = 1000, message = "Description cannot exceed 1000 characters")
    val description: String? = null
)
