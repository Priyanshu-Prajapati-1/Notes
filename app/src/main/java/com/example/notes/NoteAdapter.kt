package com.example.notes

import MyNote
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val context: Context, private val noteList: ArrayList<MyNote>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notes_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = noteList[position]

        holder.title.text = history.title
        holder.content.text = history.textContent

        holder.rootll.setOnClickListener {
            updateNote(position)
        }

        holder.rootll.setOnLongClickListener{

//            deleteNote(noteList[position].title, noteList[position].textContent, position)
            deleteNote(position)
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateNote(position: Int) {
        val dialog  = Dialog(context)
        dialog.setContentView(R.layout.note_layout)


        val check = dialog.findViewById<ImageView>(R.id.check)
        val close = dialog.findViewById<ImageView>(R.id.close)
        val title = dialog.findViewById<EditText>(R.id.title)
        val text = dialog.findViewById<EditText>(R.id.text)

        val existingDetails : MyNote = noteList[position]

        title.setText(existingDetails.title)
        text.setText(existingDetails.textContent)


        check.setOnClickListener{
            val newTitle : String = title.text.toString()
            val newContent : String = text.text.toString()

            if(newTitle.isEmpty() && newContent.isEmpty()){
                Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show()
            }else{
                val db = NoteDataBase(context)
                noteList[position] = MyNote(newTitle, newContent)
                db.updateNote(MyNote(newTitle, newContent),  existingDetails.id, existingDetails.title, existingDetails.textContent)

                // Start MainActivity
//                val intent = Intent(context, MainActivity::class.java)
//                context.startActivity(intent)
//
//                // Finish the current activity (optional)
//                if (context is MainActivity) {
//                    context.finish()
//                }
                notifyItemChanged(position)

                noteList.clear()
                noteList.addAll(db.getNotes())
                notifyDataSetChanged()

            }
            dialog.dismiss()

        }

        close.setOnClickListener{
            dialog.dismiss()
            check.performClick()
        }
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
//    private fun deleteNote(title: String, textContent: String, position: Int) {
    private fun deleteNote( position: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.delete_layout)

        val yes = dialog.findViewById<AppCompatButton>(R.id.btndelete)
        val no = dialog.findViewById<AppCompatButton>(R.id.btnAddNo)
        val text = dialog.findViewById<TextView>(R.id.Select)

        text.text = noteList[position].title

        yes.setOnClickListener{

            val db = NoteDataBase(context)

            db.deleteNote(noteList[position].id, noteList[position].title, noteList[position].textContent)
//            db.deleteNote(noteList[position].title, noteList[position].textContent)

            noteList.removeAt(position)
            notifyItemRemoved(position)

            noteList.clear()
            noteList.addAll(db.getNotes())
            notifyDataSetChanged()

            if(noteList.isEmpty()){
                val iNext = Intent(context, MainActivity::class.java)
                context.startActivity(iNext)
            }

            dialog.dismiss()

        }

        no.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val content: TextView = itemView.findViewById(R.id.content)
        val rootll: LinearLayout = itemView.findViewById(R.id.rootll)
    }
}
