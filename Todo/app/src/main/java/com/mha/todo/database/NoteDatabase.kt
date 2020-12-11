package com.mha.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun notedao(): Notedao

    companion object {
        private var instance: NoteDatabase? = null
        fun getNoteDatabase(context: Context): NoteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, NoteDatabase::class.java, "Note")
                    .allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}