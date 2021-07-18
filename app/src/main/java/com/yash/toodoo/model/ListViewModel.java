package com.yash.toodoo.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yash.toodoo.ListRepository;

import java.util.List;

public class ListViewModel extends AndroidViewModel {

    private ListRepository mRepository;

    private LiveData<List<com.yash.toodoo.database.List>> mAllList;

    public ListViewModel(Application application) {
        super(application);

        mRepository = new ListRepository(application);

        mAllList = mRepository.getAllList();
    }

    public LiveData<List<com.yash.toodoo.database.List>> getAllList() { return mAllList; }

    public List<com.yash.toodoo.database.List> getAllLists() { return mRepository.getAllLists(); }

    public void insert(com.yash.toodoo.database.List list) { mRepository.insert(list); }

    public void delete(com.yash.toodoo.database.List list) { mRepository.delete(list); }

    public boolean update(String newListName, String oldListName) { return mRepository.update(newListName, oldListName); }

}
