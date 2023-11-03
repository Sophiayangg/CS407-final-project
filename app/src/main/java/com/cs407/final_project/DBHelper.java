package com.cs407.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper{
    // Define the database schema and table creation here
    static SQLiteDatabase sqLiteDatabase;
    public DBHelper(SQLiteDatabase sqLiteDatabase)
    {this.sqLiteDatabase = sqLiteDatabase;}
    public static void createTable(){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users "+
                "(id INTEGER PRIMARY KEY, firstname TEXT, lastname TEXT, email TEXT UNIQUE, password TEXT)");
    }
    public String saveUser(String firstname, String lastname, String email, String password){
        createTable();
        ContentValues values = new ContentValues();
        values.put("firstname", firstname);
        values.put("lastname", lastname);
        values.put("email", email);
        values.put("password", password);
        try {
            sqLiteDatabase.insertOrThrow("users", null, values);
            return "User registered successfully.";
        } catch (SQLiteConstraintException e) {
            Log.e("DBHelper", "Error: Duplicate email", e);
            return "Error: This email is already registered.";
        }
        //sqLiteDatabase.execSQL("INSERT INTO users (firstname,lastname, email, password) VALUES (?,?,?,?)",
                //new String[]{firstname,lastname, email,password});
    }
    public boolean authenticateUser(String email, String passwordInput){
        try{
            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE email = ?",
                    new String[]{email});
            if (c != null && c.moveToFirst()) {
                int index = c.getColumnIndex("password");
                String password = c.getString(index);
                Log.e("the password", password);
                if(password.equals(passwordInput)){
                    return true;
                }
                c.close();
            } else {
                Log.e("login","email not in the database");
            }

        } catch (Exception e) {
            Log.e("debug",e.getMessage());
        }
        return false;
    }
}
