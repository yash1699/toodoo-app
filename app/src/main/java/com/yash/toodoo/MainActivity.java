package com.yash.toodoo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yash.toodoo.adapter.ListAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddNewListFAB;
    private TextView mNoListDisplay;
    private ListAdapter mAdapter;

    private final int REQUEST_CODE = 0x998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_list);
        mNoListDisplay = findViewById(R.id.tv_no_list);
        mAddNewListFAB = findViewById(R.id.fab_add_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter.getItemCount() != 0){
            mNoListDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        mAddNewListFAB.setOnClickListener(v -> {
            Intent addNewListPopUpIntent = new Intent(MainActivity.this, AddNewListPopUp.class);
            startActivityForResult(addNewListPopUpIntent,REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                if(data.hasExtra(Intent.EXTRA_TEXT)){
                    String list = data.getStringExtra(Intent.EXTRA_TEXT);
                    mAdapter.addNewList(list);
                }
            }
        }

        if(mAdapter.getItemCount() != 0){
            mNoListDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}