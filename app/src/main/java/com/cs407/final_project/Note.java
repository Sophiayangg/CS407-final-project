package com.cs407.final_project;

public class Note {
    private int id;
    private int recipeId;
    private String content;

    // Constructors
    public Note() {}

    public Note(int recipeId, String content) {
        this.recipeId = recipeId;
        this.content = content;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getContent() {
        return content;
    }
}
