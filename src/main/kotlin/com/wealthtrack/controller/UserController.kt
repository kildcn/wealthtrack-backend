package com.wealthtrack.controller

import com.wealthtrack.dto.response.UserResponse
import com.wealthtrack.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User Management API")
@SecurityRequirement(name = "bearerAuth")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    fun getCurrentUser(): ResponseEntity<UserResponse> {
        val currentUser = userService.getCurrentUser()

        val userResponse = UserResponse(
            id = currentUser.id,
            email = currentUser.email,
            firstName = currentUser.firstName,
            lastName = currentUser.lastName,
            roles = currentUser.roles.map { it.name }
        )

        return ResponseEntity.ok(userResponse)
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    fun updateUserProfile(@Valid @RequestBody updateRequest: Map<String, String>): ResponseEntity<UserResponse> {
        val currentUser = userService.getCurrentUser()

        val firstName = updateRequest["firstName"] ?: currentUser.firstName
        val lastName = updateRequest["lastName"] ?: currentUser.lastName

        val updatedUser = userService.updateUserProfile(currentUser, firstName, lastName)

        val userResponse = UserResponse(
            id = updatedUser.id,
            email = updatedUser.email,
            firstName = updatedUser.firstName,
            lastName = updatedUser.lastName,
            roles = updatedUser.roles.map { it.name }
        )

        return ResponseEntity.ok(userResponse)
    }
}
