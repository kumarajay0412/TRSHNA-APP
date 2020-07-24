package com.example.tishna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText name;
    EditText age;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

    }
    public void register (View view){
        String Name = name.getText().toString();
        int Age = Integer.parseInt(age.getText().toString());
        String Username = username.getText().toString();
        String Password = password.getText().toString();
        int daily_limit = 10;
//        new registering(Name,Age,Username,Password,daily_limit).execute();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    System.out.println("y");
                    if (success) {
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        Register.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                        builder.setMessage("Register Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(Name, Age, Username, Password, daily_limit, responseListener);

        RequestQueue queue = Volley.newRequestQueue(Register.this);

        queue.add(registerRequest);

    }
}

