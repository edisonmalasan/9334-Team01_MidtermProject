package Client.Admin.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryModelAdmin {
    private List<String> categories;

    public CategoryModelAdmin() {
        this.categories = new ArrayList<>();
    }

    // Add a new category if it doesn't already exist
    public void addCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (!categories.contains(category)) {
            categories.add(category);
        } else {
            System.out.println("Category already exists: " + category);
        }
    }

    // Remove a category if it exists
    public void removeCategory(String category) {
        if (categories.remove(category)) {
            System.out.println("Category removed: " + category);
        } else {
            System.out.println("Category not found: " + category);
        }
    }

    // Get the list of categories
    public List<String> getCategories() {
        return new ArrayList<>(categories); // Return a copy to prevent external modification
    }

    // Clear all categories
    public void clearCategories() {
        categories.clear();
        System.out.println("All categories have been cleared.");
    }

    // Check if a category exists
    public boolean categoryExists(String category) {
        return categories.contains(category);
    }

    // Get the number of categories
    public int getCategoryCount() {
        return categories.size();
    }
}