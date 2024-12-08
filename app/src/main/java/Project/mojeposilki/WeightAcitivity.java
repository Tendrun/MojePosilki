package Project.mojeposilki;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Locale;

public class WeightAcitivity extends AppCompatActivity {

    private MealDatabaseHelper dbHelper;
    private EditText editTextWeight;
    private EditText editTextHeight;
    private Button buttonCalculateBMI;
    private TextView textViewBMIResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityweight);

        // Initialize views
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        buttonCalculateBMI = findViewById(R.id.buttonCalculateBMI);
        textViewBMIResult = findViewById(R.id.textViewBMIResult);

        dbHelper = new MealDatabaseHelper(this);


        // Set click listener on the button
        buttonCalculateBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        setupLineChart();
        makeLink();
    }

    void makeLink(){
        TextView textView = findViewById(R.id.textView);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://pacjent.gov.pl/aktualnosc/wroc-do-formy-po-covid-19-w-8-tygodni'> Cwiczenia " +
                "Aby wrócić do formy </a>";
        textView.setText(Html.fromHtml(text));
    }

    private void setupLineChart() {
        List<BarEntry> weightEntries = new ArrayList<>();
        List<String> dateLabels = new ArrayList<>();

        int i = 0;

        Cursor cursor = dbHelper.getAllWeightRecords(); // Call your method to get weight records
        if (cursor != null && cursor.moveToFirst()) {
            do {
                float weight = cursor.getFloat(0); // Weight is the first column in the SELECT
                long dateInMillis = cursor.getLong(1); // Date in milliseconds

                // Add to the weight entries
                weightEntries.add(new BarEntry(i++, weight)); // X = Index, Y = Weight

                // Format the date for labels
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String formattedDate = sdf.format(new Date(dateInMillis));
                dateLabels.add(formattedDate);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Initialize the BarChart
        BarChart barChart = findViewById(R.id.barchart);

        // Create BarDataSet
        BarDataSet barDataSet = new BarDataSet(weightEntries, "Weight (kg)");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Customize the BarDataSet
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        // Set X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels)); // Map index to date labels
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Place X-axis labels at the bottom
        xAxis.setGranularity(1f); // Ensure one label per bar
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false); // Optional: Remove grid lines

        // Disable the right Y-axis (optional)
        barChart.getAxisRight().setEnabled(false);

        // Enable the left Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f); // Set granularity for weight values

        // Customize the chart
        barChart.getDescription().setEnabled(false); // Remove chart description
        barChart.setFitBars(true); // Make bars fit within the view
        barChart.animateY(1000); // Animate the chart

        // Refresh the chart
        barChart.invalidate(); // Redraw the chart
    }

    private void calculateBMI() {
        // Get input values
        String weightText = editTextWeight.getText().toString();
        String heightText = editTextHeight.getText().toString();

        // Validate inputs
        if (weightText.isEmpty() || heightText.isEmpty()) {
            Toast.makeText(this, "Please enter both weight and height!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Convert inputs to numeric values
            float weight = Float.parseFloat(weightText);
            float heightInCm = Float.parseFloat(heightText);

            // Convert height from cm to meters
            float heightInMeters = heightInCm / 100;

            // Calculate BMI
            float bmi = weight / (heightInMeters * heightInMeters);

            // Display the BMI result
            textViewBMIResult.setText(String.format("Your BMI is: %.2f", bmi));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input! Please enter numeric values.", Toast.LENGTH_SHORT).show();
        }
    }
}
