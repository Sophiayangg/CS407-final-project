package com.cs407.final_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        DatabaseHelper db = new DatabaseHelper(this);

        Recipe recipe = db.getRecipeById(recipeId); // Implement this method in DatabaseHelper
        List<Note> notes = db.getNotesByRecipeId(recipeId); // Implement this method in DatabaseHelper

        TextView nameText = findViewById(R.id.name_text);
        TextView ingredientText = findViewById(R.id.output_text);
        TextView instructionText = findViewById(R.id.instruction_intro_text);
        TextView notesText = findViewById(R.id.notes);

        nameText.setText(recipe.getName());
        ingredientText.setText(recipe.getIngredients());
        instructionText.setText(recipe.getInstructions());
        // Print the recipe details to the Logcat
        Log.d("RecipeDetail", "Name: " + recipe.getName());
        Log.d("RecipeDetail", "Ingredients: " + recipe.getIngredients());
        Log.d("RecipeDetail", "Instructions: " + recipe.getInstructions());

        StringBuilder notesStringBuilder = new StringBuilder();
        for (Note note : notes) {
            notesStringBuilder.append(note.getContent()).append("\n");
        }
        notesText.setText(notesStringBuilder.toString());
    }
}
