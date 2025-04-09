package com.wealthtrack.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.type.TypeReference
import com.wealthtrack.dto.request.SimulationRequest
import com.wealthtrack.dto.response.SimulationResponse
import com.wealthtrack.dto.response.YearlyResult
import com.wealthtrack.model.SimulationResult
import com.wealthtrack.repository.SimulationResultRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Service
class SimulationService(
    private val simulationResultRepository: SimulationResultRepository,
    private val userService: UserService,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun createSimulation(request: SimulationRequest): SimulationResponse {
        val currentUser = userService.getCurrentUser()

        logger.info { "Creating investment simulation for user: ${currentUser.email}" }

        val yearlyResults = calculateYearlyResults(request)
        val finalYearResult = yearlyResults.last()

        val finalAmount = finalYearResult.balanceWithInflation
        val totalContributions = request.initialInvestment +
                                 (request.monthlyContribution * 12 * request.investmentDurationYears)
        val totalEarnings = finalAmount - totalContributions

        val simulationResult = SimulationResult(
            user = currentUser,
            name = request.name,
            description = request.description,
            initialInvestment = request.initialInvestment,
            monthlyContribution = request.monthlyContribution,
            annualReturnRate = request.annualReturnRate,
            investmentDurationYears = request.investmentDurationYears,
            inflationRate = request.inflationRate,
            taxRate = request.taxRate,
            finalAmount = finalAmount,
            totalContributions = totalContributions,
            totalEarnings = totalEarnings,
            resultDataJson = objectMapper.writeValueAsString(yearlyResults)
        )

        val savedResult = simulationResultRepository.save(simulationResult)

        return mapToResponse(savedResult, yearlyResults)
    }

    @Transactional(readOnly = true)
    fun getSimulationById(id: UUID): SimulationResponse {
        val currentUser = userService.getCurrentUser()

        val simulation = simulationResultRepository.findByIdAndUserId(id, currentUser.id)
            ?: throw com.wealthtrack.exception.ResourceNotFoundException("Simulation not found with id: $id")

        // Using TypeReference for proper type handling
        val typeRef = object : TypeReference<List<YearlyResult>>() {}
        val yearlyResults: List<YearlyResult> = objectMapper.readValue(simulation.resultDataJson, typeRef)

        return mapToResponse(simulation, yearlyResults)
    }

    @Transactional(readOnly = true)
    fun getAllSimulations(): List<SimulationResponse> {
        val currentUser = userService.getCurrentUser()

        return simulationResultRepository.findByUserId(currentUser.id).map { simulation ->
            // Using TypeReference for proper type handling
            val typeRef = object : TypeReference<List<YearlyResult>>() {}
            val yearlyResults: List<YearlyResult> = objectMapper.readValue(simulation.resultDataJson, typeRef)

            mapToResponse(simulation, yearlyResults)
        }
    }

    @Transactional
    fun deleteSimulation(id: UUID) {
        val currentUser = userService.getCurrentUser()

        val simulation = simulationResultRepository.findByIdAndUserId(id, currentUser.id)
            ?: throw com.wealthtrack.exception.ResourceNotFoundException("Simulation not found with id: $id")

        simulationResultRepository.delete(simulation)
    }

    private fun calculateYearlyResults(request: SimulationRequest): List<YearlyResult> {
        val yearlyResults = mutableListOf<YearlyResult>()

        var currentBalance = request.initialInvestment
        val monthlyContribution = request.monthlyContribution
        val monthlyReturnRate = request.annualReturnRate / 12 / 100
        val monthlyInflationRate = request.inflationRate / 12 / 100
        val taxRate = request.taxRate / 100

        // Tracking without inflation for comparison
        var currentBalanceWithoutInflation = request.initialInvestment

        for (year in 1..request.investmentDurationYears) {
            var yearlyContribution = 0.0
            var yearlyEarnings = 0.0
            var yearlyTaxes = 0.0

            // Tracking without inflation for comparison
            var yearlyEarningsWithoutInflation = 0.0

            // Calculate monthly compounding
            for (month in 1..12) {
                // Monthly contribution
                currentBalance += monthlyContribution
                currentBalanceWithoutInflation += monthlyContribution
                yearlyContribution += monthlyContribution

                // Calculate monthly return
                val monthlyEarnings = currentBalance * monthlyReturnRate
                yearlyEarnings += monthlyEarnings

                // Apply taxes if applicable
                val monthlyTaxes = if (monthlyEarnings > 0) monthlyEarnings * taxRate else 0.0
                yearlyTaxes += monthlyTaxes

                // Update balance with earnings and taxes
                currentBalance += (monthlyEarnings - monthlyTaxes)

                // Apply inflation
                currentBalance = currentBalance / (1 + monthlyInflationRate)

                // Without inflation calculations
                val monthlyEarningsWithoutInflation = currentBalanceWithoutInflation * monthlyReturnRate
                yearlyEarningsWithoutInflation += monthlyEarningsWithoutInflation

                // Update without-inflation balance (only earnings, no inflation adjustment)
                val monthlyTaxesWithoutInflation = if (monthlyEarningsWithoutInflation > 0)
                    monthlyEarningsWithoutInflation * taxRate else 0.0
                currentBalanceWithoutInflation += (monthlyEarningsWithoutInflation - monthlyTaxesWithoutInflation)
            }

            yearlyResults.add(
                YearlyResult(
                    year = year,
                    yearlyContribution = yearlyContribution,
                    yearlyEarnings = yearlyEarnings,
                    yearlyTaxes = yearlyTaxes,
                    balanceWithInflation = currentBalance,
                    balanceWithoutInflation = currentBalanceWithoutInflation
                )
            )
        }

        return yearlyResults
    }

    private fun mapToResponse(simulation: SimulationResult, yearlyResults: List<YearlyResult>): SimulationResponse {
        return SimulationResponse(
            id = simulation.id,
            name = simulation.name,
            description = simulation.description,
            initialInvestment = simulation.initialInvestment,
            monthlyContribution = simulation.monthlyContribution,
            annualReturnRate = simulation.annualReturnRate,
            investmentDurationYears = simulation.investmentDurationYears,
            inflationRate = simulation.inflationRate,
            taxRate = simulation.taxRate,
            finalAmount = simulation.finalAmount,
            totalContributions = simulation.totalContributions,
            totalEarnings = simulation.totalEarnings,
            yearlyResults = yearlyResults
        )
    }
}
