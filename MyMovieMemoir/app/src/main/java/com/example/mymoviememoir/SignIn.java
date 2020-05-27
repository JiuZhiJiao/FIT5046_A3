package com.example.mymoviememoir;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.network.OKHttpConnection;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignIn extends AppCompatActivity {

    OKHttpConnection okHttpConnection = null;
    String username = "";
    String password = "";
    String result = "";
    Credential credential = new Credential();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        okHttpConnection = new OKHttpConnection();

        // Get UI
        final EditText etUsername = findViewById(R.id.sign_in_et_username);
        final EditText etPassword = findViewById(R.id.sign_in_et_password);
        Button btnSignIn = findViewById(R.id.sign_in_bt_signin);
        Button btnSignUp = findViewById(R.id.sign_in_bt_signup);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                FindByUsername findByUsername = new FindByUsername();

                // Check enter is empty or not
                if (username.isEmpty()) {
                    sendToast("Please Enter the User Name");
                } else if (password.isEmpty()) {
                    sendToast("Please Enter the Password");
                } else {
                    findByUsername.execute(username);
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    // OKHttpConnection to NetBean
    private class FindByUsername extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String username = strings[0];
            return okHttpConnection.findByUsername(username);
        }

        @Override
        protected void onPostExecute(String s) {
            result = s;
            if (s.equals("[]")) {
                sendToast("User Not Found");
            } else {
                if (passwordRight(password)) {
                    // Pass credential
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("credentialFromSignIn", credential);
                    // Turn to the MainActivity to release Home Screen
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    sendToast("Wrong Password");
                }
            }
        }
    }

    // Check password is right or not
    public boolean passwordRight(String password) {
        String passwordHash = "";

        // get passwordhash from result
        Gson gson = new Gson();
        Credential[] credentials = gson.fromJson(result, Credential[].class);
        credential = credentials[0];

        // transfer the input password with MD5
        passwordHash = md5(password);

        return passwordHash.equals(credentials[0].getPasswordhash());
    }

    // transfer with MD5
    public static String md5(String originalStr) {
        MessageDigest messageDigest = null;
        String result = "";

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(originalStr.getBytes());
            for (byte b: bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result.substring(8,24);
    }

    // Toast
    protected void sendToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
