package com.example.andro2client.LocalDB

import android.annotation.SuppressLint
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

    fun delete(idevents: String) {
        val selectionArgs = arrayOf("1")
        database!!.delete("users", "_id=?", selectionArgs)
    }

    @SuppressLint("Range")
    fun userexit(): String {
        val selectionArgs: Array<String>
        selectionArgs = arrayOf("1")
        val Select = "select *" +
                " from users where _id=? " ;

        val cursor = database!!.rawQuery(Select, selectionArgs)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                return cursor.getString(cursor.getColumnIndex("username"))
            }
        }
        return ""

    }

    fun Sighningup(
        _id: Int, username: String?,
        password: String?
    ) {
        val contentValue = ContentValues()

            contentValue.put(DatabaseHelper._id, "1")

        contentValue.put(DatabaseHelper.username, username)
        contentValue.put(DatabaseHelper.password, password)

            database!!.insert(DatabaseHelper.users, null, contentValue)


    }
}


