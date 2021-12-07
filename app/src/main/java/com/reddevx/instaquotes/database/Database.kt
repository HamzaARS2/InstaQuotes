package com.reddevx.instaquotes.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(private val context:Context, )
    : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
        companion object {
            const val DATABASE_NAME = "favoriteQuotes.db"
            const val DATABASE_VERSION = 4

            // Favorite table
            const val FAVORITE_TABLE_NAME = "favoriteQuotes"
            const val FAVORITE_CLM_IMAGE = "imageUrl"
            const val FAVORITE_CLM_QUOTE = "quoteText"
            const val FAVORITE_CLM_CATEGORY = "quoteCategory"

        }

    override fun onCreate(db: SQLiteDatabase?) {
       val favoriteTable = "CREATE TABLE $FAVORITE_TABLE_NAME (" +
               "$FAVORITE_CLM_IMAGE TEXT NOT NULL," +
               "$FAVORITE_CLM_QUOTE TEXT NOT NULL," +
               "$FAVORITE_CLM_CATEGORY TEXT NOT NULL)"
        db?.execSQL(favoriteTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}