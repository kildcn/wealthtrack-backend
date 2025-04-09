package com.wealthtrack.service

import com.wealthtrack.exception.ResourceNotFoundException
import com.wealthtrack.model.User
import com.wealthtrack.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Service
class UserService(private val userRepository: UserRepository) {

    fun getCurrentUser(): User {
        val username = SecurityContextHolder.getContext().authentication.name
        return userRepository.findByEmail(username)
            ?: throw ResourceNotFoundException("Current user not found")
    }

    @Transactional(readOnly = true)
    fun getUserById(id: UUID): User {
        return userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
    }

    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("User not found with email: $email")
    }

    @Transactional
    fun updateUserProfile(user: User, firstName: String, lastName: String): User {
        // Create a new user instance with updated fields
        val updatedUser = user.copy(
            firstName = firstName,
            lastName = lastName
        )

        logger.info { "Updating profile for user: ${user.email}" }
        return userRepository.save(updatedUser)
    }
}
