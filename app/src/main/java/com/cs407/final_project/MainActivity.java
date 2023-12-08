package com.cs407.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    //signup page
        TextView loginTextView = (TextView) findViewById(R.id.login);
        String text = "Log In";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        loginTextView.setText(spannableString);

        //go to login page
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        //Signup user
        Button signUpButton = findViewById(R.id.signup);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextFirstname = findViewById(R.id.firstName);
                EditText editTextLastname = findViewById(R.id.lastName);
                EditText editTextEmail = findViewById(R.id.email);
                EditText editTextPassword = findViewById(R.id.Password);

                String  firstname= String.valueOf(editTextFirstname.getText());
                String  lastname = String.valueOf(editTextLastname.getText());
                String  email = String.valueOf(editTextEmail.getText());
                String  password = String.valueOf(editTextPassword.getText());

                CheckBox termsCheckbox = findViewById(R.id.checkBox); // Checkbox for terms of use
                if (!termsCheckbox.isChecked()) {
                    showErrorToast("You must agree to the terms of use.");
                    //Toast.makeText(MainActivity.this, "You must agree to the terms of use.", Toast.LENGTH_LONG).show();

                    return;
                }

                if (!isValidEmail(email)) {
                    showErrorToast("Invalid email address.");
                    return;
                }

                if (!isPasswordStrong(password)) {
                    showErrorToast("Password must be at least 6 characters long, contain at least one number, and one lowercase letter.");
                    return;
                }
                Context context = getApplicationContext();
                SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE, null);

                DBHelper dbHelper = new DBHelper(sqLiteDatabase);
                String message = dbHelper.saveUser(firstname, lastname, email, password);
                Log.i("RegistrationTag", message);

                // Check if the message indicates a duplicate email and show a Toast in that case
                if (message.contains("already registered")) {
                    //Toast.makeText(MainActivity.this, "duplicated email.", Toast.LENGTH_LONG).show();
                    showErrorToast(message);
                } else {
                    // Only proceed to the Home activity if the user was registered successfully
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email);
                    editor.putString("firstname",firstname);
                    editor.putString("lastname",lastname);
                    editor.apply();
                    startActivity(intent);
                }

            }
        });

    }
    private boolean isValidEmail(String email) {
        // Check if email is valid
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    private boolean isPasswordStrong(String password) {
        //  password that must be at least 6 characters long, contain at least one number, and one lowercase letter
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z]).{6,}$";
        return password.matches(passwordPattern);
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