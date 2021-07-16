package com.yash.toodoo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListDao {

    @Query("SELECT listName FROM List")
    LiveData<List<com.yash.toodoo.database.List>> getAllList();

    @Query("SELECT listName FROM List")
    List<com.yash.toodoo.database.List> getAllLists();

    @Insert
    void insert(com.yash.toodoo.database.List list);

    @Delete
    void delete(com.yash.toodoo.database.List list);
}
