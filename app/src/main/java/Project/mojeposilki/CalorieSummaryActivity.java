package Project.mojeposilki;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalorieSummaryActivity extends AppCompatActivity {

    private MealDatabaseHelper dbHelper;
    private TextView tvStartDate, tvEndDate, tvCalories,  tvProtein, tvFats, tvCarbs, tv_Exercises_List;
    private Button btnCalculate;

    private long startDateInMillis = 0;
    private long endDateInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_count);

        dbHelper = new MealDatabaseHelper(this);

        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        tvCalories = findViewById(R.id.tv_calories);
        btnCalculate = findViewById(R.id.btn_calculate);
        tvProtein = findViewById(R.id.tv_protein);
        tvFats = findViewById(R.id.tv_fats);
        tvCarbs = findViewById(R.id.tv_carbs);
        tv_Exercises_List = findViewById(R.id.tv_Exercises_List);


        // Set listeners for start and end date pickers
        tvStartDate.setOnClickListener(v -> showDatePicker((dateInMillis) -> {
            startDateInMillis = dateInMillis;
            tvStartDate.setText(formatDate(dateInMillis));
        }));

        tvEndDate.setOnClickListener(v -> showDatePicker((dateInMillis) -> {
            endDateInMillis = dateInMillis;
            tvEndDate.setText(formatDate(dateInMillis));
        }));

        // Button to calculate calories in the selected range
        btnCalculate.setOnClickListener(v -> {
            if (startDateInMillis > 0 && endDateInMillis > 0 && startDateInMillis <= endDateInMillis) {
                NutritionalInfo nutritionalInfo = dbHelper.getNutritionalInfoByRange(startDateInMillis, endDateInMillis);
                double weightChange = dbHelper.getWeightChange(startDateInMillis, endDateInMillis);
                String Exercises = dbHelper.getExercisesFromDates(startDateInMillis, endDateInMillis);


                tvCalories.setText("Total Calories: " + nutritionalInfo.calories + " kcal");
                tvProtein.setText("Total Protein: " + nutritionalInfo.protein + " g");
                tvFats.setText("Total Fats: " + nutritionalInfo.fats + " g");
                tvCarbs.setText("Total Carbs: " + nutritionalInfo.carbohydrates + " g");

                TextView tvWeightChange = findViewById(R.id.tv_weight_change);
                tvWeightChange.setText("Weight Change: " + weightChange + " kg");

                tv_Exercises_List.setText("Exercises: " + Exercises);

            } else {
                tvCalories.setText("Please select a valid date range.");
            }
        });
    }

    private void showDatePicker(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            // Normalize to the start of the day
            selectedDate.set(Calendar.HOUR_OF_DAY, 0);
            selectedDate.set(Calendar.MINUTE, 0);
            selectedDate.set(Calendar.SECOND, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);

            listener.onDateSelected(selectedDate.getTimeInMillis());
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String formatDate(long dateInMillis) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dateInMillis);
    }

    // Interface for date selection callback
    private interface OnDateSelectedListener {
        void onDateSelected(long dateInMillis);
    }
}
