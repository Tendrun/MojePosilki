package Project.mojeposilki;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GoalsActivity extends AppCompatActivity {

    private EditText etWeightGoal, etCalorieGoal, etProteinGoal, etFatsGoal, etCarbsGoal;
    private Button btnSaveGoals;
    private MealDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        // Initialize views
        etWeightGoal = findViewById(R.id.et_weight_goal);
        etCalorieGoal = findViewById(R.id.et_calorie_goal);
        etProteinGoal = findViewById(R.id.et_protein_goal);
        etFatsGoal = findViewById(R.id.et_fats_goal);
        etCarbsGoal = findViewById(R.id.et_carbs_goal);
        btnSaveGoals = findViewById(R.id.btn_save_goals);

        dbHelper = new MealDatabaseHelper(this);

        // Load current goals (if any)
        long todayInMillis = System.currentTimeMillis();
        HealthGoal currentGoal = dbHelper.readHealthGoals();
        if (currentGoal != null) {
            etWeightGoal.setText(String.valueOf(currentGoal.getWeightGoal()));
            etCalorieGoal.setText(String.valueOf(currentGoal.getCalorieGoal()));
            etProteinGoal.setText(String.valueOf(currentGoal.getProteinGoal()));
            etFatsGoal.setText(String.valueOf(currentGoal.getFatsGoal()));
            etCarbsGoal.setText(String.valueOf(currentGoal.getCarbsGoal()));
        }

        // Save button click listener
        btnSaveGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double weightGoal = Double.parseDouble(etWeightGoal.getText().toString());
                    double calorieGoal = Double.parseDouble(etCalorieGoal.getText().toString());
                    double proteinGoal = Double.parseDouble(etProteinGoal.getText().toString());
                    double fatsGoal = Double.parseDouble(etFatsGoal.getText().toString());
                    double carbsGoal = Double.parseDouble(etCarbsGoal.getText().toString());

                    dbHelper.writeHealthGoals( weightGoal, calorieGoal, proteinGoal, fatsGoal, carbsGoal);
                    Toast.makeText(GoalsActivity.this, "Goals saved successfully!", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(GoalsActivity.this, "Please enter valid numbers!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
