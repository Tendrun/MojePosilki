package Project.mojeposilki;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddWeightActivity extends AppCompatActivity {
    private TextInputLayout WeightTextLayout;
    private Button addWeightButton;

    private MealDatabaseHelper dbHelper;
    private DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_weight_activity);

        WeightTextLayout = findViewById(R.id.textFieldLayout);
        addWeightButton = findViewById(R.id.addWeightButton);
        datePicker = findViewById(R.id.datePicker);


        dbHelper = new MealDatabaseHelper(this);


        // Add new product when button is clicked
        addWeightButton.setOnClickListener(v -> saveWeight());
        System.out.println("przed");

    }

    private void saveWeight() {

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        String WeightValue = WeightTextLayout.getEditText().getText().toString().trim();

        if (WeightValue.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("HEERERE " + WeightValue);
        long dateInMillis = calendar.getTimeInMillis();
        dbHelper.addWeight(Double.parseDouble(WeightValue), dateInMillis);

        Toast.makeText(this, "Weight saved to database!", Toast.LENGTH_SHORT).show();
    }
}
