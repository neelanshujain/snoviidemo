package com.example.demo.controller

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository
) {

    @PostMapping
    fun createUser(@RequestBody user: User): User {
        return userRepository.save(user)
    }

    @GetMapping
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): User? {
        return userRepository.findById(id).orElse(null)
    }
}
