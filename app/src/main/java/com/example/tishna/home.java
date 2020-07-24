package com.example.tishna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class home extends AppCompatActivity {
    Bundle info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       info = getIntent().getExtras();

    }

    public void account(View view) {
        Intent account = new Intent(home.this, account.class);
        account.putExtras(info);
        startActivity(account);
    }

    public void start(View view) {
        Intent start = new Intent(home.this,bluetooth.class);
        start.putExtras(info);
        startActivity(start);
    }

    public void bathing(View view) {
    }

}
