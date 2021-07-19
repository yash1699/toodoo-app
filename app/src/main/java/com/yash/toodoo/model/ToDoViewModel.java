package com.yash.toodoo.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yash.toodoo.ToDoRepository;
import com.yash.toodoo.database.ToDo;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    private ToDoRepository mRepository;

    private LiveData<List<ToDo>> mToDos;

    public ToDoViewModel(Application application, String listName) {
        super(application);

        mRepository = new ToDoRepository(application, listName);

        mToDos = mRepository.getLiveToDoOfList();
    }

    public LiveData<List<ToDo>> getLiveToDoOfList() { return mToDos; }

    public List<ToDo> getAllToDo() { return  mRepository.getAllToDo(); }

    public void insert(ToDo toDo) { mRepository.insert(toDo); }

    public void delete(ToDo toDo) { mRepository.delete(toDo); }

    public boolean updateToDO(String oldToDo, String newToDo) { return mRepository.updateToDo(oldToDo, newToDo); }

}
