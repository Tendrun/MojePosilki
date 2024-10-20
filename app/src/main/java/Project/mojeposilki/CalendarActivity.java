package Project.mojeposilki;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private EditText etMealName;
    private DatePicker datePicker;
    private Button btnAddToCalendar;
    private MealDatabaseHelper mealDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


       /*

        etMealName = findViewById(R.id.et_meal_name);
        datePicker = findViewById(R.id.datePicker);
        btnAddToCalendar = findViewById(R.id.btn_add_to_calendar);


        // Initialize the database helper
        mealDatabaseHelper = new MealDatabaseHelper(this);

        btnAddToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMealToCalendar();
            }
        });
    }

    private void addMealToCalendar() {
        String mealName = etMealName.getText().toString().trim();

        if (mealName.isEmpty()) {
            Toast.makeText(this, "Please enter a meal name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the selected date from the DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        long startTimeInMillis = cal.getTimeInMillis();





        // Save the meal and date to the database
        mealDatabaseHelper.addMeal(mealName, startTimeInMillis);





        // Add meal to the calendar
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeInMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTimeInMillis + 60 * 60 * 1000) // 1 hour for meal
                .putExtra(CalendarContract.Events.TITLE, "Meal: " + mealName)
                .putExtra(CalendarContract.Events.DESCRIPTION, "Enjoy your meal!")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        Toast.makeText(this, "Meal saved to calendar and database!", Toast.LENGTH_SHORT).show();*/
    }
}



