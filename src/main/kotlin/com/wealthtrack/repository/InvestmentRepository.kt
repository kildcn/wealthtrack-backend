package com.wealthtrack.repository

import com.wealthtrack.model.Investment
import com.wealthtrack.model.Portfolio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface InvestmentRepository : JpaRepository<Investment, UUID> {
    fun findByPortfolio(portfolio: Portfolio): List<Investment>

    @Query("SELECT i FROM Investment i JOIN FETCH i.transactions WHERE i.id = :id")
    fun findByIdWithTransactions(id: UUID): Investment?

    @Query("SELECT i FROM Investment i JOIN i.portfolio p WHERE p.user.id = :userId")
    fun findAllByUserId(userId: UUID): List<Investment>
}
