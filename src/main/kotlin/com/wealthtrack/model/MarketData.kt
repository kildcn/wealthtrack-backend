package com.wealthtrack.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "market_data")
data class MarketData(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @OneToOne(mappedBy = "marketData")
    val asset: Asset? = null,

    @Column(name = "day_low")
    val dayLow: Double? = null,

    @Column(name = "day_high")
    val dayHigh: Double? = null,

    @Column(name = "year_low")
    val yearLow: Double? = null,

    @Column(name = "year_high")
    val yearHigh: Double? = null,

    @Column(name = "market_cap")
    val marketCap: Double? = null,

    @Column(name = "pe_ratio")
    val peRatio: Double? = null,

    @Column(name = "dividend_yield")
    val dividendYield: Double? = null,

    @Column(name = "volume")
    val volume: Long? = null,

    @Column(name = "average_volume")
    val averageVolume: Long? = null,

    @Column(name = "fifty_day_avg")
    val fiftyDayAvg: Double? = null,

    @Column(name = "two_hundred_day_avg")
    val twoHundredDayAvg: Double? = null,

    @Column(name = "historical_data", columnDefinition = "TEXT")
    val historicalDataJson: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
