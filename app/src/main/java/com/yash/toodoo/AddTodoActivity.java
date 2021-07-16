package com.yash.toodoo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.yash.toodoo.adapter.CompletedTodoListAdapter;
import com.yash.toodoo.adapter.TodoListAdapter;

public class AddTodoActivity extends AppCompatActivity implements TodoListAdapter.TodoListItemClickListener, CompletedTodoListAdapter.CompletedTodoListItemClickListener {

    private TextView mTodoLabelDisplay;
    private RecyclerView mTodoList;

    private TextView mCompletedTodoLabelDisplay;
    private RecyclerView mCompletedTodoList;

    private EditText mAddTodoEditText;
    private TextView mAddTodoButton;

    private TodoListAdapter mTodoListAdapter;

    private CompletedTodoListAdapter mCompletedTodoListAdapter;

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

        mAddTodoButton.setOnClickListener(v -> {
            String todo = mAddTodoEditText.getText().toString();
            if(TextUtils.isEmpty(todo)){
                mAddTodoEditText.setError("Please write a todo to add.");
                return;
            }

            mTodoListAdapter.addNewTodo(todo);
            if(mTodoList.getVisibility() == View.INVISIBLE){
                mTodoLabelDisplay.setVisibility(View.VISIBLE);
                mTodoList.setVisibility(View.VISIBLE);
            }
        });
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            actionBar.setTitle(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClickTodoListItem(String todo) {
        mTodoListAdapter.completeTodo(todo);

        mCompletedTodoListAdapter.completeTodo(todo);

        if(mCompletedTodoList.getVisibility() == View.INVISIBLE){
            mCompletedTodoLabelDisplay.setVisibility(View.VISIBLE);
            mCompletedTodoList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickCompletedTodoListItem(String completedTodo) {
        mCompletedTodoListAdapter.removeCompletedTodo(completedTodo);
        mTodoListAdapter.addNewTodo(completedTodo);
    }
}