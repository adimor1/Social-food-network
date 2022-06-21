package com.example.andro2client.model

import java.io.Serializable

data class User(
    val id: String,
    val email: String,
    val name: String,
    val favorite: String,
    val birth: String,
    val type: String,
    val gender: String,
    val isBlueV: String

) : Serializable

