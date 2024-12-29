package Project.mojeposilki;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class AddRecipeActivity extends AppCompatActivity {

    private EditText recipeNameField, description;
    private RecyclerView productRecyclerView;
    private Button addProductButton, saveRecipeButton;
    private List<Product> productList;
    private ProductAdapter productAdapter;

    private Spinner mealTypeSpinner;

    MealDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Zapisanie przepisu do bazy danych
        dbHelper = new MealDatabaseHelper(this);


        setContentView(R.layout.activity_add_recipe);

        recipeNameField = findViewById(R.id.recipeName);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        addProductButton = findViewById(R.id.addProductButton);
        saveRecipeButton = findViewById(R.id.saveRecipeButton);
        description = findViewById(R.id.Description);
        mealTypeSpinner = findViewById(R.id.spinnerMealType);


        // Initialize product list and adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this::removeProduct);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);

        // Add new product when button is clicked
        addProductButton.setOnClickListener(v -> addNewProduct());

        saveRecipeButton.setOnClickListener(v -> saveRecipe());
    }

    private void addNewProduct() {
        // Create a new empty product and add to the list
        productList.add(new Product("", "", "", "", "",
                "", ""));
        productAdapter.notifyItemInserted(productList.size() - 1);
    }

    private void removeProduct(int position) {
        productList.remove(position);
        productAdapter.notifyItemRemoved(position);
    }

    private void saveRecipe() {
        String recipeName = recipeNameField.getText().toString().trim();
        String descriptionFormated = description.getText().toString().trim();
        String mealType = mealTypeSpinner.getSelectedItem().toString();

        if (recipeName.isEmpty()) {
            recipeNameField.setError("Recipe name cannot be empty");
            return;
        }

        // Validate each product (ingredient)
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if (product.getSkladnik().isEmpty() || product.getIlosc().isEmpty() ||
                    product.getJednostka().isEmpty() || product.getKategoria().isEmpty() ) {
                Toast.makeText(this, "Please fill out all fields for each ingredient", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isNumeric(product.getIlosc())) {
                Toast.makeText(this, "Invalid numeric value in ingredient " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }
        }


        // Zapisujemy samą nazwę przepisu z datą
        long MealID = dbHelper.addMeal(recipeName, descriptionFormated, mealType);


        // Możemy także dodać logikę do zapisania produktów w innej tabeli, jeśli to potrzebne
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            dbHelper.addIngredient(product.getSkladnik(), product.getIlosc(),  product.getJednostka(),
                    product.getKategoria(), product.getfats(), product.getcarbohydrates(),
                    product.getProtein(), Long.toString(MealID));

            System.out.println("product.getKategoria()" + product.getKategoria());

        }


        Toast.makeText(this, "Recipe saved to database!", Toast.LENGTH_SHORT).show();

        // Po zapisaniu przepisu przechodzimy do MainActivity
        Intent intent = new Intent(AddRecipeActivity.this, MainActivity.class);
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


