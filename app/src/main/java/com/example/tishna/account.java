package com.example.tishna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;

public class account extends AppCompatActivity {
    TextView name , age , daily_limit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        name = (TextView)findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        daily_limit = (TextView)findViewById(R.id.dailyLimit);

        Bundle info = getIntent().getExtras();

        name.setText(info.getString("name"));
        age.setText(String.valueOf(info.getInt("age")));
        daily_limit.setText(String.valueOf(info.getInt("daily_limit")));

    }

    public void logout(View view) {
        Intent i = new Intent(account.this,MainActivity.class);
        startActivity(i);
    }
}

