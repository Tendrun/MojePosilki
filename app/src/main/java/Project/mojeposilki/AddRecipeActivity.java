package Project.mojeposilki;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;


public class AddRecipeActivity extends AppCompatActivity {

    private EditText recipeNameField;
    private RecyclerView productRecyclerView;
    private Button addProductButton, saveRecipeButton;
    private List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        recipeNameField = findViewById(R.id.recipeName);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        addProductButton = findViewById(R.id.addProductButton);
        saveRecipeButton = findViewById(R.id.saveRecipeButton);

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
        productList.add(new Product("", "", ""));
        productAdapter.notifyItemInserted(productList.size() - 1);
    }

    private void removeProduct(int position) {
        productList.remove(position);
        productAdapter.notifyItemRemoved(position);
    }

    private void saveRecipe() {
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


        // Zapisanie przepisu do bazy danych
        MealDatabaseHelper dbHelper = new MealDatabaseHelper(this);
        long currentTimeMillis = System.currentTimeMillis(); // Możesz użyć tej wartości jako daty

        // Zapisujemy samą nazwę przepisu z datą
        dbHelper.addMeal(recipeName, currentTimeMillis);

        // Możemy także dodać logikę do zapisania produktów w innej tabeli, jeśli to potrzebne
        // ...

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


