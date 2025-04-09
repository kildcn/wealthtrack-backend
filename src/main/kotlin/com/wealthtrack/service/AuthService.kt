package com.wealthtrack.service

import com.wealthtrack.dto.request.LoginRequest
import com.wealthtrack.dto.request.SignupRequest
import com.wealthtrack.dto.response.JwtResponse
import com.wealthtrack.exception.BadRequestException
import com.wealthtrack.model.Role
import com.wealthtrack.model.User
import com.wealthtrack.repository.RoleRepository
import com.wealthtrack.repository.UserRepository
import com.wealthtrack.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun registerUser(signupRequest: SignupRequest): User {
        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.email)) {
            throw BadRequestException("Email is already in use")
        }

        // Create new user
        val user = User(
            email = signupRequest.email,
            password = passwordEncoder.encode(signupRequest.password),
            firstName = signupRequest.firstName,
            lastName = signupRequest.lastName
        )

        // Add default user role
        val userRole = roleRepository.findByName(Role.ROLE_USER)
            ?: throw BadRequestException("Default role not found")

        user.addRole(userRole)

        logger.info { "Registering new user: ${signupRequest.email}" }
        return userRepository.save(user)
    }

    fun authenticateUser(loginRequest: LoginRequest): JwtResponse {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val accessToken = jwtTokenProvider.generateToken(authentication)
        val refreshToken = jwtTokenProvider.generateRefreshToken(loginRequest.email)

        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw BadRequestException("User not found with email: ${loginRequest.email}")

        logger.info { "User authenticated: ${loginRequest.email}" }

        return JwtResponse(
            id = user.id,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            roles = user.roles.map { it.name },
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    @Transactional
    fun refreshToken(refreshToken: String): JwtResponse {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw BadRequestException("Invalid refresh token")
        }

        val username = jwtTokenProvider.getUsernameFromToken(refreshToken)
        val user = userRepository.findByEmail(username)
            ?: throw BadRequestException("User not found")

        // Create new authentication object
        val authentication = UsernamePasswordAuthenticationToken(
            username, null, user.roles.map { org.springframework.security.core.authority.SimpleGrantedAuthority(it.name) }
        )

        // Generate new tokens
        val newAccessToken = jwtTokenProvider.generateToken(authentication)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(username)

        logger.info { "Token refreshed for user: $username" }

        return JwtResponse(
            id = user.id,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            roles = user.roles.map { it.name },
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }
}
