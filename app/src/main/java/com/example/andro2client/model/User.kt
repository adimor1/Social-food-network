package com.example.andro2client.model

import java.io.Serializable

data class User(
    val id: String,
    val email: String,
    val name: String
) : Serializable

