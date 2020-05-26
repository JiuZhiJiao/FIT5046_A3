package com.example.mymoviememoir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // Get UI
        EditText etUsername = findViewById(R.id.sign_in_et_username);
        EditText etPassword = findViewById(R.id.sign_in_et_password);
        Button btnSignIn = findViewById(R.id.sign_in_bt_signin);
        Button btnSignUp = findViewById(R.id.sign_in_bt_signup);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Turn to the MainActivity to release Home Screen
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
