package com.example.notes

import MyNote
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDataBase(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME , null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "note_database"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "note"
        private const val NOTE_ID = "id"
        private const val TITLE = "title"
        private const val CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $TITLE TEXT,
                $CONTENT TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        if (p1 < p2) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    fun updateNote(note: MyNote, noteId: Int , oldTitle : String, oldContent : String){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(TITLE, note.title)
        values.put(CONTENT, note.textContent)
//        values.put(NOTE_ID, note.id)

//        val whereClause = "$TITLE = ? AND $CONTENT = ? AND $NOTE_ID = ? "
//        val whereArgs = arrayOf(oldTitle, oldContent , noteId.toString())

        val whereClause = "$NOTE_ID = ? AND $TITLE = ? AND $CONTENT = ?"
        val whereArgs = arrayOf(noteId.toString(), oldTitle, oldContent)

        db.update(TABLE_NAME, values, whereClause, whereArgs)
    }

    fun addNote(title: String,  textContent: String) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(TITLE, title)
        values.put(CONTENT, textContent)

        db.insert(TABLE_NAME, null, values)
    }

    fun deleteNote(noteId :Int, title: String, content:String){
//    fun deleteNote(title: String, content:String){
        val db = this.writableDatabase

        val whereClause = "$NOTE_ID = ? AND $TITLE =? AND $CONTENT =?"
        val whereArgs = arrayOf(noteId.toString(), title, content)
        db.delete(TABLE_NAME, whereClause, whereArgs)

//        val whereClause = "$TITLE =? AND $CONTENT =?"
//        val whereArgs = arrayOf(title, content)
//        db.delete(TABLE_NAME, whereClause, whereArgs)
    }

    @SuppressLint("Range")
    fun getNotes(): ArrayList<MyNote> {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        val Notes = ArrayList<MyNote>()

        while (cursor.moveToNext()) {
            val model = MyNote(
                cursor.getString(cursor.getColumnIndex(TITLE)),  // Replace with the correct column index
                cursor.getString(cursor.getColumnIndex(CONTENT)), // Replace with the correct column index
                cursor.getInt(cursor.getColumnIndex(NOTE_ID)),
            )
            Notes.add(model)
        }

        cursor.close()
        db.close()

        return Notes
    }
}