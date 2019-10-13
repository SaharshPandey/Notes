package com.saharsh.notes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log
import android.widget.Toast

class DBManager {
    val dbName = "NotesbySaharsh"
    val dbTable="Notes"
    val colId = "_id"
    val colTitle = "title"
    val colDescp = "description"
    val colTime = "time"
    val dbVersion = 1

    //CREATE TABLE IF NOT EXISTS NOTES (ID INTEGER PRIMARY KEY, title TEXT, descp TEXT);
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS "+dbTable+" ("+ colId +" INTEGER PRIMARY KEY, "+
            colTitle + " TEXT, "+ colDescp +" TEXT, "+ colTime+ " TEXT);"

    var sqlDB: SQLiteDatabase? =null

    constructor(context: Context?){
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase

    }

    inner class DatabaseHelperNotes : SQLiteOpenHelper{

        var context: Context? = null
        constructor(context: Context?):super(context, dbName, null, dbVersion){
        this.context = context
        }

        override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL(sqlCreateTable)
            Log.d("DB","DATABASE CREATED")
            //Toast.makeText(this.context, "database created", Toast.LENGTH_SHORT).show()

        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            Log.d("DB","DATABASE UPGRADED")
            p0!!.execSQL("DROP TABLE IF EXISTS"+ dbTable)

        }

    }

    fun Insert(values:ContentValues): Long{
        val ID = sqlDB!!.insert(dbTable, "", values)
        return ID
    }

    fun Query(projection: Array<String>, selection: String, selectionArgs: Array<String>, sorOrder: String?): Cursor{
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sorOrder)
        return  cursor
    }

    fun Delete(selection:String, selectionArgs: Array<String>): Int{
        val count = sqlDB!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun Update(values:ContentValues, selection: String, selectionArgs: Array<String>): Int{
        val count = sqlDB!!.update(dbTable, values, selection, selectionArgs)
        return count
    }
}
