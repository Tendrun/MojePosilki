package Project.mojeposilki;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import Project.mojeposilki.MealDatabaseHelper;

public class ExerciseActivity extends AppCompatActivity {

    private Spinner spinnerExerciseType;
    private TextView textViewSelectedDate;
    private Button buttonSelectDate, buttonSubmitExercise;

    private String selectedExercise = "Running";
    private long selectedDateInMillis = -1;
    MealDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        dbHelper = new MealDatabaseHelper(this);

        spinnerExerciseType = findViewById(R.id.spinnerExerciseType);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        buttonSubmitExercise = findViewById(R.id.buttonSubmitExercise);

        // Set up the spinner with exercise types
        String[] exerciseTypes = {"Running", "Training", "Cycling", "Yoga"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exerciseTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExerciseType.setAdapter(adapter);

        spinnerExerciseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExercise = exerciseTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedExercise = exerciseTypes[0];
            }
        });

        // Date picker logic
        buttonSelectDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ExerciseActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        selectedDateInMillis = calendar.getTimeInMillis();
                        textViewSelectedDate.setText("Selected Date: " + dayOfMonth + "-" + (month + 1) + "-" + year);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Submit button logic
        buttonSubmitExercise.setOnClickListener(v -> {
            if (selectedDateInMillis == -1) {
                Toast.makeText(getBaseContext(), "Please select a date!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate calories burned
            double caloriesBurned = calculateCalories(selectedExercise);

            // Save to database
            saveExerciseData(selectedExercise, selectedDateInMillis, caloriesBurned);

            // Show result
            Toast.makeText(this, "Calories burned: " + caloriesBurned, Toast.LENGTH_SHORT).show();
        });
    }

    private double calculateCalories(String exerciseType) {
        switch (exerciseType) {
            case "Running":
                return 600; // Example: Running burns 600 calories/hour
            case "Training":
                return 400; // Training burns 400 calories/hour
            case "Cycling":
                return 500; // Cycling burns 500 calories/hour
            case "Yoga":
                return 200; // Yoga burns 200 calories/hour
            default:
                return 0;
        }
    }

    private void saveExerciseData(String exerciseType, long dateInMillis, double caloriesBurned) {
        // Insert into database
        dbHelper.insertExerciseRecord(exerciseType, dateInMillis, caloriesBurned);
    }
}
