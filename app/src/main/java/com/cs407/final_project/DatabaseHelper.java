package com.cs407.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RecipeDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_NOTES = "notes";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_INTRODUCTION = "introduction";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_INSTRUCTIONS = "instructions";
    private static final String KEY_RECIPE_ID = "recipe_id";
    private static final String KEY_CONTENT = "content";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_INTRODUCTION + " TEXT,"
                + KEY_INGREDIENTS + " TEXT,"
                + KEY_INSTRUCTIONS + " TEXT" + ")";

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_RECIPE_ID + " INTEGER,"
                + KEY_CONTENT + " TEXT,"
                + "FOREIGN KEY (" + KEY_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + KEY_ID + "))";

        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        // Create tables again
        onCreate(db);
    }

    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the recipe already exists in the database
        String[] columns = {KEY_ID};
        String selection = KEY_NAME + " = ?"; // You can add more criteria if needed
        String[] selectionArgs = {recipe.getName()};
        Cursor cursor = db.query(TABLE_RECIPES, columns, selection, selectionArgs, null, null, null);

        long recipeId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            if (idIndex != -1) {
                recipeId = cursor.getLong(idIndex);
                cursor.close();
                db.close();
                return -2; // Special value indicating the recipe already exists
            }
        }
        cursor.close();

        if (recipeId == -1) {
            // Recipe does not exist, insert new row
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, recipe.getName());
            values.put(KEY_CATEGORY, recipe.getCategory());
            values.put(KEY_INTRODUCTION, recipe.getIntroduction());
            values.put(KEY_INGREDIENTS, recipe.getIngredients());
            values.put(KEY_INSTRUCTIONS, recipe.getInstructions());

            recipeId = db.insert(TABLE_RECIPES, null, values);
        }

        db.close(); // Closing database connection
        return recipeId;
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPE_ID, note.getRecipeId());
        values.put(KEY_CONTENT, note.getContent());

        // Inserting Row
        db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection
    }

    public List<Recipe> getRecipesByCategory(String category) {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_ID, KEY_NAME, KEY_CATEGORY, KEY_INTRODUCTION, KEY_INGREDIENTS, KEY_INSTRUCTIONS }, KEY_CATEGORY + "=?", new String[] { category }, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                recipe.setId(cursor.getInt(0));
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return recipeList;
    }

    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[]{KEY_ID, KEY_NAME, KEY_CATEGORY, KEY_INTRODUCTION, KEY_INGREDIENTS, KEY_INSTRUCTIONS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int categoryIndex = cursor.getColumnIndex(KEY_CATEGORY);
            int introductionIndex = cursor.getColumnIndex(KEY_INTRODUCTION);
            int ingredientsIndex = cursor.getColumnIndex(KEY_INGREDIENTS);
            int instructionsIndex = cursor.getColumnIndex(KEY_INSTRUCTIONS);

            if (idIndex != -1 && nameIndex != -1 && categoryIndex != -1 && introductionIndex != -1 && ingredientsIndex != -1 && instructionsIndex != -1) {
                // Extract values from the cursor
                String name = cursor.getString(nameIndex);
                String category = cursor.getString(categoryIndex);
                String introduction = cursor.getString(introductionIndex);
                String ingredients = cursor.getString(ingredientsIndex);
                String instructions = cursor.getString(instructionsIndex);

                // Use your constructor to create a Recipe object
                recipe = new Recipe(name, category, introduction, ingredients, instructions);
                recipe.setId(cursor.getInt(idIndex));
            }

            cursor.close();
        }

        return recipe;
    }
    public List<Note> getNotesByRecipeId(int recipeId) {
        List<Note> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{KEY_ID, KEY_RECIPE_ID, KEY_CONTENT}, KEY_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)}, null, null, null);

        if (cursor != null) {
            int recipeIdIndex = cursor.getColumnIndex(KEY_RECIPE_ID);
            int contentIndex = cursor.getColumnIndex(KEY_CONTENT);

            if (recipeIdIndex != -1 && contentIndex != -1) {
                while (cursor.moveToNext()) {
                    int noteRecipeId = cursor.getInt(recipeIdIndex);
                    String content = cursor.getString(contentIndex);
                    Note note = new Note(noteRecipeId, content);
                    notesList.add(note);
                }
            }
            cursor.close();
        }

        return notesList;
    }

    public boolean isRecipeLiked(int recipeId) {
        if (recipeId!=1){
        SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_RECIPES, new String[]{KEY_ID}, KEY_ID + "=?", new String[]{String.valueOf(recipeId)}, null, null, null);
        boolean isLiked = cursor.getCount() > 0;
        cursor.close();
        return isLiked;}
        else{
            String error="error";
            Log.d("error",error);
            return false;
        }

    }


}

