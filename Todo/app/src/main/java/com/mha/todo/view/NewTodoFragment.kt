package com.mha.todo.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.mha.todo.service.SendNote
import com.mha.todo.databinding.FragmentNewTodoBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewTodoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentNewTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentNewTodoBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.fabTodolist.setOnClickListener {
            binding.btnNewtodolist.visibility = View.VISIBLE
            binding.edtNote.visibility = View.VISIBLE
            binding.fabTodolist.visibility = View.INVISIBLE
        }
        binding.btnNewtodolist.setOnClickListener {
            sendNote()
            binding.edtNote.setText("")
            binding.btnNewtodolist.visibility = View.INVISIBLE
            binding.edtNote.visibility = View.INVISIBLE
            binding.fabTodolist.visibility = View.VISIBLE
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.btnNewtodolist.windowToken, 0)
        }
        super.onActivityCreated(savedInstanceState)
    }

    private fun sendNote() {
        val sendNote: SendNote = activity as SendNote
        sendNote.note(binding.edtNote.text.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewTodoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}