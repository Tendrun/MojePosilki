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

public class ModifyMealActivity extends AppCompatActivity {

    private EditText recipeName;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private Button addProductButton, saveRecipeButton;
    private MealDatabaseHelper mealDatabaseHelper;
    private long mealId; // This will hold the ID of the meal we are modifying
    private ArrayList<String> ingredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_meal);

        recipeName = findViewById(R.id.recipeName);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        addProductButton = findViewById(R.id.addProductButton);
        saveRecipeButton = findViewById(R.id.saveRecipeButton);

        mealDatabaseHelper = new MealDatabaseHelper(this);

        // Retrieve the meal ID passed from the previous activity
        mealId = getIntent().getLongExtra("mealId", -1);

        if (mealId != -1) {
            loadMealDetails(mealId); // Load the meal details to modify
        }

        // Setup RecyclerView for ingredients
        ingredientsList = new ArrayList<>();
        productAdapter = new ProductAdapter(ingredientsList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);

        // Button to add a new ingredient
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewIngredient();
            }
        });

        // Save the modified meal to the database
        saveRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveModifiedMeal();
            }
        });
    }

    // Method to load meal details from the database
    private void loadMealDetails(long mealId) {
        Cursor cursor = mealDatabaseHelper.getMealById(mealId);
        if (cursor != null && cursor.moveToFirst()) {
            // Set the meal name to the EditText
            String mealName = cursor.getString(cursor.getColumnIndex("meal_name"));
            recipeName.setText(mealName);

            // Load the list of ingredients (if stored as a comma-separated string)
            String ingredients = cursor.getString(cursor.getColumnIndex("ingredients"));
            if (ingredients != null && !ingredients.isEmpty()) {
                String[] ingredientArray = ingredients.split(",");
                for (String ingredient : ingredientArray) {
                    ingredientsList.add(ingredient.trim());
                }
                productAdapter.notifyDataSetChanged();
            }
        }
    }

    // Add a new ingredient to the RecyclerView
    private void addNewIngredient() {
        ingredientsList.add(""); // Add an empty ingredient for the user to fill in
        productAdapter.notifyItemInserted(ingredientsList.size() - 1);
    }

    // Save the modified meal details
    private void saveModifiedMeal() {
        String updatedMealName = recipeName.getText().toString().trim();

        // Convert ingredients list to a comma-separated string
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (String ingredient : ingredientsList) {
            if (!ingredient.isEmpty()) {
                ingredientsBuilder.append(ingredient).append(",");
            }
        }

        if (ingredientsBuilder.length() > 0) {
            ingredientsBuilder.deleteCharAt(ingredientsBuilder.length() - 1); // Remove trailing comma
        }

        String updatedIngredients = ingredientsBuilder.toString();

        // Prepare values to update in the database
        ContentValues values = new ContentValues();
        values.put("meal_name", updatedMealName);
        values.put("ingredients", updatedIngredients);

        // Update the meal in the database
        mealDatabaseHelper.updateMeal(mealId, values);

        // Show a message and finish the activity
        Toast.makeText(this, "Meal updated successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity and go back to the previous screen
    }
}
