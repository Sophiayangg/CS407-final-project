package com.cs407.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, activity_catagories.class);
                startActivity(intent);
            }
        });

        ImageButton btnProfile = findViewById(R.id.btnUserProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, Profile.class);
                startActivity(intent);
            }
        });
        ImageButton btnHome = findViewById(R.id.btnHome);

        // Set the OnClickListener for Home Button
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, Home.class);
            startActivity(intent);
        });

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        DatabaseHelper db = new DatabaseHelper(this);

        Recipe recipe = db.getRecipeById(recipeId); // Implement this method in DatabaseHelper
        List<Note> notes = db.getNotesByRecipeId(recipeId); // Implement this method in DatabaseHelper

        TextView nameText = findViewById(R.id.name_text);
        TextView ingredientText = findViewById(R.id.output_text);
        TextView instructionText = findViewById(R.id.instruction_intro_text);
        TextView notesText = findViewById(R.id.notes);

        if (recipe != null) {
            nameText.setText(recipe.getName());
            ingredientText.setText(recipe.getIngredients());
            instructionText.setText(recipe.getInstructions());
            StringBuilder notesStringBuilder = new StringBuilder();
            for (Note note : notes) {
                notesStringBuilder.append(note.getContent()).append("\n");
            }
            notesText.setText(notesStringBuilder.toString());
        } else {
            nameText.setText("");
            ingredientText.setText("Details not available.");
            instructionText.setText("Details not available.");
            notesText.setText("Details not available.");

        }
    }
}
