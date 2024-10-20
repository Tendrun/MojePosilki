package Project.mojeposilki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModifyMealActivity extends AppCompatActivity {

    private EditText recipeNameField, description;
    private RecyclerView productRecyclerView;
    private Button addProductButton, saveRecipeButton;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private long mealId = -1;  // Default value for new meal

    private MealDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Initialize views
        recipeNameField = findViewById(R.id.recipeName);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        addProductButton = findViewById(R.id.addProductButton);
        saveRecipeButton = findViewById(R.id.saveRecipeButton);
        description = findViewById(R.id.Description);

        // Initialize the database helper
        dbHelper = new MealDatabaseHelper(this);

        // Retrieve the meal ID if this is an edit operation
        Intent intent = getIntent();
        mealId = intent.getLongExtra("mealId", -1);  // Defaults to -1 if no ID is passed

        /*
        // If we're editing, load the meal details
        if (mealId != -1) {
            loadMealDetails(mealId);
        }
*/
        // Initialize product list and adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this::removeProduct);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);

        // Add new product when button is clicked
        addProductButton.setOnClickListener(v -> addNewProduct());

        // Save the recipe when the button is clicked
        saveRecipeButton.setOnClickListener(v -> saveRecipe());
    }

    private void addNewProduct() {
        // Create a new empty product and add to the list
        productList.add(new Product("", "", ""));
        productAdapter.notifyItemInserted(productList.size() - 1);
    }

    private void removeProduct(int position) {
        productList.remove(position);
        productAdapter.notifyItemRemoved(position);
    }

    private void saveRecipe() {
        String descriptionFormated = description.getText().toString().trim();
        String recipeName = recipeNameField.getText().toString().trim();
        if (recipeName.isEmpty()) {
            recipeNameField.setError("Recipe name cannot be empty");
            return;
        }

        // Validate each product (ingredient)
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if (product.getSkladnik().isEmpty() || product.getIlosc().isEmpty() || product.getJednostka().isEmpty()) {
                Toast.makeText(this, "Please fill out all fields for each ingredient", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isNumeric(product.getIlosc())) {
                Toast.makeText(this, "Invalid numeric value in ingredient " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Save the recipe to the database
        if (mealId != -1) {
            // Update the existing meal
            ContentValues values = new ContentValues();
            values.put("meal_name", recipeName);

            int rowsUpdated = dbHelper.updateMeal(mealId, values);
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Recipe updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update recipe!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add a new meal if it's not an edit
            long currentTimeMillis = System.currentTimeMillis();  // Use this as the date
            mealId = dbHelper.addMeal(recipeName, currentTimeMillis, descriptionFormated);  // Get the new meal ID
            Toast.makeText(this, "Recipe saved to database!", Toast.LENGTH_SHORT).show();
        }

        // Save the ingredients
        dbHelper.deleteMeal(mealId);  // Clear old ingredients for this mealId

        // Zapisanie przepisu do bazy danych
        MealDatabaseHelper dbHelper = new MealDatabaseHelper(this);
        long currentTimeMillis = System.currentTimeMillis(); // Możesz użyć tej wartości jako daty

        // Zapisujemy samą nazwę przepisu z datą
        dbHelper.addMeal(recipeName, currentTimeMillis, descriptionFormated);

        // Możemy także dodać logikę do zapisania produktów w innej tabeli, jeśli to potrzebne
        // ...

        Toast.makeText(this, "Recipe saved to database!", Toast.LENGTH_SHORT).show();


        // Po zapisaniu przepisu przechodzimy do MainActivity
        Intent intent = new Intent(ModifyMealActivity.this, MainActivity.class);
        startActivity(intent);
    }


    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
