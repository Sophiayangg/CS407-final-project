package com.cs407.final_project;

public class CategoryItem {
    private final int imageResourceId;
    private final String categoryName;

    public CategoryItem(int imageResourceId, String categoryName) {
        this.imageResourceId = imageResourceId;
        this.categoryName = categoryName;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
