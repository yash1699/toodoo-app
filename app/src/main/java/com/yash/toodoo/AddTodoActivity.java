package com.yash.toodoo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yash.toodoo.adapter.CompletedTodoListAdapter;
import com.yash.toodoo.adapter.TodoListAdapter;
import com.yash.toodoo.database.Completed;
import com.yash.toodoo.database.ToDo;
import com.yash.toodoo.model.CompletedViewModel;
import com.yash.toodoo.model.ToDoViewModel;
import com.yash.toodoo.modelFactory.CompletedViewModelFactory;
import com.yash.toodoo.modelFactory.ToDoViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class AddTodoActivity extends AppCompatActivity implements TodoListAdapter.TodoListItemClickListener, CompletedTodoListAdapter.CompletedTodoListItemClickListener {

    private TextView mTodoLabelDisplay;
    private RecyclerView mTodoList;

    private TextView mCompletedTodoLabelDisplay;
    private RecyclerView mCompletedTodoList;

    private EditText mAddTodoEditText;
    private TextView mAddTodoButton;

    private TodoListAdapter mTodoListAdapter;

    private CompletedTodoListAdapter mCompletedTodoListAdapter;

    private ToDoViewModel mToDoViewModel;

    private CompletedViewModel mCompletedViewModel;

    private String mListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        mTodoLabelDisplay = findViewById(R.id.tv_todo_label);
        mTodoList = findViewById(R.id.rv_todo_list);

        mCompletedTodoLabelDisplay = findViewById(R.id.tv_completed_todo_label);
        mCompletedTodoList = findViewById(R.id.rv_completed_todo_list);

        mAddTodoEditText = findViewById(R.id.et_add_todo);

        mAddTodoButton = findViewById(R.id.bt_add_todo);

        LinearLayoutManager todoListLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mTodoList.setLayoutManager(todoListLayoutManager);

        mTodoListAdapter = new TodoListAdapter(this);
        mTodoList.setAdapter(mTodoListAdapter);

        LinearLayoutManager completedTodoListLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mCompletedTodoList.setLayoutManager(completedTodoListLayoutManager);

        mCompletedTodoListAdapter = new CompletedTodoListAdapter(this);
        mCompletedTodoList.setAdapter(mCompletedTodoListAdapter);

        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            mListName = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        actionBar.setTitle(mListName);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mToDoViewModel = new ViewModelProvider(this, new ToDoViewModelFactory(this.getApplication(), mListName))
                .get(ToDoViewModel.class);
        loadTodo();

        mCompletedViewModel = new ViewModelProvider(this, new CompletedViewModelFactory(this.getApplication(), mListName))
                .get(CompletedViewModel.class);
        loadCompleted();

        mAddTodoButton.setOnClickListener(v -> {
            String todo = mAddTodoEditText.getText().toString();
            if(TextUtils.isEmpty(todo)){
                mAddTodoEditText.setError("Please write a todo to add.");
                return;
            }

            ToDo toDo = new ToDo(mListName, todo);
            mToDoViewModel.insert(toDo);
            mAddTodoEditText.setText("");
            hideKeyboard();
            mTodoListAdapter.addNewTodo(todo);
            if(mTodoList.getVisibility() == View.INVISIBLE){
                mTodoLabelDisplay.setVisibility(View.VISIBLE);
                mTodoList.setVisibility(View.VISIBLE);
            }
        });

        mToDoViewModel.getLiveToDoOfList().observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                mTodoListAdapter.addAllToDo(getParsedToDo(toDos));
            }
        });

        mCompletedViewModel.getLiveCompletedOfList().observe(this, new Observer<List<Completed>>() {
            @Override
            public void onChanged(List<Completed> completeds) {
                mCompletedTodoListAdapter.completeAllTodo(getParsedCompleted(completeds));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mTodoListAdapter.getItemCount() != 0){
            showToDoRecyclerView();
        }
        if(mCompletedTodoListAdapter.getItemCount() != 0){
            showCompletedRecyclerView();
        }
    }

    @Override
    public void onClickTodoListItem(String todo) {
        mCompletedViewModel.insert(new Completed(mListName,todo));
        mToDoViewModel.delete(new ToDo(mListName, todo));

        mTodoListAdapter.completeTodo(todo);
        mCompletedTodoListAdapter.completeTodo(todo);

        if(mTodoListAdapter.getItemCount() == 0){
            hideToDoRecyclerView();
        }

        if(mCompletedTodoList.getVisibility() == View.INVISIBLE){
            showCompletedRecyclerView();
        }
    }

    @Override
    public void onClickCompletedTodoListItem(String completedTodo) {
        mCompletedViewModel.delete(new Completed(mListName, completedTodo));
        mToDoViewModel.insert(new ToDo(mListName, completedTodo));

        mTodoListAdapter.addNewTodo(completedTodo);
        mCompletedTodoListAdapter.removeCompletedTodo(completedTodo);

        if(mCompletedTodoListAdapter.getItemCount() == 0){
            hideCompletedRecyclerView();
        }

        if(mTodoList.getVisibility() == View.INVISIBLE){
            showToDoRecyclerView();
        }
    }

    @Override
    public void onLongClickCompletedTodoListItem(String completedTodo) {
        mCompletedViewModel.delete(new Completed(mListName, completedTodo));
        mCompletedTodoListAdapter.removeCompletedTodo(completedTodo);

        if(mCompletedTodoListAdapter.getItemCount() == 0) {
            hideCompletedRecyclerView();
        }
    }

    @Override
    public void onLongClickTodoListItem(View view, String todo) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.options_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_edit) {
                shopEditPopUp(view, todo);
            } else {
                mToDoViewModel.delete(new ToDo(mListName, todo));
                mTodoListAdapter.completeTodo(todo);

                if(mTodoListAdapter.getItemCount() == 0) {
                    hideToDoRecyclerView();
                }
            }
            return true;
        });
        popupMenu.show();
    }

    private void shopEditPopUp(View view, String oldToDo) {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.edit_pop_up, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        EditText editTodo = popupView.findViewById(R.id.et_edit);
        Button addEdit = popupView.findViewById(R.id.bt_edit);
        Button cancelEdit = popupView.findViewById(R.id.bt_cancel_edit);
        editTodo.setHint("New Todo");

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        addEdit.setOnClickListener(v -> {
            String newToDo = editTodo.getText().toString();
            if(TextUtils.isEmpty(newToDo)) {
                editTodo.setError("Please write a todo.");
                return;
            }

            boolean result = mToDoViewModel.updateToDO(oldToDo, newToDo);
            if(result) {
                mTodoListAdapter.completeTodo(oldToDo);
            }
            popupWindow.dismiss();
        });

        cancelEdit.setOnClickListener(v -> popupWindow.dismiss());
    }

    private void loadTodo(){
        List<ToDo> todos = mToDoViewModel.getAllToDo();

        if(todos != null){
            if(todos.size() != 0){
                mTodoListAdapter.addAllToDo(getParsedToDo(todos));
            }
        }
    }

    private void loadCompleted() {
        List<Completed> completeds = mCompletedViewModel.getAllCompleted();

        if(completeds != null) {
            if(completeds.size() != 0) {
                mCompletedTodoListAdapter.completeAllTodo(getParsedCompleted(completeds));
            }
        }
    }

    private void showToDoRecyclerView(){
        mTodoLabelDisplay.setVisibility(View.VISIBLE);
        mTodoList.setVisibility(View.VISIBLE);
    }

    private void showCompletedRecyclerView() {
        mCompletedTodoLabelDisplay.setVisibility(View.VISIBLE);
        mCompletedTodoList.setVisibility(View.VISIBLE);
    }

    private void hideToDoRecyclerView(){
        mTodoLabelDisplay.setVisibility(View.INVISIBLE);
        mTodoList.setVisibility(View.INVISIBLE);
    }

    private void hideCompletedRecyclerView(){
        mCompletedTodoLabelDisplay.setVisibility(View.INVISIBLE);
        mCompletedTodoList.setVisibility(View.INVISIBLE);
    }

    private ArrayList<String> getParsedToDo(List<ToDo> toDos){
        ArrayList<String> parsedTodos = new ArrayList<>();
        for(ToDo toDo: toDos){
            parsedTodos.add(toDo.todo);
        }
        return parsedTodos;
    }

    private ArrayList<String> getParsedCompleted(List<Completed> completeds) {
        ArrayList<String> parsedCompleteds = new ArrayList<>();
        for(Completed completed: completeds) {
            parsedCompleteds.add(completed.completed);
        }
        return parsedCompleteds;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}