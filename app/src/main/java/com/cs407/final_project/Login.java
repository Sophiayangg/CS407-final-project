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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                if(dbHelper.authenticateUser(email,password)){
                    TextView errorMessage = findViewById(R.id.errorMessage);
                    errorMessage.setText("");
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                }else{
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    TextView errorMessage = findViewById(R.id.errorMessage);
                    errorMessage.setText("Invalid email or password");
                }
            }
        });
    }
}