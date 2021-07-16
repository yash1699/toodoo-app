package com.yash.toodoo.database;

import android.content.Context;
import android.provider.SyncStateContract;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {List.class, ToDo.class, Completed.class}, version = 1)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract ListDao listDao();
    public abstract ToDoDao toDoDao();
    public abstract CompletedDao completedDao();

    private static ApplicationDatabase database;

    public static ApplicationDatabase getInstance(Context context){
        if(database == null){
            database = buildDatabaseInstance(context);
        }
        return database;
    }

    private static ApplicationDatabase buildDatabaseInstance(Context context){
        return Room.databaseBuilder(context,
                ApplicationDatabase.class,
                "todo_app").build();
    }

    public void cleanUp(){
        database = null;
    }
}
