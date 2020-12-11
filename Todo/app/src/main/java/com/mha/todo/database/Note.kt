package com.mha.todo.database

import androidx.room.*

@Entity(tableName = "Note")
data class Note(
    @ColumnInfo
    var noteTitle: String,
    @ColumnInfo
    var note: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var noteId: Int? = null,
)
