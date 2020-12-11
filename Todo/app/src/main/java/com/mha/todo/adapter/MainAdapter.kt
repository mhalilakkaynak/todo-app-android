package com.mha.todo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mha.todo.R
import com.mha.todo.database.Note
import com.mha.todo.view.TodolistActivity

class MainAdapter(private val context: Context, private val noteList: ArrayList<Note>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTodoTitle: TextView = view.findViewById(R.id.tvTodoTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_main, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTodoTitle.text = noteList[position].noteTitle
        holder.tvTodoTitle.setOnClickListener {
            val intent = Intent(context, TodolistActivity::class.java)
            intent.putExtra("notePosition", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}