package com.yash.toodoo.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = List.class, parentColumns = "listName", childColumns = "list_name", onDelete = CASCADE))
public class ToDo {

    @NonNull
    @PrimaryKey
    public String todo;

    @ColumnInfo(name = "list_name")
    public String listName;

    public ToDo(String listName, String todo){
        this.listName = listName;
        this.todo = todo;
    }



}
