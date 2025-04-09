package com.wealthtrack.controller

import com.wealthtrack.dto.request.SimulationRequest
import com.wealthtrack.dto.response.SimulationResponse
import com.wealthtrack.service.SimulationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/simulations")
@Tag(name = "Investment Simulations", description = "Investment Simulation API")
@SecurityRequirement(name = "bearerAuth")
class SimulationController(private val simulationService: SimulationService) {

    @GetMapping
    @Operation(summary = "Get all simulations for the current user")
    fun getAllSimulations(): ResponseEntity<List<SimulationResponse>> {
        val simulations = simulationService.getAllSimulations()
        return ResponseEntity.ok(simulations)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get simulation by ID")
    fun getSimulationById(@PathVariable id: UUID): ResponseEntity<SimulationResponse> {
        val simulation = simulationService.getSimulationById(id)
        return ResponseEntity.ok(simulation)
    }

    @PostMapping
    @Operation(summary = "Create a new investment simulation")
    fun createSimulation(@Valid @RequestBody request: SimulationRequest): ResponseEntity<SimulationResponse> {
        val simulation = simulationService.createSimulation(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(simulation)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a simulation")
    fun deleteSimulation(@PathVariable id: UUID): ResponseEntity<Void> {
        simulationService.deleteSimulation(id)
        return ResponseEntity.noContent().build()
    }
}
