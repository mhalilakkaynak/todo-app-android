package com.mha.todo.database

import androidx.room.*

@Dao
interface Notedao {
    @Insert
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM Note")
    fun getAllNote(): List<Note>

    @Update
    fun update(note: Note)
}