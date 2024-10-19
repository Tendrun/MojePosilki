package Project.mojeposilki;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewMealsActivity extends AppCompatActivity {

    private ListView lvMeals;
    private MealDatabaseHelper mealDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meals);

        lvMeals = findViewById(R.id.lv_meals);
        mealDatabaseHelper = new MealDatabaseHelper(this);

        // Retrieve saved meals from the database (sorted alphabetically)
        Cursor cursor = mealDatabaseHelper.getAllMealsSorted();

        // Create a custom adapter to handle date formatting
        MealCursorAdapter adapter = new MealCursorAdapter(this, cursor, 0);

        // Set the adapter to the ListView
        lvMeals.setAdapter(adapter);

        // Set click listener for each meal item
        lvMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When a meal is clicked, show options to delete or modify
                Cursor clickedMealCursor = (Cursor) parent.getItemAtPosition(position);
                long mealId = clickedMealCursor.getLong(clickedMealCursor.getColumnIndex("_id"));
                showMealOptionsDialog(mealId);
            }
        });
    }

    // Method to show a dialog with options to delete or modify the meal
    private void showMealOptionsDialog(final long mealId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Meal Options");
        builder.setItems(new CharSequence[]{"Modify", "Delete"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Modify meal
                    Intent intent = new Intent(ViewMealsActivity.this, ModifyMealActivity.class);
                    intent.putExtra("mealId", mealId);
                    startActivity(intent);
                } else if (which == 1) {
                    // Delete meal
                    deleteMeal(mealId);
                }
            }
        });
        builder.show();
    }

    // Method to delete a meal from the database
    private void deleteMeal(long mealId) {
        mealDatabaseHelper.deleteMeal(mealId);
        // Refresh the list after deletion
        Cursor newCursor = mealDatabaseHelper.getAllMealsSorted();
        ((MealCursorAdapter) lvMeals.getAdapter()).changeCursor(newCursor);
        Toast.makeText(this, "Meal deleted", Toast.LENGTH_SHORT).show();
    }

    // Custom adapter for handling the conversion of date from milliseconds to human-readable form
    public class MealCursorAdapter extends android.widget.SimpleCursorAdapter {

        public MealCursorAdapter(Context context, Cursor c, int flags) {
            super(context, R.layout.meal_list_item, c, new String[]{
                    "meal_name", // Column name in database
                    "meal_date"  // Column name in database
            }, new int[]{
                    R.id.tv_meal_name,  // ID of TextView in meal_list_item.xml
                    R.id.tv_meal_date   // ID of TextView in meal_list_item.xml
            }, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find the TextViews in the meal_list_item.xml layout
            TextView mealNameTextView = view.findViewById(R.id.tv_meal_name);
            TextView mealDateTextView = view.findViewById(R.id.tv_meal_date);

            // Get the data from the cursor
            String mealName = cursor.getString(cursor.getColumnIndex("meal_name"));
            long mealDateMillis = cursor.getLong(cursor.getColumnIndex("meal_date"));

            // Set the meal name
            mealNameTextView.setText(mealName);

            // Format the meal date (from milliseconds) into a human-readable format
            String formattedDate = formatDate(mealDateMillis);
            mealDateTextView.setText(formattedDate);
        }

        // Helper method to format the date from milliseconds to "DD-MM-YYYY"
        private String formatDate(long millis) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date resultDate = new Date(millis);
            return sdf.format(resultDate);
        }
    }
}
