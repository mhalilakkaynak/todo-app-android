package com.mha.todo.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mha.todo.service.AlarmReceiver
import com.mha.todo.R
import com.mha.todo.service.SendNote
import com.mha.todo.adapter.TodoListAdapter
import com.mha.todo.database.Note
import com.mha.todo.database.NoteDatabase
import com.mha.todo.databinding.ActivityTodolistBinding
import java.util.*
import kotlin.collections.ArrayList

class TodolistActivity : AppCompatActivity(), SendNote {
    private lateinit var binding: ActivityTodolistBinding
    private lateinit var adapter: TodoListAdapter
    private val todoList = ArrayList<String>()
    private var noteList: List<Note>? = null
    private var notePosition: Int? = null
    private var noteId: Int? = null
    private val noteDatabase = NoteDatabase.getNoteDatabase(this).notedao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodolistBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        noteList = noteDatabase.getAllNote()
        notePosition = intent.extras?.get("notePosition") as Int
        if (notePosition != -1) {
            noteId = noteList!![notePosition!!].noteId
            binding.edtTodolistTitle.setText(noteList!![notePosition!!].noteTitle)
            stringToList()
        }
        showRecyclerviewTodolist()
        toolBar()
    }

    private fun stringToList() {
        var tempString = ""
        for (i in noteList!![notePosition!!].note) {
            if (i != '[') {
                if (i != ',' && i != ']') {
                    tempString += i
                } else {
                    if (tempString != " " && tempString != "") {
                        todoList.add(tempString.trim())
                        tempString = ""
                    }
                }
            }
        }
    }

    private fun toolBar() {
        binding.toolbarTodolist.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itemback -> {
                    onBackPressed()
                }
                R.id.itemdelete -> {
                    deleteNote()
                }
                R.id.itemalarm -> {
                    pickerDialog()
                }
                R.id.itemcopy -> {
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Todo list",
                        "${binding.edtTodolistTitle.text}\n${todoList}")
                    clipboard.setPrimaryClip(clip)
                    Snackbar.make(binding.toolbarTodolist,
                        "Copied to clipboard",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun pickerDialog() {
        val calendar = Calendar.getInstance()
        val onTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val calNow = Calendar.getInstance()
            val calSet: Calendar = calNow.clone() as Calendar
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calSet.set(Calendar.MINUTE, minute)
            calSet.set(Calendar.SECOND, 0)
            calSet.set(Calendar.MILLISECOND, 0)
            if (calSet <= calNow) {
                calSet.add(Calendar.DATE, 1)
            }
            binding.tvAlarm.text = "$hourOfDay:$minute"
            setAlarm(calSet)
        }
        val timerPickerDialog = TimePickerDialog(
            this,
            onTimeSetListener,
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            false)
        timerPickerDialog.setTitle("Create alarm")
        timerPickerDialog.show()
    }

    private fun setAlarm(calSet: Calendar) {
        Snackbar.make(binding.toolbarTodolist, "Created in alarm", Snackbar.LENGTH_SHORT).show()
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("notePosition", notePosition)
        intent.putExtra("contentText", binding.edtTodolistTitle.text.toString())
        val pendingIntent =
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.timeInMillis, pendingIntent)
    }


    private fun showRecyclerviewTodolist() {
        adapter = TodoListAdapter(this, todoList)
        binding.recyclerviewTodolist.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewTodolist.setHasFixedSize(true)
        binding.recyclerviewTodolist.adapter = adapter
    }

    override fun note(note: String) {
        todoList.add(note)
        showRecyclerviewTodolist()
    }

    private fun deleteNote() {
        if (notePosition != -1) {
            try {
                noteDatabase.delete(Note(binding.edtTodolistTitle.text.toString(),
                    todoList.toString(),
                    noteId))
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Snackbar.make(binding.toolbarTodolist, "Error", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(binding.toolbarTodolist,
                "Unsaved data cannot be deleted",
                Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updateNote() {
        if (binding.edtTodolistTitle.text.isNotEmpty() || todoList.isEmpty()) {
            noteDatabase.update(Note(binding.edtTodolistTitle.text.toString(),
                todoList.toString(), noteId))
        } else {
            Toast.makeText(this, "Could not create note", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertNote() {
        if (binding.edtTodolistTitle.text.isNotEmpty() || todoList.isNotEmpty()) {
            noteDatabase.insert(Note(binding.edtTodolistTitle.text.toString(), todoList.toString()))
        } else {
            Toast.makeText(this, "Could not create note", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (notePosition == -1) {
            insertNote()
        } else {
            updateNote()
        }
        startActivity(Intent(this, MainActivity::class.java))
    }
}