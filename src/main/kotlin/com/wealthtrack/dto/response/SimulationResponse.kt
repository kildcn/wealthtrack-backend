package com.wealthtrack.dto.response

import java.util.UUID

data class SimulationResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val initialInvestment: Double,
    val monthlyContribution: Double,
    val annualReturnRate: Double,
    val investmentDurationYears: Int,
    val inflationRate: Double,
    val taxRate: Double,
    val finalAmount: Double,
    val totalContributions: Double,
    val totalEarnings: Double,
    val yearlyResults: List<YearlyResult>
)

data class YearlyResult(
    val year: Int,
    val yearlyContribution: Double,
    val yearlyEarnings: Double,
    val yearlyTaxes: Double,
    val balanceWithInflation: Double,
    val balanceWithoutInflation: Double
)
