package br.com.jms.contentprovider.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns._ID
import br.com.jms.contentprovider.database.NotesDBHelper.Companion.TABLE_NOTES

class NotesProvider : ContentProvider() {

    private lateinit var mUriMatcher: UriMatcher
    private lateinit var dbHelper: NotesDBHelper

    override fun onCreate(): Boolean {
        mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTHORITY, "notes", NOTES)
        mUriMatcher.addURI(AUTHORITY, "notes/#", NOTES_BY_ID)
        if (context != null) { dbHelper = NotesDBHelper(context as Context) }
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
       if (mUriMatcher.match(uri) == NOTES_BY_ID) {
           val db: SQLiteDatabase = dbHelper.writableDatabase
           val linesAffect = db.delete(TABLE_NOTES, "$_ID=?", arrayOf(uri.lastPathSegment))
           db.close()
           context?.contentResolver?.notifyChange(uri, null)
           return linesAffect
       } else {
           throw UnsupportedSchemeException("Uri inválida para exclusão!")
       }
    }

    // para requisição de arquivos
    override fun getType(uri: Uri): String? = throw UnsupportedSchemeException("Uri não implementado")

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (mUriMatcher.match(uri) == NOTES) {
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val id = db.insert(TABLE_NOTES, null, values)
            val insertUri = Uri.withAppendedPath(BASE_URI, id.toString())
            db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return insertUri
        } else {
            throw UnsupportedSchemeException("Uri inválida para inserção!")
        }
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    companion object {

        const val AUTHORITY = "br.com.jms.contentprovider.provider"

        val BASE_URI = Uri.parse("content://$AUTHORITY")
        val URI_NOTES = Uri.withAppendedPath(BASE_URI, "notes")
        // "content://br.com.jms.contentprovider.provider.provider/notes"

        const val NOTES = 1
        const val NOTES_BY_ID = 2
    }
}