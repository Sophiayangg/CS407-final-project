package com.cs407.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class activity_catagories extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<CategoryItem> categoryItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        ImageButton buttonMenu = findViewById(R.id.btnMenu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_catagories.this, activity_catagories.class);
                startActivity(intent);
            }
        });

        ImageButton buttonProfile = findViewById(R.id.btnUserProfile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_catagories.this, Profile.class);
                startActivity(intent);
            }
        });
        ImageButton btnHome = findViewById(R.id.btnHome);

        // Set the OnClickListener for Home Button
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(activity_catagories.this, Home.class);
            startActivity(intent);
        });
        // Replace 'your_layout' with your actual layout
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        // Prepare data
        categoryItem = new ArrayList<>();
        // Add CategoryItems to the list
        categoryItem.add(new CategoryItem(R.drawable.img_5938, "ASIAN"));
        categoryItem.add(new CategoryItem(R.drawable.img_5941, "DRINKS"));
        categoryItem.add(new CategoryItem(R.drawable.img_5942, "MEXICAN"));
        categoryItem.add(new CategoryItem(R.drawable.img_5943, "WESTERN"));
        categoryItem.add(new CategoryItem(R.drawable.img_5940, "INDIAN"));
        categoryItem.add(new CategoryItem(R.drawable.img_5946, "DESSERTS"));
        categoryItem.add(new CategoryItem(R.drawable.img, "AFRICAN"));
        categoryItem.add(new CategoryItem(R.drawable.img_5945, "OTHER"));

        // Set adapter
        adapter = new CategoryAdapter(this, categoryItem);
        recyclerView.setAdapter(adapter);

        // ... add other categories


        //AppCompatButton asianButton = findViewById(R.id.AsianButton);

//        setupCategoryButton(R.id.AsianButton, "Asian");
//        setupCategoryButton(R.id.MexicanButton, "Mexican");
//        setupCategoryButton(R.id.WesternButton, "Western");
//        setupCategoryButton(R.id.OthersButton, "Others");
//        setupCategoryButton(R.id.DessertsButton, "Desserts");
//        setupCategoryButton(R.id.IndianButton, "Indian");
//        setupCategoryButton(R.id.AfricanButton, "African");
//        setupCategoryButton(R.id.DrinksButton, "Drinks");

    }

    private void setupCategoryButton(int buttonId, final String category) {
        AppCompatButton categoryButton = findViewById(buttonId);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_catagories.this, LikedRecipesActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });
    }



}
