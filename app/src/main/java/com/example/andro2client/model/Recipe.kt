package com.example.andro2client.model

import android.os.Parcelable
import java.io.Serializable



data class Recipe(
    val id: String,
    val time: String,
    val level: String,
    val type: String,
    val foodType: String,
    val creatorMail: String,
    val imageRec: String
    ) : Serializable

