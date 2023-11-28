package com.cs407.final_project;

public class Recipe {
    private int id;
    private String name;
    private String category;
    private String introduction;
    private String ingredients;
    private String instructions;

    // Constructors
    public Recipe() {}

    public Recipe(String name, String category, String introduction, String ingredients, String instructions) {
        this.name = name;
        this.category = category;
        this.introduction = introduction;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
    public String getIntroduction() {
        return introduction;
    }
    public String getIngredients() {
        return ingredients;
    }
    public String getInstructions() {
        return instructions;
    }
}
