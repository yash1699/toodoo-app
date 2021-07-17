package com.yash.toodoo.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yash.toodoo.CompletedRepository;
import com.yash.toodoo.database.Completed;

import java.util.List;

public class CompletedViewModel extends AndroidViewModel {

    private CompletedRepository mRepository;

    private LiveData<List<Completed>> mCompleted;

    public CompletedViewModel(Application application, String listName) {
        super(application);

        mRepository = new CompletedRepository(application, listName);

        mCompleted = mRepository.getLiveCompletedOfList();
    }

    public LiveData<List<Completed>> getLiveCompletedOfList() { return mCompleted; }

    public List<Completed> getAllCompleted() { return mRepository.getAllCompleted(); }

    public void insert(Completed completed) { mRepository.insert(completed); }

    public void delete(Completed completed) { mRepository.delete(completed); }

}
