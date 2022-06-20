package com.example.andro2client

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_users)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //db.execSQL("drop table menu");
        db.execSQL(CREATE_TABLE_users)
    }

    companion object {
        // Database Information
        const val DB_NAME = "mymoviesreviews.DB"

        // database version
        const val DB_VERSION = 1

        // Table Name
        const val users = "users"

        // Table columns
        const val _id = "_id"
        const val username = "username"
        const val password = "password"
        const val email = "email"

        // Creating table query
        private const val CREATE_TABLE_users = ("create table IF NOT EXISTS " + users +
                "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + username + " text , "
                + password + " text ,"
                + email + " text "
                +
                ");")
    }
}