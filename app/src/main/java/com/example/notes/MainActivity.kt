package com.example.notes

import MyNote
import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatBtn: FloatingActionButton = findViewById(R.id.floatBtn)
        val createNote: AppCompatButton = findViewById(R.id.createNote)

        initval()

        floatBtn.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.note_layout)

            val check = dialog.findViewById<ImageView>(R.id.check)
            val close = dialog.findViewById<ImageView>(R.id.close)
            val title = dialog.findViewById<EditText>(R.id.title)
            val text = dialog.findViewById<EditText>(R.id.text)


            close.setOnClickListener{
                dialog.dismiss()
            }

            check.setOnClickListener{
                val title : String = title.text.toString()
                val textContent :String = text.text.toString()

                if(!textContent.equals("")){

                    val recyclerView: RecyclerView = findViewById(R.id.recycleView)
                    val db = NoteDataBase(this)
                    val note: ArrayList<MyNote> = db.getNotes()
                    val adapter = NoteAdapter(this, note)
                    db.addNote(title, textContent)
                    showNotes()
                    adapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(note.size - 1)
                    dialog.dismiss()

                }else{
                    Toast.makeText(this, "Please enter something", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.setCancelable(true)
            dialog.show()
        }

        createNote.setOnClickListener{
            floatBtn.performClick()
        }

        supportActionBar?.hide()
    }

    private fun initval() {
        val recyclerView: RecyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        showNotes()
    }

    fun showNotes(){
        val recyclerView: RecyclerView = findViewById(R.id.recycleView)
        val llout: LinearLayout = findViewById(R.id.llout)
        val db = NoteDataBase(this)
        val note: ArrayList<MyNote> = db.getNotes()

        val adapter = NoteAdapter(this, note)

        if(note.size > 0){
            recyclerView.visibility = View.VISIBLE
            llout.visibility = View.INVISIBLE

            recyclerView.adapter = adapter
        }else{
            recyclerView.visibility = View.INVISIBLE
            llout.visibility = View.VISIBLE
        }
    }

}