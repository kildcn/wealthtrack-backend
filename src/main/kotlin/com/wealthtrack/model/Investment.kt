package com.wealthtrack.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "investments")
data class Investment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    var portfolio: Portfolio,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    val asset: Asset,

    @Column(name = "initial_amount", nullable = false)
    val initialAmount: Double,

    @Column(name = "quantity", nullable = false)
    var quantity: Double,

    @Column(name = "purchase_price", nullable = false)
    val purchasePrice: Double,

    @Column(name = "purchase_date", nullable = false)
    val purchaseDate: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "investment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val transactions: MutableList<Transaction> = mutableListOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Add a transaction to this investment
    fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
        transaction.investment = this

        // Update quantity based on transaction type
        when (transaction.type) {
            TransactionType.BUY -> this.quantity += transaction.quantity
            TransactionType.SELL -> this.quantity -= transaction.quantity
        }

        // Update timestamp
        this.updatedAt = LocalDateTime.now()
    }

    // Calculate current value of the investment
    fun getCurrentValue(): Double {
        return quantity * asset.currentPrice
    }

    // Calculate performance percentage
    fun getPerformancePercentage(): Double {
        val currentValue = getCurrentValue()
        return if (initialAmount > 0) {
            ((currentValue - initialAmount) / initialAmount) * 100
        } else {
            0.0
        }
    }
}
