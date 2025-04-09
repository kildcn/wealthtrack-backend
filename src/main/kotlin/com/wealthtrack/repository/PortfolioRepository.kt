package com.wealthtrack.repository

import com.wealthtrack.model.Portfolio
import com.wealthtrack.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PortfolioRepository : JpaRepository<Portfolio, UUID> {
    fun findByUser(user: User): List<Portfolio>

    fun findByUserIdAndId(userId: UUID, id: UUID): Portfolio?

    @Query("SELECT p FROM Portfolio p JOIN FETCH p.investments WHERE p.id = :id")
    fun findByIdWithInvestments(id: UUID): Portfolio?
}
