package com.wealthtrack.controller

import com.wealthtrack.dto.request.LoginRequest
import com.wealthtrack.dto.request.SignupRequest
import com.wealthtrack.dto.response.JwtResponse
import com.wealthtrack.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
class AuthController(private val authService: AuthService) {

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    fun registerUser(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<Map<String, String>> {
        authService.registerUser(signupRequest)

        return ResponseEntity.ok(mapOf(
            "message" to "User registered successfully"
        ))
    }

    @PostMapping("/signin")
    @Operation(summary = "Authenticate a user and get tokens")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<JwtResponse> {
        val jwtResponse = authService.authenticateUser(loginRequest)

        return ResponseEntity.ok(jwtResponse)
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token using refresh token")
    fun refreshToken(@RequestBody refreshTokenRequest: Map<String, String>): ResponseEntity<JwtResponse> {
        val refreshToken = refreshTokenRequest["refreshToken"]
            ?: return ResponseEntity.badRequest().build()

        val jwtResponse = authService.refreshToken(refreshToken)

        return ResponseEntity.ok(jwtResponse)
    }
}
