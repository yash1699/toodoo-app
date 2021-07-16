package com.yash.toodoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yash.toodoo.adapter.ListAdapter;
import com.yash.toodoo.database.List;
import com.yash.toodoo.model.ListViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListAdapter.ListItemOnLongClickListener {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddNewListFAB;
    private TextView mNoListDisplay;
    private ListAdapter mAdapter;

    private ListViewModel mListViewModel;

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

        mAdapter = new ListAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter.getItemCount() != 0){
            showRecyclerView();
        }

        mAddNewListFAB.setOnClickListener(v -> {
            Intent addNewListPopUpIntent = new Intent(MainActivity.this, AddNewListPopUp.class);
            startActivityForResult(addNewListPopUpIntent,REQUEST_CODE);
        });


        mListViewModel = new ViewModelProvider(this).get(ListViewModel.class);

        java.util.List<List> allLists = mListViewModel.getAllLists();

        if(allLists.size()!=0){
            ArrayList<String> parsedList = getParsedLists(allLists);
            mAdapter.addAllList(parsedList);
        }

        mListViewModel.getAllList().observe(this, new Observer<java.util.List<List>>() {
            @Override
            public void onChanged(java.util.List<List> lists) {
                mAdapter.addAllList(getParsedLists(lists));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAdapter.getItemCount()!=0){
            showRecyclerView();
        }
    }

    private ArrayList<String> getParsedLists(java.util.List<List> lists){
        ArrayList<String> parsedList = new ArrayList<>();
        for (List list: lists){
            parsedList.add(list.listName);
        }
        return parsedList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                if (data != null){
                    if(data.hasExtra(Intent.EXTRA_TEXT)){
                        String list = data.getStringExtra(Intent.EXTRA_TEXT);
                        mListViewModel.insert(new List(list));
                        mAdapter.addNewList(list);
                        if(mRecyclerView.getVisibility() == View.INVISIBLE){
                            showRecyclerView();
                        }
                    }
                }
            }
        }

        if(mAdapter.getItemCount() != 0){
            showRecyclerView();
        }
    }

    private void showRecyclerView(){
        mNoListDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemLongClick(String list) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        mListViewModel.delete(new List(list));
        mAdapter.removeList(list);
        if(mAdapter.getItemCount()==0){
            mNoListDisplay.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}