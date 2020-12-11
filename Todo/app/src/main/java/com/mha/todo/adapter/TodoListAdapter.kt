package com.mha.todo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mha.todo.R

@Suppress("DEPRECATION")
class TodoListAdapter(private val context: Context, private val todolist: ArrayList<String>) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkboxTodolist: CheckBox = view.findViewById(R.id.checkboxTodolist)
        val animationCongratulation: com.airbnb.lottie.LottieAnimationView =
            view.findViewById(R.id.animationCongratulation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_todolist, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkboxTodolist.text = todolist[position]
        holder.checkboxTodolist.setOnClickListener {
            if (holder.checkboxTodolist.isChecked) {
                todolist[position] = ""
                holder.animationCongratulation.visibility = View.VISIBLE
                holder.checkboxTodolist.visibility = View.INVISIBLE
                holder.animationCongratulation.playAnimation()
                android.os.Handler().postDelayed({
                    holder.animationCongratulation.resumeAnimation()
                }, 700)
            }
        }
    }

    override fun getItemCount(): Int {
        return todolist.size
    }
}