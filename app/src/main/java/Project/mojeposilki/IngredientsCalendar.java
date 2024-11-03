package Project.mojeposilki;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
public class IngredientsCalendar extends AppCompatActivity {

    private ListView lvMeals;
    private Spinner spinnerCategory;
    private MealDatabaseHelper mealDatabaseHelper;
    private IngredientCursorAdapter adapter;
    private DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_calendar);

        lvMeals = findViewById(R.id.lv_meals);
        spinnerCategory = findViewById(R.id.spinner_category);
        datePicker = findViewById(R.id.datePicker);

        mealDatabaseHelper = new MealDatabaseHelper(this);

        // Populate spinner with categories
        String[] categories = {"All Categories", "Vegetables", "Fruits", "Dairy", "Meat", "Grains"}; // Example categories
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        // Set listener for date and category changes
        datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> loadIngredientsForDateAndCategory());

        //TODO
        printAllIngredients();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadIngredientsForDateAndCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void printAllIngredients() {
        Cursor cursor = mealDatabaseHelper.getAllIngredients();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String skladnik = cursor.getString(cursor.getColumnIndexOrThrow("skladnik"));
                String kategoria = cursor.getString(cursor.getColumnIndexOrThrow("kategoria"));
                String ilosc = cursor.getString(cursor.getColumnIndexOrThrow("ilosc"));
                String jednostka = cursor.getString(cursor.getColumnIndexOrThrow("jednostka"));

                System.out.println("Ingredient ID: " + id);
                System.out.println("Ingredient Name: " + skladnik);
                System.out.println("Category: " + kategoria);
                System.out.println("Quantity: " + ilosc);
                System.out.println("Unit: " + jednostka);
                System.out.println("---------------");
            }
            cursor.close();
        }
    }


    private void loadIngredientsForDateAndCategory() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String selectedCategory = spinnerCategory.getSelectedItem().toString();
        long dateInMillis = calendar.getTimeInMillis();

        // Fetch ingredients from the database
        Cursor cursor = mealDatabaseHelper.getIngredientsByDate(dateInMillis, selectedCategory);

        if (adapter == null) {
            adapter = new IngredientCursorAdapter(this, cursor, 0);
            lvMeals.setAdapter(adapter);
        } else {
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();  // Force ListView to refresh
        }
    }



    // Method to add the selected meal to the calendar
    private void addMealToCalendar(long mealId) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0); // Set to midnight (optional)
        calendar.set(Calendar.MILLISECOND, 0);

        // Handle adding meal logic with date here
        MealDatabaseHelper dbHelper = new MealDatabaseHelper(this);
        dbHelper.addMealToCalendar(mealId, calendar.getTimeInMillis());

        Toast.makeText(this, "Meal added to calendar", Toast.LENGTH_SHORT).show();
    }

    // Custom adapter for the ListView
    public class IngredientCursorAdapter extends android.widget.SimpleCursorAdapter {

        public IngredientCursorAdapter(Context context, Cursor c, int flags) {
            super(context, R.layout.ingridient_list_item, c, new String[]{
                    "skladnik"
            }, new int[]{
                    R.id.tv_meal_name
            }, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView skladnikTextView = view.findViewById(R.id.tv_meal_name);
            Button btnMarkBought = view.findViewById(R.id.btn_mark_bought);
            Button btnAddIngredient = view.findViewById(R.id.btn_add_ingredient);
            Button btnDeleteIngredient = view.findViewById(R.id.btn_delete_ingredient);

            // Set ingredient name
            String skladnik = cursor.getString(cursor.getColumnIndex("skladnik"));
            skladnikTextView.setText(skladnik);

            // Set click listeners for buttons
            btnMarkBought.setOnClickListener(v -> {
                // Logic to mark as bought (for future implementation)
                Toast.makeText(context, skladnik + " marked as bought", Toast.LENGTH_SHORT).show();
            });

            btnAddIngredient.setOnClickListener(v -> {
                // Navigate to AddRecipeActivity
                Intent intent = new Intent(context, AddRecipeActivity.class);
                context.startActivity(intent);
            });

            btnDeleteIngredient.setOnClickListener(v -> {
                // Get the ingredient ID and delete it (for future implementation)
                int ingredientId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                Toast.makeText(context, "Delete ingredient with ID: " + ingredientId, Toast.LENGTH_SHORT).show();
            });
        }

    }
}
