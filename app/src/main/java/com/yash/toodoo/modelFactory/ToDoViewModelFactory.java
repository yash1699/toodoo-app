package com.yash.toodoo.modelFactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yash.toodoo.model.ToDoViewModel;

public class ToDoViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;

    private String mListName;

    public ToDoViewModelFactory(Application application, String listName) {
        mApplication = application;
        mListName = listName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ToDoViewModel(mApplication, mListName);
    }
}
