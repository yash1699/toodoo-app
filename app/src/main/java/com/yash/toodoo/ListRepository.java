package com.yash.toodoo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.yash.toodoo.database.ListDao;
import com.yash.toodoo.database.ToODoODatabase;

import java.util.List;

public class ListRepository {

    private ListDao mListDao;

    private LiveData<List<com.yash.toodoo.database.List>> mAllList;

    public ListRepository(Application application){
        ToODoODatabase db = ToODoODatabase.getInstance(application);

        mListDao = db.listDao();

        mAllList = mListDao.getAllList();
    }

    public LiveData<List<com.yash.toodoo.database.List>> getAllList(){
        return mAllList;
    }

    public void insert(com.yash.toodoo.database.List list){
        new insertAsyncTask(mListDao).execute(list);
    }

    public List<com.yash.toodoo.database.List> getAllLists(){
        return mListDao.getAllLists();
    }

    private static class insertAsyncTask extends AsyncTask<com.yash.toodoo.database.List,Void, Void>{
        private ListDao mAsyncListDao;

        insertAsyncTask(ListDao dao){
            mAsyncListDao = dao;
        }

        @Override
        protected Void doInBackground(com.yash.toodoo.database.List... lists) {
            mAsyncListDao.insert(lists[0]);
            return null;
        }
    }

}
