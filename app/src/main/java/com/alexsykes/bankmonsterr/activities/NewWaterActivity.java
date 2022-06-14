package com.alexsykes.bankmonsterr.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.alexsykes.bankmonsterr.R;

public class NewWaterActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText editWaterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_water);
        editWaterView = findViewById(R.id.edit_water);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if (TextUtils.isEmpty(editWaterView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String water = editWaterView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, water);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}