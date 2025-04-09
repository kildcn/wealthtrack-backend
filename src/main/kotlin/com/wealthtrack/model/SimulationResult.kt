package com.wealthtrack.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "simulation_results")
data class SimulationResult(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val name: String,

    @Column(length = 1000)
    val description: String? = null,

    @Column(name = "initial_investment", nullable = false)
    val initialInvestment: Double,

    @Column(name = "monthly_contribution", nullable = false)
    val monthlyContribution: Double,

    @Column(name = "annual_return_rate", nullable = false)
    val annualReturnRate: Double,

    @Column(name = "investment_duration_years", nullable = false)
    val investmentDurationYears: Int,

    @Column(name = "inflation_rate", nullable = false)
    val inflationRate: Double,

    @Column(name = "tax_rate", nullable = false)
    val taxRate: Double,

    @Column(name = "final_amount", nullable = false)
    val finalAmount: Double,

    @Column(name = "total_contributions", nullable = false)
    val totalContributions: Double,

    @Column(name = "total_earnings", nullable = false)
    val totalEarnings: Double,

    @Column(name = "result_data", columnDefinition = "TEXT")
    val resultDataJson: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
