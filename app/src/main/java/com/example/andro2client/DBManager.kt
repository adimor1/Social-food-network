package com.example.andro2client

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase


class DBManager(private val context: Context) {
    private var dbHelper: DatabaseHelper? = null
    private var database: SQLiteDatabase? = null
    @Throws(SQLException::class)
    fun open(): DBManager {
        dbHelper = DatabaseHelper(context)
        database = dbHelper!!.getWritableDatabase()
        return this
    }

    fun close() {
        dbHelper?.close()
    }

    fun userexit(username: String, password: String): Boolean {
        val selectionArgs: Array<String>
        selectionArgs = arrayOf(username, password)
        val Select = "select *" +
                " from users " +
                " where username = ? and" +
                " password = ? "
        val cursor = database!!.rawQuery(Select, selectionArgs)
        return if (cursor != null) {
            cursor.count > 0
        } else false
    }

    fun Sighningup(
        _id: Int, username: String?,
        password: String?
    ) {
        val contentValue = ContentValues()
        if (_id != 0) {
            contentValue.put(DatabaseHelper._id, _id)
        }
        contentValue.put(DatabaseHelper.username, username)
        contentValue.put(DatabaseHelper.password, password)
        if (_id == 0) {
            database!!.insert(DatabaseHelper.users, null, contentValue)
        } else {
            val whereArgs = arrayOf(Integer.toString(_id))
            database!!.update(
                DatabaseHelper.users,
                contentValue,
                DatabaseHelper._id.toString() + "=?",
                whereArgs
            )
        }
    }
}


