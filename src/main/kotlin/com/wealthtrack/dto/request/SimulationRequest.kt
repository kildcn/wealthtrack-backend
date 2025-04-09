package com.wealthtrack.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SimulationRequest(
    @field:NotBlank(message = "Simulation name is required")
    @field:Size(min = 2, max = 100, message = "Simulation name must be between 2 and 100 characters")
    val name: String,

    @field:Size(max = 1000, message = "Description cannot exceed 1000 characters")
    val description: String? = null,

    @field:Min(value = 0, message = "Initial investment must be positive")
    val initialInvestment: Double,

    @field:Min(value = 0, message = "Monthly contribution must be positive")
    val monthlyContribution: Double,

    @field:Min(value = -100, message = "Annual return rate cannot be less than -100%")
    @field:Max(value = 1000, message = "Annual return rate cannot exceed 1000%")
    val annualReturnRate: Double,

    @field:Min(value = 1, message = "Investment duration must be at least 1 year")
    @field:Max(value = 100, message = "Investment duration cannot exceed 100 years")
    val investmentDurationYears: Int,

    @field:Min(value = 0, message = "Inflation rate must be positive")
    @field:Max(value = 100, message = "Inflation rate cannot exceed 100%")
    val inflationRate: Double = 2.0,

    @field:Min(value = 0, message = "Tax rate must be positive")
    @field:Max(value = 100, message = "Tax rate cannot exceed 100%")
    val taxRate: Double = 0.0
)
