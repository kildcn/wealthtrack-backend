package com.wealthtrack.controller

import com.wealthtrack.dto.request.PortfolioRequest
import com.wealthtrack.model.Portfolio
import com.wealthtrack.service.PortfolioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/portfolios")
@Tag(name = "Portfolios", description = "Portfolio Management API")
@SecurityRequirement(name = "bearerAuth")
class PortfolioController(private val portfolioService: PortfolioService) {

    @GetMapping
    @Operation(summary = "Get all portfolios for current user")
    fun getAllPortfolios(): ResponseEntity<List<Portfolio>> {
        val portfolios = portfolioService.getAllPortfolios()
        return ResponseEntity.ok(portfolios)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get portfolio by ID")
    fun getPortfolioById(@PathVariable id: UUID): ResponseEntity<Portfolio> {
        val portfolio = portfolioService.getPortfolioById(id)
        return ResponseEntity.ok(portfolio)
    }

    @GetMapping("/{id}/investments")
    @Operation(summary = "Get portfolio with all investments")
    fun getPortfolioWithInvestments(@PathVariable id: UUID): ResponseEntity<Portfolio> {
        val portfolio = portfolioService.getPortfolioWithInvestments(id)
        return ResponseEntity.ok(portfolio)
    }

    @PostMapping
    @Operation(summary = "Create a new portfolio")
    fun createPortfolio(@Valid @RequestBody portfolioRequest: PortfolioRequest): ResponseEntity<Portfolio> {
        val createdPortfolio = portfolioService.createPortfolio(portfolioRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPortfolio)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing portfolio")
    fun updatePortfolio(
        @PathVariable id: UUID,
        @Valid @RequestBody portfolioRequest: PortfolioRequest
    ): ResponseEntity<Portfolio> {
        val updatedPortfolio = portfolioService.updatePortfolio(id, portfolioRequest)
        return ResponseEntity.ok(updatedPortfolio)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a portfolio")
    fun deletePortfolio(@PathVariable id: UUID): ResponseEntity<Void> {
        portfolioService.deletePortfolio(id)
        return ResponseEntity.noContent().build()
    }
}
