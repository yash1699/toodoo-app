package com.yash.toodoo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListDao {

    @Query("SELECT listName FROM List")
    List<String> getAll();

    @Insert
    void insert(com.yash.toodoo.database.List list);

    @Delete
    void delete(com.yash.toodoo.database.List list);
}
