package com.reddevx.instaquotes.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.reddevx.instaquotes.models.Quote

class DatabaseManager private constructor(private val context: Context,
                      private val sqLiteOpenHelper:SQLiteOpenHelper = Database(context)) {

    private lateinit var database:SQLiteDatabase

    companion object {
         private var instance:DatabaseManager? = null
        operator fun invoke(context: Context) = synchronized(this) {
            if (instance == null)
                instance = DatabaseManager(context)
            instance
        }
    }

    fun open(){
        database = sqLiteOpenHelper.writableDatabase
    }
    fun close(){
        if (database != null)
            database.close()
    }

    fun insert(quote: Quote) : Boolean {
        val cv = ContentValues()
        cv.put(Database.FAVORITE_CLM_IMAGE,quote.imageUrl)
        cv.put(Database.FAVORITE_CLM_QUOTE,quote.quoteText)
        cv.put(Database.FAVORITE_CLM_CATEGORY,quote.category)
        val result: Long = database.insert(Database.FAVORITE_TABLE_NAME,null,cv)
        return result != -1L
    }

    fun isFromFavorites(quoteText:String) : Boolean {
        val cursor = database.rawQuery("SELECT * FROM ${Database.FAVORITE_TABLE_NAME} WHERE ${Database.FAVORITE_CLM_QUOTE} = ?",
            arrayOf(quoteText))
        if (cursor.moveToNext()){
            cursor.close()
            return true
        }else{
            cursor.close()
            return false
        }
    }

    fun delete(quote: Quote) : Boolean {
        val result = database.delete(Database.FAVORITE_TABLE_NAME,
            Database.FAVORITE_CLM_QUOTE + " = ?",
            arrayOf(quote.quoteText))
        return result > 0
    }

    fun deleteAll() : Boolean {
        return database.delete(Database.FAVORITE_TABLE_NAME, null,null) > 0
    }


    fun getUserFavorites() : ArrayList<Quote> {
        val quotes = ArrayList<Quote>()
        val cursor: Cursor =
            database.rawQuery("SELECT * FROM ${Database.FAVORITE_TABLE_NAME}", null)
        while (cursor.moveToNext()){
            val imageUrl = cursor.getString(cursor.getColumnIndex(Database.FAVORITE_CLM_IMAGE))
            val quoteText = cursor.getString(cursor.getColumnIndex(Database.FAVORITE_CLM_QUOTE))
            val category = cursor.getString(cursor.getColumnIndex(Database.FAVORITE_CLM_CATEGORY))
            quotes.add(Quote(imageUrl,quoteText, category))
        }
        cursor.close()
        return quotes

    }

}