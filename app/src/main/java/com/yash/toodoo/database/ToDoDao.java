package com.yash.toodoo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ToDoDao {

    @Query("Select * from ToDo where list_name = :listName")
    LiveData<List<ToDo>> getLiveToDoOfList(String listName);

    @Query("Select * from ToDo where list_name = :listName")
    List<ToDo> getToDoOfList(String listName);

    @Insert
    void insert(ToDo toDo);

    @Delete
    void delete(ToDo toDo);
}
