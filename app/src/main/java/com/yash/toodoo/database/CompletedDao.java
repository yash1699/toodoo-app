package com.yash.toodoo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompletedDao {

    @Query("Select * from Completed where list_name = :listName")
    LiveData<List<Completed>> getLiveCompletedOfList(String listName);

    @Query("Select * from Completed where list_name = :listName")
    List<Completed> getCompletedOfList(String listName);

    @Insert
    void insert(Completed completed);

    @Delete
    void delete(Completed completed);
}
