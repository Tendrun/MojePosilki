package Project.mojeposilki;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class WeightAcitivity extends AppCompatActivity {

    private LineChart lineChart;

    private MealDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityweight);

        dbHelper = new MealDatabaseHelper(this);

        setupLineChart();

    }

    private ArrayList<Entry> getWeightDataFromDatabase() {
        ArrayList<Entry> entries = new ArrayList<>();

        // Replace with your database helper instance
        MealDatabaseHelper dbHelper = new MealDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT weightKG, meal_date FROM Weight ORDER BY meal_date ASC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                float date = cursor.getFloat(cursor.getColumnIndex("meal_date")); // Convert if necessary
                float weight = cursor.getFloat(cursor.getColumnIndex("weightKG"));
                entries.add(new Entry(date, weight));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return entries;
    }

    private void setupLineChart() {
        ArrayList<BarEntry> weightEntries = new ArrayList<>();


        // Example data: Replace with your database query results
        weightEntries.add(new BarEntry(1, 65.5f)); // Day 1, Weight 65.5kg
        weightEntries.add(new BarEntry(2, 66.0f)); // Day 2, Weight 66.0kg
        weightEntries.add(new BarEntry(3, 64.8f)); // Day 3, Weight 64.8kg


        BarChart barChart = findViewById(R.id.barchart);
        BarDataSet barDataSet = new BarDataSet(weightEntries,"Date ");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        //color bar data set
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //text color
        barDataSet.setValueTextColor(Color.BLACK);

        //settting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(true);
    }
}
