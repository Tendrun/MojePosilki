package Project.mojeposilki;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private ListView lvMeals;
    private MealDatabaseHelper mealDatabaseHelper;
    private MealCursorAdapter adapter;
    private DatePicker datePicker;

    long mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        lvMeals = findViewById(R.id.lv_meals);
        datePicker = findViewById(R.id.datePicker);

        mealDatabaseHelper = new MealDatabaseHelper(this);

        // Load meals and display them in the ListView
        loadMeals();
        // Set an item click listener for when a meal is selected from the list
        lvMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                mealId = cursor.getLong(cursor.getColumnIndex("_id"));
                String mealName = cursor.getString(cursor.getColumnIndex("meal_name"));
                Toast.makeText(CalendarActivity.this, "Selected meal: " + mealName, Toast.LENGTH_SHORT).show();
                // Handle meal selection logic if needed
            }
        });

        // Button to add the selected meal to the calendar
        findViewById(R.id.btn_add_to_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMealToCalendar(mealId);
            }
        });
    }

    // Load meals from the database and bind them to the ListView
    private void loadMeals() {
        Cursor cursor = mealDatabaseHelper.getAllMealsSortedByNameAsc();

        if (adapter == null) {
            adapter = new MealCursorAdapter(this, cursor, 0);
            lvMeals.setAdapter(adapter);
        } else {
            adapter.changeCursor(cursor);
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
        //TODO
        dbHelper.addMealToCalendar(mealId, calendar.getTimeInMillis());

        Toast.makeText(this, "Meal added to calendar", Toast.LENGTH_SHORT).show();
    }

    // Custom adapter for the ListView
    public class MealCursorAdapter extends android.widget.SimpleCursorAdapter {

        public MealCursorAdapter(Context context, Cursor c, int flags) {
            super(context, R.layout.meal_list_item, c, new String[]{
                    "meal_name" // Column name in database
            }, new int[]{
                    R.id.tv_meal_name  // ID of TextView in meal_list_item.xml
            }, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView mealNameTextView = view.findViewById(R.id.tv_meal_name);

            // Get meal name from the cursor
            String mealName = cursor.getString(cursor.getColumnIndex("meal_name"));

            // Set meal name to TextView
            mealNameTextView.setText(mealName);
        }
    }
}
