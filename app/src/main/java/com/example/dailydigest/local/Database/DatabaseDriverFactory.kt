package com.example.dailydigest.local.Database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.dailydigest.AppDatabase

class DatabaseDriverFactory(private val context: Context){

    fun create():SqlDriver{
        return  AndroidSqliteDriver(AppDatabase.Schema, context, "newsDatabase.db")
    }

}
