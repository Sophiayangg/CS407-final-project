package com.cs407.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

public class Login extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //login page
        TextView signupTextView = (TextView) findViewById(R.id.signup);
        String text = "Sign up";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        signupTextView.setText(spannableString);

        /*
        // Check if "Remember Me" is enabled
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.Password);
        rememberMeCheckBox = findViewById(R.id.checkBoxRememberMe);
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        if (sharedPreferencesHelper.getRememberMeStatus()) {
            emailEditText.setText(sharedPreferencesHelper.getSavedUsername());
            passwordEditText.setText(sharedPreferencesHelper.getSavedPassword());
            rememberMeCheckBox.setChecked(true);
        }

         */


        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.Password);
        rememberMeCheckBox = findViewById(R.id.checkBoxRememberMe);
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        // Check if "Remember Me" is enabled
        if (sharedPreferencesHelper.getRememberMeStatus()) {
            emailEditText.setText(sharedPreferencesHelper.getSavedUsername());
            passwordEditText.setText(sharedPreferencesHelper.getSavedPassword());
            rememberMeCheckBox.setChecked(true);
        }
        // Add your login button click listener
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                boolean rememberMe = rememberMeCheckBox.isChecked();
                if (isValidCredentials(email, password)) {
                    // Save credentials if "Remember Me" is enabled
                    if (rememberMe) {
                        sharedPreferencesHelper.saveCredentials(email, password, true);
                        // Launch the main activity
                    } else {
                        sharedPreferencesHelper.saveCredentials("", "", false);
                    }

                    // Launch the main activity
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Optional: finish the login activity to prevent going back
                }
            }
        });

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.Password);
        rememberMeCheckBox = findViewById(R.id.checkBoxRememberMe);
        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username, password, and rememberMe status
                String username = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                boolean rememberMe = rememberMeCheckBox.isChecked();

                // Validate the login credentials (replace with your authentication logic)
                if (isValidCredentials(username, password)) {
                    // Save credentials if "Remember Me" is enabled
                    if (rememberMe) {
                        sharedPreferencesHelper.saveCredentials(username, password, true);
                    } else {
                        sharedPreferencesHelper.saveCredentials("", "", false);
                    }

                    // Launch the main activity
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Optional: finish the login activity to prevent going back
                } else {
                    // Display an error message or handle failed login
                    // For example, show a Toast or update a TextView
                }
            }
        });
        //go to sign up page
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate to LoginActivity
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //sign in
        Button signupButton = findViewById(R.id.login);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editTextEmail = findViewById(R.id.email);
                EditText editTextPassword = findViewById(R.id.Password);

                String  email = String.valueOf(editTextEmail.getText());
                String  password = String.valueOf(editTextPassword.getText());


                Context context = getApplicationContext();
                SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users",Context.MODE_PRIVATE,null);
                DBHelper dbHelper = new DBHelper(sqLiteDatabase);
                if (dbHelper.authenticateUser(email, password)) {
                    String[] name = dbHelper.getFirstLastName(email);
                    String firstName = name[0];
                    String lastName = name[1];

                    if (firstName != null && !firstName.isEmpty()) {
                        //save user email
                        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email", email);
                        editor.putString("firstname",firstName);
                        editor.putString("lastname",lastName);
                        editor.apply();
                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);
                        finish(); // Close the login activity
                    } else {
                        // Handle the error case where the first name couldn't be retrieved
                        TextView errorMessage = findViewById(R.id.errorMessage);
                        showErrorToast("Error retrieving user information.");
                    }
                } else {
                    // Authentication failed
                    TextView errorMessage = findViewById(R.id.errorMessage);
                    showErrorToast("Incorrect email or password.");
                }

                /*
                // Save credentials if "Remember Me" is enabled
                boolean rememberMe = rememberMeCheckBox.isChecked();

                if (isValidCredentials(email, password)) {
                    // Save credentials if "Remember Me" is enabled
                    if (rememberMe) {
                        sharedPreferencesHelper.saveCredentials(email, password, true);
                    } else {
                        sharedPreferencesHelper.saveCredentials("", "", false);
                    }

                    // Launch the main activity
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Optional: finish the login activity to prevent going back
                } else {
                    // Display an error message or handle failed login
                    // For example, show a Toast or update a TextView
                }
                */
            }
        });
    }


    private boolean isValidCredentials(String email, String password) {
        // Add your authentication logic here (e.g., check against a server)
        // For this example, let's consider it valid if both fields are non-empty
        return !email.isEmpty() && !password.isEmpty();
    }

    private void showErrorToast(String message) {
        // Inflate the custom layout.
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container)); // Replace with the ID of your container

        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        text.setText(message); // Set your error message

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout); // Set the inflated layout
        toast.show();
    }
}