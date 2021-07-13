package br.com.jms.contentprovider.database

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.jms.contentprovider.utils.NoteClickedListener
import br.com.jms.contentprovider.R
import br.com.jms.contentprovider.database.NotesDBHelper.Companion.DESCRIPTION_NOTES
import br.com.jms.contentprovider.database.NotesDBHelper.Companion.TITLE_NOTES

class NotesAdapter(private val listener: NoteClickedListener): RecyclerView.Adapter<NotesViewHolder>() {

    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))

    override fun getItemCount(): Int = if (mCursor != null ) mCursor?.count as Int else 0

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        mCursor?.moveToPosition(position)
        holder.noteTitle.text = mCursor?.getString(mCursor?.getColumnIndex(TITLE_NOTES) as Int)
        holder.noteDescription.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRIPTION_NOTES) as Int)
        holder.noteButtonRemove.setOnClickListener {
            mCursor?.moveToPosition(position)
            listener.noteRemoveItem(mCursor)
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener { listener.noteClickedItem(mCursor as Cursor) }
    }

    fun setCursor(newCursor: Cursor?) {
        mCursor = newCursor
        notifyDataSetChanged()
    }
}

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val noteTitle = itemView.findViewById(R.id.tx_note_title) as TextView
    val noteDescription = itemView.findViewById(R.id.tx_note_description) as TextView
    val noteButtonRemove = itemView.findViewById(R.id.bt_remove_note) as Button
}