package com.example.andro2client.model


object LoginUser {
    var loginEmail: String = ""
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var isAdmin: String = "false"
        get() {
            return field
        }
        set(value) {
            field = value
        }
}