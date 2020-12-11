package com.mha.todo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mha.todo.adapter.MainAdapter
import com.mha.todo.database.Note
import com.mha.todo.database.NoteDatabase
import com.mha.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private lateinit var noteList: List<Note>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val notePosition: Int? = intent?.extras?.get("notePosition") as Int?
        if (notePosition != null) {
            val intent2 = Intent(this, TodolistActivity::class.java)
            intent2.putExtra("notePosition", notePosition)
            startActivity(intent2)
        }
        binding.fabMain.setOnClickListener {
            val intent = Intent(this, TodolistActivity::class.java)
            intent.putExtra("notePosition", -1)
            startActivity(intent)
        }
        val noteDatabase = NoteDatabase.getNoteDatabase(this).notedao()
        noteList = noteDatabase.getAllNote()
        showRecyclerviewMain()
    }

    private fun showRecyclerviewMain() {
        binding.recyclerviewMain.setHasFixedSize(true)
        binding.recyclerviewMain.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(this, noteList as ArrayList<Note>)
        binding.recyclerviewMain.adapter = adapter
    }


    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


}