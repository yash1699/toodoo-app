package com.yash.toodoo.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class List {

    @NonNull
    @PrimaryKey
    public String listName;

    public List(@NonNull String listName){
        this.listName = listName;
    }
}
