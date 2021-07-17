package com.yash.toodoo;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.yash.toodoo.database.ToDo;
import com.yash.toodoo.database.ToDoDao;
import com.yash.toodoo.database.ToODoODatabase;

import java.util.List;

public class ToDoRepository {

    private ToDoDao mToDoDao;

    private LiveData<List<ToDo>> mToDos;

    private Application mApplication;

    private List<ToDo> mAllToDo;

    private String mListName;

    public ToDoRepository(Application application, String listName) {
        mApplication = application;
        mListName = listName;

        ToODoODatabase db = ToODoODatabase.getInstance(mApplication);
        mToDoDao = db.toDoDao();

        mToDos = mToDoDao.getLiveToDoOfList(mListName);
    }

    public LiveData<List<ToDo>> getLiveToDoOfList() { return mToDos; }

    public List<ToDo> getAllToDo() {
        mAllToDo = mToDoDao.getToDoOfList(mListName);
        return mAllToDo;
    }

    public void insert(ToDo toDo){
        new insertAsyncTask(mToDoDao, mApplication).execute(toDo);
    }

    public void delete(ToDo toDo) { new deleteAsyncTask(mToDoDao).execute(toDo); }

    private static class insertAsyncTask extends AsyncTask<ToDo, Void, Boolean> {
        private ToDoDao mAsyncToDoDao;
        private Application mApplication;

        insertAsyncTask(ToDoDao dao, Application application) {
            mApplication = application;
            mAsyncToDoDao = dao;
        }

        @Override
        protected Boolean doInBackground(ToDo... toDos) {
            boolean isSuccess = true;
            try {
                mAsyncToDoDao.insert(toDos[0]);
            } catch (SQLiteConstraintException e){
                Log.e("SQLiteInsertError", e.toString());
                isSuccess = false;
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(!result){
                Toast.makeText(mApplication.getApplicationContext(), "Same ToDo already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class deleteAsyncTask extends AsyncTask<ToDo, Void, Void> {
        private ToDoDao mAsyncToDoDao;

        deleteAsyncTask(ToDoDao dao){ mAsyncToDoDao = dao; }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            mAsyncToDoDao.delete(toDos[0]);
            return null;
        }
    }
}
