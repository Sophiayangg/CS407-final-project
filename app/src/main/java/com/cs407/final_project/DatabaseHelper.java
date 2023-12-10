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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RecipeDatabase";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_NOTES = "notes";

    private static final String TABLE_HISTORY = "histories";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_INTRODUCTION = "introduction";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_INSTRUCTIONS = "instructions";
    private static final String KEY_RECIPE_ID = "recipe_id";
    private static final String KEY_CONTENT = "content";

    private static final String KEY_USEREMAIL = "email";

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
                + KEY_INSTRUCTIONS + " TEXT,"
                + KEY_USEREMAIL + " TEXT"+ ")";

        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_INTRODUCTION + " TEXT,"
                + KEY_INGREDIENTS + " TEXT,"
                + KEY_INSTRUCTIONS + " TEXT,"
                + KEY_USEREMAIL + " TEXT"+ ")";

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_RECIPE_ID + " INTEGER,"
                + KEY_CONTENT + " TEXT,"
                + "FOREIGN KEY (" + KEY_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + KEY_ID + "))";

        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        // Create tables again
        onCreate(db);
    }

    /**
     * clear up all the tables in database
     */
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES); // Replace TABLE_NAME with your table name
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        // Create tables again
        onCreate(db);
    }


    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the recipe already exists in the database
        String[] columns = {KEY_ID};
        String selection = KEY_NAME + " = ? AND " + KEY_USEREMAIL + " = ? ";
        String[] selectionArgs = {recipe.getName(),recipe.getUserEmail()};
        Cursor cursor = db.query(TABLE_RECIPES, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return -2; // Recipe already exists
        }
        cursor.close();

        // Insert new recipe
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, recipe.getName());
        values.put(KEY_CATEGORY, String.join(",", recipe.getCategory())); // Store categories as a single string
        values.put(KEY_INTRODUCTION, recipe.getIntroduction());
        values.put(KEY_INGREDIENTS, recipe.getIngredients());
        values.put(KEY_INSTRUCTIONS, recipe.getInstructions());
        values.put(KEY_USEREMAIL,recipe.getUserEmail());

        long recipeId = db.insert(TABLE_RECIPES, null, values);
        db.close();
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

    public long addHistory(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the recipe already exists in the database
        String[] columns = {KEY_ID};
        String selection = KEY_NAME + " = ? AND " + KEY_USEREMAIL + " = ? ";
        String[] selectionArgs = {recipe.getName(),recipe.getUserEmail()};
        Cursor cursor = db.query(TABLE_HISTORY, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return -2; // Recipe already exists
        }
        cursor.close();

        // Insert new recipe
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, recipe.getName());
        values.put(KEY_CATEGORY, String.join(",", recipe.getCategory())); // Store categories as a single string
        values.put(KEY_INTRODUCTION, recipe.getIntroduction());
        values.put(KEY_INGREDIENTS, recipe.getIngredients());
        values.put(KEY_INSTRUCTIONS, recipe.getInstructions());
        values.put(KEY_USEREMAIL,recipe.getUserEmail());

        long recipeId = db.insert(TABLE_HISTORY, null, values);
        db.close();
        return recipeId;
    }

    public List<Recipe> getHistories(String userEmail){
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Fetch all recipes
        Cursor cursor = db.query(TABLE_HISTORY, new String[]{KEY_ID, KEY_NAME, KEY_CATEGORY, KEY_INTRODUCTION, KEY_INGREDIENTS, KEY_INSTRUCTIONS,KEY_USEREMAIL}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int emailIndex = cursor.getColumnIndex(KEY_USEREMAIL);
                //Log.d("email Index", "" + emailIndex);
                if(emailIndex != -1){
                    String email = cursor.getString(emailIndex);
                    if (email.trim().equalsIgnoreCase(userEmail.trim())) {
                        //Log.d("email matched", email);
                        int categoryIdIndex = cursor.getColumnIndex(KEY_CATEGORY);
                        int nameIndex = cursor.getColumnIndex(KEY_NAME);
                        int introIndex = cursor.getColumnIndex(KEY_INTRODUCTION);
                        int ingredientIndex = cursor.getColumnIndex(KEY_INGREDIENTS);
                        int instructionIndex = cursor.getColumnIndex(KEY_INSTRUCTIONS);
                        int idIndex = cursor.getColumnIndex(KEY_ID);


                        if (categoryIdIndex != -1 && nameIndex != -1 && introIndex != -1 && ingredientIndex != -1 && instructionIndex != -1 && idIndex != -1) { // Check if all indices are valid
                            Recipe recipe = new Recipe(
                                    cursor.getString(nameIndex),
                                    cursor.getString(categoryIdIndex),
                                    cursor.getString(introIndex),
                                    cursor.getString(ingredientIndex),
                                    cursor.getString(instructionIndex),
                                    email);
                            recipe.setId(cursor.getInt(idIndex));
                            recipeList.add(recipe);
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipeList;
    }
    public List<Recipe> getRecipesByCategory(String category,String userEmail) {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("LikedRecipesActivity", "getRecipesByCategory called for category: " + category); // Confirm method call


        // Fetch all recipes
        Cursor cursor = db.query(TABLE_RECIPES, new String[]{KEY_ID, KEY_NAME, KEY_CATEGORY, KEY_INTRODUCTION, KEY_INGREDIENTS, KEY_INSTRUCTIONS,KEY_USEREMAIL}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                int categoryIdIndex = cursor.getColumnIndex(KEY_CATEGORY);
                int emailIndex = cursor.getColumnIndex(KEY_USEREMAIL);

                if (categoryIdIndex != -1 && emailIndex != -1) {  // Check if the category index is valid
                    String categories = cursor.getString(categoryIdIndex);
                    String email = cursor.getString(emailIndex);
                    //Log.d("Categories", "Fetched Categories: " + categories); // Log the categories
                    String[] categoryArray = categories.split("/");
                    if(email.trim().equalsIgnoreCase(userEmail.trim())){
                        //Log.d("email matched", email);
                        for (String cat : categoryArray) {
                            //Log.d("CategoryCheck", "Checking Category: " + cat.trim()); // Log each category being checked
                            if (cat.trim().equalsIgnoreCase(category.trim())) {
                                //Log.d("CategoryMatch", "Match found for " + cat.trim()); // Log a successful match
                                int nameIndex = cursor.getColumnIndex(KEY_NAME);
                                int introIndex = cursor.getColumnIndex(KEY_INTRODUCTION);
                                int ingredientIndex = cursor.getColumnIndex(KEY_INGREDIENTS);
                                int instructionIndex = cursor.getColumnIndex(KEY_INSTRUCTIONS);
                                int idIndex = cursor.getColumnIndex(KEY_ID);

                                if (nameIndex != -1 && introIndex != -1 && ingredientIndex != -1 && instructionIndex != -1 && idIndex != -1) { // Check if all indices are valid
                                    Recipe recipe = new Recipe(
                                            cursor.getString(nameIndex),
                                            categories,
                                            cursor.getString(introIndex),
                                            cursor.getString(ingredientIndex),
                                            cursor.getString(instructionIndex),
                                            email);
                                    recipe.setId(cursor.getInt(idIndex));
                                    recipeList.add(recipe);
                                    break; // Exit the loop once a match is found
                                }
                            }
                        }
                    }
                }
                else {
                    Log.d("LikedRecipesActivity", "Category column index is -1");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return recipeList;
    }





    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[]{KEY_ID, KEY_NAME, KEY_CATEGORY, KEY_INTRODUCTION, KEY_INGREDIENTS, KEY_INSTRUCTIONS, KEY_USEREMAIL}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int categoryIndex = cursor.getColumnIndex(KEY_CATEGORY);
            int introductionIndex = cursor.getColumnIndex(KEY_INTRODUCTION);
            int ingredientsIndex = cursor.getColumnIndex(KEY_INGREDIENTS);
            int instructionsIndex = cursor.getColumnIndex(KEY_INSTRUCTIONS);
            int emailIndex = cursor.getColumnIndex(KEY_USEREMAIL);

            if (idIndex != -1 && nameIndex != -1 && categoryIndex != -1 && introductionIndex != -1 && ingredientsIndex != -1 && instructionsIndex != -1 && emailIndex != -1) {
                // Extract values from the cursor
                String name = cursor.getString(nameIndex);
                String category = cursor.getString(categoryIndex);
                String introduction = cursor.getString(introductionIndex);
                String ingredients = cursor.getString(ingredientsIndex);
                String instructions = cursor.getString(instructionsIndex);
                String email = cursor.getString(emailIndex);
                // Use your constructor to create a Recipe object
                recipe = new Recipe(name, category, introduction, ingredients, instructions,email);
                recipe.setId(cursor.getInt(idIndex));
            }

            cursor.close();
        }

        return recipe;
    }

    public Recipe getHistoryById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORY, new String[]{KEY_ID, KEY_NAME, KEY_CATEGORY, KEY_INTRODUCTION, KEY_INGREDIENTS, KEY_INSTRUCTIONS, KEY_USEREMAIL}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int categoryIndex = cursor.getColumnIndex(KEY_CATEGORY);
            int introductionIndex = cursor.getColumnIndex(KEY_INTRODUCTION);
            int ingredientsIndex = cursor.getColumnIndex(KEY_INGREDIENTS);
            int instructionsIndex = cursor.getColumnIndex(KEY_INSTRUCTIONS);
            int emailIndex = cursor.getColumnIndex(KEY_USEREMAIL);

            if (idIndex != -1 && nameIndex != -1 && categoryIndex != -1 && introductionIndex != -1 && ingredientsIndex != -1 && instructionsIndex != -1) {
                // Extract values from the cursor
                String name = cursor.getString(nameIndex);
                String category = cursor.getString(categoryIndex);
                String introduction = cursor.getString(introductionIndex);
                String ingredients = cursor.getString(ingredientsIndex);
                String instructions = cursor.getString(instructionsIndex);
                String email = cursor.getString(emailIndex);

                // Use your constructor to create a Recipe object
                recipe = new Recipe(name, category, introduction, ingredients, instructions,email);
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
    public void deleteRecipe(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, KEY_ID + " = ?", new String[]{String.valueOf(recipeId)});
        db.delete(TABLE_NOTES, KEY_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public void deleteHistory(int historyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, KEY_ID + " = ?", new String[]{String.valueOf(historyId)});
        db.delete(TABLE_NOTES, KEY_RECIPE_ID + " = ?", new String[]{String.valueOf(historyId)});
        db.close();
    }
}

