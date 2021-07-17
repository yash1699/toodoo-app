package com.yash.toodoo;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.yash.toodoo.database.ListDao;
import com.yash.toodoo.database.ToODoODatabase;

import java.util.List;

public class ListRepository {

    private ListDao mListDao;

    private LiveData<List<com.yash.toodoo.database.List>> mAllList;

    private Application mApplication;

    private List<com.yash.toodoo.database.List> mAllLists;

    public ListRepository(Application application){
        mApplication = application;
        ToODoODatabase db = ToODoODatabase.getInstance(mApplication);
        mListDao = db.listDao();

        mAllList = mListDao.getAllList();
    }

    public LiveData<List<com.yash.toodoo.database.List>> getAllList(){
        return mAllList;
    }

    public void insert(com.yash.toodoo.database.List list) {
        new insertAsyncTask(mListDao, mApplication).execute(list);
    }

    public void delete(com.yash.toodoo.database.List list) {
        new deleteAsyncTask(mListDao).execute(list);
    }

    public List<com.yash.toodoo.database.List> getAllLists(){
        mAllLists = mListDao.getAllLists();
        return mAllLists;
    }

    private static class insertAsyncTask extends AsyncTask<com.yash.toodoo.database.List,Void, Boolean> {
        private ListDao mAsyncListDao;

        private Application mApplication;

        insertAsyncTask(ListDao dao, Application application){
            mApplication = application;
            mAsyncListDao = dao;
        }

        @Override
        protected Boolean doInBackground(com.yash.toodoo.database.List... lists) {
            boolean isSuccess = true;
            try {
                mAsyncListDao.insert(lists[0]);
            } catch (SQLiteConstraintException exception){
                Log.e("SQLiteInsertError" , exception.toString());
                isSuccess = false;
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(!result){
                Toast.makeText(mApplication.getApplicationContext(), "List with same name already exist.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class deleteAsyncTask extends AsyncTask<com.yash.toodoo.database.List, Void, Void> {
        private ListDao mAsyncListDao;

        deleteAsyncTask(ListDao dao) { mAsyncListDao = dao; }

        @Override
        protected Void doInBackground(com.yash.toodoo.database.List... lists) {
            mAsyncListDao.delete(lists[0]);
            return null;
        }
    }
}
