package Client.Admin.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryModelAdmin {
    private List<String> categories;

    public CategoryModelAdmin() {
        this.categories = new ArrayList<>();
    }

    public void addCategory(String category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public void removeCategory(String category) {
        categories.remove(category);
    }

    public List<String> getCategories() {
        return categories;
    }

    public void clearCategories() {
        categories.clear();
    }
}
