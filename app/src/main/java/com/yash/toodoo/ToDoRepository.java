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
    
    public boolean updateToDo(String oldToDo, String newToDo) {
        updateAsyncTask task = new updateAsyncTask(mToDoDao, mApplication);
        task.execute(new String[]{mListName, oldToDo, newToDo});
        return task.getResult();
    }

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
    
    private static class updateAsyncTask extends AsyncTask<String[], Void, Boolean> {
        private ToDoDao mAsyncToDoDao;
        private Application mApplication;
        private boolean mResult = true;
        
        updateAsyncTask(ToDoDao dao, Application application) {
            mAsyncToDoDao = dao;
            mApplication = application;
        }

        @Override
        protected Boolean doInBackground(String[]... strings) {
            boolean isSuccess = true;
            try {
                mAsyncToDoDao.updateToDo(strings[0][0], strings[0][1], strings[0][2]);
            } catch (SQLiteConstraintException e) {
                isSuccess = false;
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(!result) {
                mResult = false;
            }
        }
        
        protected boolean getResult() {
            return mResult;
        }
    }
}
