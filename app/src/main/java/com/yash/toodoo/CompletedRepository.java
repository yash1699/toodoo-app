package com.yash.toodoo;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.yash.toodoo.database.Completed;
import com.yash.toodoo.database.CompletedDao;
import com.yash.toodoo.database.ToODoODatabase;

import java.util.List;

public class CompletedRepository {
    private CompletedDao mCompletedDao;

    private Application mApplication;

    private LiveData<List<Completed>> mCompleted;

    private List<Completed> mAllCompleted;

    private String mListName;

    public CompletedRepository(Application application, String listName) {
        mApplication = application;
        mListName = listName;

        ToODoODatabase db = ToODoODatabase.getInstance(mApplication);
        mCompletedDao = db.completedDao();

        mCompleted = mCompletedDao.getLiveCompletedOfList(mListName);
    }

    public LiveData<List<Completed>> getLiveCompletedOfList() { return mCompleted; }

    public List<Completed> getAllCompleted() {
        mAllCompleted = mCompletedDao.getCompletedOfList(mListName);
        return mAllCompleted;
    }

    public void insert(Completed completed) { new insertAsyncTask(mCompletedDao).execute(completed); }

    public void delete(Completed completed) { new deleteAsyncTask(mCompletedDao).execute(completed); }

    private static class insertAsyncTask extends AsyncTask<Completed, Void, Void> {
        private CompletedDao mAsyncCompletedDao;

        insertAsyncTask(CompletedDao dao) {
            mAsyncCompletedDao = dao;
        }

        @Override
        protected Void doInBackground(Completed... completeds) {
            try {
                mAsyncCompletedDao.insert(completeds[0]);
            } catch (SQLiteConstraintException e) {
                Log.d("SQLiteInsertError", "Completed Error");
            }
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Completed, Void, Void> {
        private CompletedDao mAsyncCompletedDao;

        deleteAsyncTask(CompletedDao dao) {
            mAsyncCompletedDao = dao;
        }

        @Override
        protected Void doInBackground(Completed... completeds) {

            mAsyncCompletedDao.delete(completeds[0]);
            return null;
        }
    }
}
