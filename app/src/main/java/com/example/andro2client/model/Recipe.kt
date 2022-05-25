package com.example.andro2client.model

import android.os.Parcelable
import java.io.Serializable

data class Recipe(
    val id: String,
    val time: String,
    val level: String,
    val type: String,
    val creatorMail: String
    ) : Serializable

