package com.wealthtrack.model

import jakarta.persistence.*
import java.time.LocalDateTime

enum class AssetType {
    STOCK, BOND, ETF, CRYPTO, COMMODITY, CASH, REAL_ESTATE, OTHER
}

@Entity
@Table(name = "assets")
data class Asset(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val symbol: String,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false)
    val type: AssetType,

    @Column(name = "current_price", nullable = false)
    var currentPrice: Double,

    @Column(name = "price_updated_at", nullable = false)
    var priceUpdatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "asset")
    val investments: MutableList<Investment> = mutableListOf(),

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "market_data_id")
    var marketData: MarketData? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
