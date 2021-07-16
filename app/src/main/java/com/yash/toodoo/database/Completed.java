package com.yash.toodoo.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = List.class, parentColumns = "listName", childColumns = "list_name", onDelete = CASCADE))
public class Completed {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "completed")
    public String completed;

    @ColumnInfo(name = "list_name")
    public String listName;

    public Completed(String completed, String listName){
        this.completed = completed;
        this.listName = listName;
    }
}
