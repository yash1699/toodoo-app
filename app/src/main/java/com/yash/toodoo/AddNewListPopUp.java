package com.yash.toodoo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewListPopUp extends Activity {

    private EditText mNewListName;
    private Button mAddNewListButton;
    private Button mCancelNewListButton;

    private String LIST_NAME="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_list_pop_up);

        mNewListName = findViewById(R.id.et_new_list_name);
        mAddNewListButton = findViewById(R.id.bt_add_new_list);
        mCancelNewListButton = findViewById(R.id.bt_cancel_new_list);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getDisplay().getRealMetrics(displayMetrics);

        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        getWindow().setLayout((int)(deviceWidth*0.8),(int)(deviceHeight*0.3));

        mAddNewListButton.setOnClickListener(v -> {
            LIST_NAME = mNewListName.getText().toString();
            if(TextUtils.isEmpty(LIST_NAME)){
                mNewListName.setError("Please write a name of the list");
                return;
            }
            Intent mainActivityIntent = new Intent(AddNewListPopUp.this, MainActivity.class);
            mainActivityIntent.putExtra(Intent.EXTRA_TEXT, LIST_NAME);
            setResult(Activity.RESULT_OK,mainActivityIntent);
            finish();
        });

        mCancelNewListButton.setOnClickListener
                (v -> {
                    setResult(Activity.RESULT_OK);
                    finish();
                });
    }
}