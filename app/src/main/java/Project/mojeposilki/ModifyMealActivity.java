package Project.mojeposilki;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyMealActivity extends AppCompatActivity {

    private MealDatabaseHelper mealDatabaseHelper;
    private EditText etMealName;
    private long mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_meal);

        etMealName = findViewById(R.id.et_meal_name);
        mealDatabaseHelper = new MealDatabaseHelper(this);

        // Get the mealId passed from the previous activity
        mealId = getIntent().getLongExtra("mealId", -1);

        // Load meal details into the EditText (if needed)
        loadMealDetails();

        // Save modified meal on button click (you can add a save button in your layout)
        findViewById(R.id.btn_save).setOnClickListener(v -> saveModifiedMeal());
    }

    private void loadMealDetails() {
        // Logic to load meal details and populate the EditText (optional)
    }

    private void saveModifiedMeal() {
        String newMealName = etMealName.getText().toString();

        if (!newMealName.isEmpty()) {
            SQLiteDatabase db = mealDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("meal_name", newMealName);

            db.update("meals", values, "_id = ?", new String[]{String.valueOf(mealId)});
            Toast.makeText(this, "Meal modified", Toast.LENGTH_SHORT).show();
            finish(); // Return to the previous activity
        } else {
            Toast.makeText(this, "Meal name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}

