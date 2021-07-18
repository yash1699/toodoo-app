package com.yash.toodoo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yash.toodoo.adapter.ListAdapter;
import com.yash.toodoo.database.List;
import com.yash.toodoo.model.ListViewModel;
import com.yash.toodoo.model.ToDoViewModel;
import com.yash.toodoo.modelFactory.ToDoViewModelFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListAdapter.ListItemOnLongClickListener {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddNewListFAB;
    private TextView mNoListDisplay;
    private ListAdapter mAdapter;

    private ListViewModel mListViewModel;

    private final int REQUEST_CODE = 0x998;

    private int mFabClickCount = 0;

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
            if(mFabClickCount != 0)
                return;
            mFabClickCount = 1;
            Intent addNewListPopUpIntent = new Intent(MainActivity.this, AddNewListPopUp.class);
            startActivityForResult(addNewListPopUpIntent,REQUEST_CODE);
        });


        mListViewModel = new ViewModelProvider(this).get(ListViewModel.class);

        java.util.List<List> allLists = mListViewModel.getAllLists();

        if(allLists!=null){
            if(allLists.size()!=0){
                ArrayList<String> parsedList = getParsedLists(allLists);
                mAdapter.addAllList(parsedList);
            }
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
        mFabClickCount = 0;
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
    public void onListItemLongClick(View v, String list) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.inflate(R.menu.list_options_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_edit_list_name){
                    showEditPopUp(v, list);
                }
                else {
                    mListViewModel.delete(new List(list));
                    mAdapter.removeList(list);
                    if(mAdapter.getItemCount()==0){
                        mNoListDisplay.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.INVISIBLE);
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void showEditPopUp(View view, String oldListName) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.list_edit_pop_up, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText editListNameEditText = popupView.findViewById(R.id.et_edit_list_name);

        Button cancelEditListName = popupView.findViewById(R.id.bt_cancel_edit_list_name);

        Button addEditListName = popupView.findViewById(R.id.bt_edit_list_name);

        addEditListName.setOnClickListener(v -> {
            String newListName = editListNameEditText.getText().toString();

            if(TextUtils.isEmpty(newListName)) {
                editListNameEditText.setError("Please write a name");
                return;
            }

            boolean result = mListViewModel.update(newListName, oldListName);
            if(result) {
                mAdapter.removeList(oldListName);
            }

            popupWindow.dismiss();
        });

        cancelEditListName.setOnClickListener(v->{
            popupWindow.dismiss();
        });

    }

}