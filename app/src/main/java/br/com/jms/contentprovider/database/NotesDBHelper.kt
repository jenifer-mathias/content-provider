package br.com.jms.contentprovider.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.ID

class NotesDBHelper(
    context: Context
) : SQLiteOpenHelper(context, "databaseNotes", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
       db?.execSQL("CREATE TABLE $TABLE_NOTES (" +
               "$ID INTEGER NOT NULL PRIMARY KEY, " +
               "$TITLE_NOTES TEXT NOT NULL, " +
               "$DESCRIPTION_NOTES TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    companion object {
        const val TABLE_NOTES = "Notes"
        const val TITLE_NOTES = "title"
        const val DESCRIPTION_NOTES = "description"
    }

}