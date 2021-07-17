package com.yash.toodoo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {List.class, ToDo.class, Completed.class}, version = 3)
public abstract class ToODoODatabase extends RoomDatabase {

    public abstract ListDao listDao();
    public abstract ToDoDao toDoDao();
    public abstract CompletedDao completedDao();

    private static ToODoODatabase INSTANCE;

    public static ToODoODatabase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (ToODoODatabase.class){
                if (INSTANCE == null){
                    INSTANCE = buildDatabaseInstance(context);
                }
            }
        }
        return INSTANCE;
    }

    private static ToODoODatabase buildDatabaseInstance(Context context){
        return Room.databaseBuilder(context,
                ToODoODatabase.class,
                "todo_app")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
