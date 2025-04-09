package com.wealthtrack.repository

import com.wealthtrack.model.SimulationResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SimulationResultRepository : JpaRepository<SimulationResult, UUID> {
    fun findByUserId(userId: UUID): List<SimulationResult>
    fun findByIdAndUserId(id: UUID, userId: UUID): SimulationResult?
}
