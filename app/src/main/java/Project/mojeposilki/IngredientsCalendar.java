package Project.mojeposilki;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
    private NutritionalSummaryAdapter Nutriadapter;
    private TextView proteinView,caloriesView,fatsView,carbosView;

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

        // Get callories from this day

        Cursor cursor_calories = mealDatabaseHelper.getNutritionalInfoCursorByDate(dateInMillis);

        if (cursor_calories != null && cursor_calories.moveToFirst()) {
            do {
                // Retrieve each column's value
                int ingredientId = cursor_calories.getInt(cursor_calories.getColumnIndexOrThrow("_id"));
                int totalCalories = cursor_calories.getInt(cursor_calories.getColumnIndexOrThrow("total_calories"));
                int totalProtein = cursor_calories.getInt(cursor_calories.getColumnIndexOrThrow("total_protein"));
                int totalFats = cursor_calories.getInt(cursor_calories.getColumnIndexOrThrow("total_fats"));
                int totalCarbohydrates = cursor_calories.getInt(cursor_calories.getColumnIndexOrThrow("total_carbohydrates"));

                TextView tvTotalCalories = findViewById(R.id.tv_total_calories);
                TextView tvTotalProtein = findViewById(R.id.tv_total_protein);
                TextView tvTotalFats = findViewById(R.id.tv_total_fats);
                TextView tvTotalCarbohydrates = findViewById(R.id.tv_total_carbohydrates);

                // Set values to TextViews
                tvTotalCalories.setText("Total Calories " + String.valueOf(totalCalories));
                tvTotalProtein.setText("Total Protein " + String.valueOf(totalProtein));
                tvTotalFats.setText("Total Fats " + String.valueOf(totalFats));
                tvTotalCarbohydrates.setText("Total Carbohydrates " + String.valueOf(totalCarbohydrates));

                // Close the cursor
                cursor_calories.close();

            } while (cursor_calories.moveToNext()); // Move to the next row

            cursor_calories.close(); // Always close the Cursor after use
        }

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

            long ingredientId = cursor.getLong(cursor.getColumnIndex("_id"));




            // In the adapter's bindView or getView method:
            CheckBox checkBoxBought = view.findViewById(R.id.btn_mark_bought);

            // Get the ingredient ID and bought status from the cursor
            boolean isBought = mealDatabaseHelper.isIngredientBought(ingredientId);

            // Set checkbox state
            checkBoxBought.setChecked(isBought);

            // Update database when checkbox is toggled
            checkBoxBought.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mealDatabaseHelper.setIngredientBought(ingredientId, isChecked);
            });




            CheckBox checkAtHome = view.findViewById(R.id.btn_mark_athome);

            // Get the ingredient ID and bought status from the cursor
            boolean isAtHome = mealDatabaseHelper.isIngredientAtHome(ingredientId);

            // Set checkbox state
            checkAtHome.setChecked(isAtHome);

            // Update database when checkbox is toggled
            checkAtHome.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mealDatabaseHelper.setIngredientAtHome(ingredientId, isChecked);
            });


            // Set ingredient name
            String skladnik = cursor.getString(cursor.getColumnIndex("skladnik"));
            skladnikTextView.setText(skladnik);

            btnAddIngredient.setOnClickListener(v -> {
                // Navigate to AddRecipeActivity
                Intent intent = new Intent(context, AddRecipeActivity.class);
                context.startActivity(intent);
            });

            btnDeleteIngredient.setOnClickListener(v -> {
                // Get the ingredient ID and delete it (for future implementation)
                //int ingredientId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                Toast.makeText(context, "Delete ingredient with ID: " + ingredientId, Toast.LENGTH_SHORT).show();
                mealDatabaseHelper.deleteIngredientById(ingredientId);
                loadIngredientsForDateAndCategory();
            });
        }


    }
    public class NutritionalSummaryAdapter extends android.widget.SimpleCursorAdapter  {

        public NutritionalSummaryAdapter(Context context, Cursor c, int flags) {
            super(context, R.layout.ingridient_list_item, c, new String[]{
                    "total_calories",
                    "total_protein",
                    "total_fats",
                    "total_carbohydrates"
            }, new int[]{
                    R.id.tv_total_calories,
                    R.id.tv_total_protein,
                    R.id.tv_total_fats,
                    R.id.tv_total_carbohydrates
            }, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find views in the layout
            TextView totalCaloriesTextView = view.findViewById(R.id.tv_total_calories);
            TextView totalProteinTextView = view.findViewById(R.id.tv_total_protein);
            TextView totalFatsTextView = view.findViewById(R.id.tv_total_fats);
            TextView totalCarbohydratesTextView = view.findViewById(R.id.tv_total_carbohydrates);

            // Extract values from the cursor
            int totalCalories = cursor.getInt(cursor.getColumnIndex("total_calories"));
            int totalProtein = cursor.getInt(cursor.getColumnIndex("total_protein"));
            int totalFats = cursor.getInt(cursor.getColumnIndex("total_fats"));
            int totalCarbohydrates = cursor.getInt(cursor.getColumnIndex("total_carbohydrates"));

            // Bind data to the views
            totalCaloriesTextView.setText(String.valueOf(totalCalories));
            totalProteinTextView.setText(String.valueOf(totalProtein));
            totalFatsTextView.setText(String.valueOf(totalFats));
            totalCarbohydratesTextView.setText(String.valueOf(totalCarbohydrates));
        }
    }

}
