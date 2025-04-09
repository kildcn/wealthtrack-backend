package com.wealthtrack.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "portfolios")
data class Portfolio(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String,

    @Column(length = 1000)
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @OneToMany(mappedBy = "portfolio", cascade = [CascadeType.ALL], orphanRemoval = true)
    val investments: MutableList<Investment> = mutableListOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Convenience method to add an investment
    fun addInvestment(investment: Investment) {
        investments.add(investment)
        investment.portfolio = this
    }

    // Calculate portfolio total value
    fun getTotalValue(): Double {
        return investments.sumOf { it.getCurrentValue() }
    }

    // Calculate portfolio performance
    fun getPerformancePercentage(): Double {
        val totalInvested = investments.sumOf { it.initialAmount }
        val currentValue = getTotalValue()

        return if (totalInvested > 0) {
            ((currentValue - totalInvested) / totalInvested) * 100
        } else {
            0.0
        }
    }
}
