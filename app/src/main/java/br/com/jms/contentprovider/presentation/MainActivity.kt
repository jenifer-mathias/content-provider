package br.com.jms.contentprovider.presentation

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns._ID
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.jms.contentprovider.R
import br.com.jms.contentprovider.database.NotesAdapter
import br.com.jms.contentprovider.database.NotesDBHelper.Companion.TITLE_NOTES
import br.com.jms.contentprovider.database.NotesProvider.Companion.URI_NOTES
import br.com.jms.contentprovider.presentation.detail.NotesDetailFragment
import br.com.jms.contentprovider.utils.NoteClickedListener
import br.com.jms.contentprovider.utils.showNotification
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import android.text.TextUtils

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var adapter: NotesAdapter

    companion object {
        const val TAG_TOKEN_FIREBASE = "NEW_TOKEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        note_add.setOnClickListener {
            NotesDetailFragment().show(supportFragmentManager, "dialog")
        }

        adapter = NotesAdapter(object : NoteClickedListener {

            @SuppressLint("Range")
            override fun noteClickedItem(cursor: Cursor) {
                val id = cursor.getLong(cursor.getColumnIndex(_ID))
                val fragment = NotesDetailFragment.newInstance(id)
                fragment.show(supportFragmentManager, "dialog")
            }

            @SuppressLint("Range")
            override fun noteRemoveItem(cursor: Cursor?) {
                val id = cursor?.getLong(cursor.getColumnIndex(_ID))
                contentResolver.delete(Uri.withAppendedPath(URI_NOTES, id.toString()), null, null)
            }

        })
        adapter.setHasStableIds(true)

        notes_recycler.layoutManager = LinearLayoutManager(this)
        notes_recycler.adapter = adapter

        LoaderManager.getInstance(this).initLoader(0, null, this)

        bt_send_notification.setOnClickListener {
            this.showNotification("1234", "Android with Kotlin", "Hi there!")
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                Log.d(TAG_TOKEN_FIREBASE,token)
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(this, URI_NOTES, null, null, null, TITLE_NOTES)

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) {
            adapter.setCursor(data)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.setCursor(null)
    }
}