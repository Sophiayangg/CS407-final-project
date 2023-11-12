package com.cs407.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //login page
        TextView signupTextView = (TextView) findViewById(R.id.signup);
        String text = "Sign up";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        signupTextView.setText(spannableString);

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
                    String firstName = dbHelper.getFirstName(email); // Use the new method to get the first name

                    if (firstName != null && !firstName.isEmpty()) {
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.putExtra("USER_NAME", firstName);
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
            }
        });
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