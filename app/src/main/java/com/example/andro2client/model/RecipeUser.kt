package com.example.andro2client.model

import java.io.Serializable

    data class RecipeUser(
        val id: String,
        val time: String,
        val level: String,
        val type: String,
        val foodType: String,
        val creatorMail: String,
        val imageRec: String,
        val name: String,
        val ingredients: String,
        val instruction: String,
        val sponsored: String,
        val isBlueV: String,
    ) : Serializable

