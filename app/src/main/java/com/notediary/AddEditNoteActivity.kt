package com.notediary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {

    lateinit var etNoteTitle: EditText
    lateinit var etNoteDescription: EditText
    lateinit var btnUpdate: Button
    lateinit var viewModel: NoteViewModel
    var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        etNoteTitle = findViewById(R.id.etNoteTitle)
        etNoteDescription = findViewById(R.id.etNoteDescription)
        btnUpdate = findViewById(R.id.btnUpdate)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteViewModel::class.java]

        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDesc = intent.getStringExtra("noteDescription")
            noteId = intent.getIntExtra("noteID", -1)
            btnUpdate.text = "Update Note"
            etNoteTitle.setText(noteTitle)
            etNoteDescription.setText(noteDesc)
        } else {
            btnUpdate.text = "Save Note"
        }

        btnUpdate.setOnClickListener {
            val noteTitle = etNoteTitle.text.toString()
            val noteDesc = etNoteDescription.text.toString()
            if (noteType.equals("Edit")) {
                if (noteTitle.isNotEmpty() && noteDesc.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
                    val currentDate: String = sdf.format(Date())
                    val updatedNote = Note(noteTitle, noteDesc, currentDate)
                    updatedNote.id = noteId
                    viewModel.updateNote(updatedNote)
                    Toast.makeText(this@AddEditNoteActivity, "Note Updated", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDesc.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
                    val currentDate: String = sdf.format(Date())
                    viewModel.addNote(Note(noteTitle, noteDesc, currentDate))
                    Toast.makeText(this@AddEditNoteActivity, "Note Added..", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}