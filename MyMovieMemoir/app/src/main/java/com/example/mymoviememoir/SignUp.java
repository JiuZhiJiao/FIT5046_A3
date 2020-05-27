package com.example.mymoviememoir;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.model.Person;
import com.example.mymoviememoir.network.OKHttpConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class SignUp extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private RadioGroup radioGroup;
    // date picker
    private TextView displayDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Spinner spinner;
    private EditText etAddress;
    private EditText etPostcode;
    private Button btnSignIn;
    private Button btnSignUp;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    private String date;
    private String state;
    private String address;
    private String postcode;
    private boolean userExit;
    private int countCredential;
    private int countPerson;
    OKHttpConnection okHttpConnection = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Get Gender from radio group
        radioGroup = findViewById(R.id.sign_up_radioGroup);
        gender = "female";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sign_up_rb_male:
                        gender = "male";
                        break;
                    case R.id.sign_up_rb_female:
                        gender = "female";
                        break;
                }
            }
        });

        // Get DOB from date picker
        // Set the date picker
        displayDate = findViewById(R.id.sign_up_tv_datePicker);
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(SignUp.this, DatePickerDialog.THEME_HOLO_LIGHT,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // Listener for date picker
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                if (month < 10 && dayOfMonth < 10) {
                    date = year + "-0" + month + "-0" + dayOfMonth;
                } else if (month < 10 && dayOfMonth >= 10) {
                    date = year + "-0" + month + "-" + dayOfMonth;
                } else if (month > 10 && dayOfMonth < 10) {
                    date = year + "-" + month + "-0" + dayOfMonth;
                } else {
                    date = year + "-" + month + "-" + dayOfMonth;
                }
                displayDate.setText(date);
            }
        };

        // Get State from spinner
        spinner = findViewById(R.id.sign_up_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etFirstName = findViewById(R.id.sign_up_et_firstname);
        etLastName = findViewById(R.id.sign_up_et_lastname);
        etEmail = findViewById(R.id.sign_up_et_email);
        etPassword = findViewById(R.id.sign_up_et_password);
        etAddress = findViewById(R.id.sign_up_et_address);
        etPostcode = findViewById(R.id.sign_up_et_postcode);

        // Back to Sign In
        btnSignIn = findViewById(R.id.sign_up_bt_signin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

        okHttpConnection = new OKHttpConnection();
        btnSignUp = findViewById(R.id.sign_up_bt_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userExit = false;
                firstName = etFirstName.getText().toString().trim();
                lastName = etLastName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                address = etAddress.getText().toString().trim();
                postcode = etPostcode.getText().toString().trim();

                FindByUsername findByUsername = new FindByUsername();
                GetCountCredential getCountCredential = new GetCountCredential();
                GetCountPerson getCountPerson = new GetCountPerson();
                AddCredential addCredential = new AddCredential();
                AddPerson addPerson = new AddPerson();

                // Check Enter is empty or not
                if (validation(firstName,email,password)) {
                    // check user exist
                    findByUsername.execute(email);
                    if (!userExit) {

                        // Get the count of credential in the database
                        try {
                            countCredential = Integer.parseInt(getCountCredential.execute().get());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Get the count fo person in the database
                        try {
                            countPerson = Integer.parseInt(getCountPerson.execute().get());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Add new credential
                        String[] detailCredential = credentialDetails();
                        addCredential.execute(detailCredential);
                        // Add new person
                        String[] detailPerson = personDetails(detailCredential);
                        addPerson.execute(detailPerson);

                        //pass person
                        Credential credential = new Credential(countCredential, password, currentDate(), email);
                        Person person = new Person(countPerson, firstName, lastName, gender, date, address, state, postcode, credential);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("credentialFromSignUp", credential);
                        bundle.putParcelable("personFromSignUp", person);

                        // Turn to Home page
                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        sendToast("Sign Up Success");
                    }

                }

            }
        });
    }

    // Get Sign Up Date (Current Date)
    public String currentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date).toString()+"T00:00:00+11:00";
    }

    // validation
    protected boolean validation(String firstName, String username, String password) {
        boolean result = false;
        if (firstName.isEmpty()) {
            sendToast("Please Enter Your First Name");
        } else if (username.isEmpty()) {
            sendToast("Please Enter Your Email as User Name");
        } else if (password.isEmpty()) {
            sendToast("Please Enter Your Password");
        } else if (!checkPassword(password)) {
            sendToast("Password Should Include One Letter And One Number At Least");
        } else {
            result = true;
        }
        return result;
    }

    // Find the credential is exist or not
    private class FindByUsername extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String username = strings[0];
            return okHttpConnection.findByUsername(username);
        }

        @Override
        protected void onPostExecute(String s) {
            if (checkUserExist(s))
                sendToast("User Exist: Please Sign In");
        }
    }

    // Check user exist or not, and set uerExit
    protected boolean checkUserExist(String string) {
        if (string.equals("[]")) {
            userExit = false;
            return false;
        } else {
            userExit = true;
            return true;
        }
    }

    // Get the count of credentials in NetBean
    private class GetCountCredential extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return okHttpConnection.getCountCredentials();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    // Get the count of person in NetBean
    private class GetCountPerson extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return okHttpConnection.getCountPerson();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    // Post new credential to NetBean
    private class AddCredential extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String message = "The Credential with id " + strings[0] +" was added";
            return okHttpConnection.addCredential(strings) + message;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
        }
    }

    // Add new credential
    protected String[] credentialDetails() {
        password = md5(password);
        countCredential++;
        String[] details = new String[]{Integer.toString(countCredential), password, currentDate(), email};
        return details;
    }

    // Post new person to NetBean
    private class AddPerson extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String message = "The Person with id " + strings[0] + " was added";
            return okHttpConnection.addPerson(strings) + message;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
        }
    }

    // Add new person
    protected String[] personDetails(String[] strings) {
        countPerson++;
        date = date+"T00:00:00+11:00";
        String[] details = new String[] {Integer.toString(countPerson), firstName, lastName, gender, date, address, state, postcode, strings[0],strings[1],strings[2],strings[3]};
        return details;
    }

    // Check password design, password should include digit and letter
    protected boolean checkPassword(String password) {
        boolean isDigit = false;
        boolean isLetter = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                isDigit = true;
            } else if (Character.isLowerCase(password.charAt(i)) || Character.isUpperCase(password.charAt(i))) {
                isLetter = true;
            }
        }
        return isDigit && isLetter;
    }

    // transfer with MD5
    protected String md5(String originalStr) {
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
