package com.yash.toodoo.modelFactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yash.toodoo.model.CompletedViewModel;

public class CompletedViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;

    private String mListName;

    public CompletedViewModelFactory(Application application, String listName) {
        mApplication = application;
        mListName = listName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CompletedViewModel(mApplication, mListName);
    }
}
