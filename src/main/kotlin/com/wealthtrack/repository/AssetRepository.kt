package com.wealthtrack.repository

import com.wealthtrack.model.Asset
import com.wealthtrack.model.AssetType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository : JpaRepository<Asset, Long> {
    fun findBySymbol(symbol: String): Asset?

    fun findByType(type: AssetType): List<Asset>

    @Query("SELECT a FROM Asset a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.symbol) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    fun searchAssets(searchTerm: String): List<Asset>

    @Query("SELECT a FROM Asset a JOIN FETCH a.marketData WHERE a.id = :id")
    fun findByIdWithMarketData(id: Long): Asset?
}
