package com.neotica.storyapp.response.user


data class User(
    val email: String,
    val password: String
)


data class UserBody(
    val name: String,
    val email: String,
    val password: String
)

