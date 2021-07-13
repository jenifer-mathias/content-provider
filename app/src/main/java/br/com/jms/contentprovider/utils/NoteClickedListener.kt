package br.com.jms.contentprovider.utils

import android.database.Cursor

interface NoteClickedListener {

    fun noteClickedItem(cursor: Cursor)

    fun noteRemoveItem(cursor: Cursor?)

}