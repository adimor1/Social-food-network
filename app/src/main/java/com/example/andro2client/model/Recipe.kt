package com.example.andro2client.model

import android.os.Parcelable
import java.io.Serializable

data class Recipe(
    val time: String,
    val level: String,
    val creatorMail: String
    ) : Serializable

