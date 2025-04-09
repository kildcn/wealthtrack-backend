package com.wealthtrack.service

import com.wealthtrack.dto.request.PortfolioRequest
import com.wealthtrack.exception.BadRequestException
import com.wealthtrack.exception.ResourceNotFoundException
import com.wealthtrack.model.Portfolio
import com.wealthtrack.model.User
import com.wealthtrack.repository.PortfolioRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Service
class PortfolioService(
    private val portfolioRepository: PortfolioRepository,
    private val userService: UserService
) {

    @Transactional(readOnly = true)
    fun getAllPortfolios(): List<Portfolio> {
        val currentUser = userService.getCurrentUser()
        return portfolioRepository.findByUser(currentUser)
    }

    @Transactional(readOnly = true)
    fun getPortfolioById(id: UUID): Portfolio {
        val currentUser = userService.getCurrentUser()
        return portfolioRepository.findByUserIdAndId(currentUser.id, id)
            ?: throw ResourceNotFoundException("Portfolio not found with id: $id")
    }

    @Transactional(readOnly = true)
    fun getPortfolioWithInvestments(id: UUID): Portfolio {
        val portfolio = portfolioRepository.findByIdWithInvestments(id)
            ?: throw ResourceNotFoundException("Portfolio not found with id: $id")

        // Verify ownership
        val currentUser = userService.getCurrentUser()
        if (portfolio.user.id != currentUser.id) {
            throw BadRequestException("You don't have access to this portfolio")
        }

        return portfolio
    }

    @Transactional
    fun createPortfolio(portfolioRequest: PortfolioRequest): Portfolio {
        val currentUser = userService.getCurrentUser()

        val portfolio = Portfolio(
            name = portfolioRequest.name,
            description = portfolioRequest.description,
            user = currentUser
        )

        logger.info { "Creating new portfolio '${portfolio.name}' for user: ${currentUser.email}" }
        return portfolioRepository.save(portfolio)
    }

    @Transactional
    fun updatePortfolio(id: UUID, portfolioRequest: PortfolioRequest): Portfolio {
        val portfolio = getPortfolioById(id)

        // Update fields
        portfolio.name = portfolioRequest.name
        portfolio.description = portfolioRequest.description
        portfolio.updatedAt = LocalDateTime.now()

        logger.info { "Updating portfolio '${portfolio.name}' with id: $id" }
        return portfolioRepository.save(portfolio)
    }

    @Transactional
    fun deletePortfolio(id: UUID) {
        val portfolio = getPortfolioById(id)

        logger.info { "Deleting portfolio '${portfolio.name}' with id: $id" }
        portfolioRepository.delete(portfolio)
    }
}
