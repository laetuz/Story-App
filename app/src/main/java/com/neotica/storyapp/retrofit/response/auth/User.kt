package com.neotica.storyapp.retrofit.response.auth


data class User(
    val email: String,
    val password: String
)


data class Register(
    val name: String,
    val email: String,
    val password: String
)

