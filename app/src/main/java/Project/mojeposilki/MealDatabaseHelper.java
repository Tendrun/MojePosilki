package Project.mojeposilki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.tabs.TabLayout;

public class MealDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meals.db";
    private static final int DATABASE_VERSION = 1;


    // Table for meals
    private static final String TABLE_MEALS = "meals";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_MEAL_NAME = "meal_name";
    private static final String COLUMN_MEAL_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "meal_date"; // date in milliseconds


    // Table for ingredients
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String COLUMN_INGREDIENT_ID = "_id";
    private static final String COLUMN_MEAL_ID = "meal_id"; // Optional foreign key, can be null
    private static final String COLUMN_SKLADNIK = "skladnik";
    private static final String COLUMN_ILOSC = "ilosc";
    private static final String COLUMN_JEDNOSTKA = "jednostka";

    // Table for Calendar
    private static final String TABLE_CALENDAR = "Calendar";
    private static final String COLUMN_Calendar_ID = "_id";
    private static final String COLUMN_MEAL_ID_Foreign = "meal_id"; // Optional foreign key, can be null
    private static final String COLUMN_DATE_CALENDAR = "meal_date"; // date in milliseconds

    public MealDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {;

        // Create meals table
        String CREATE_MEALS_TABLE = "CREATE TABLE " + TABLE_MEALS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MEAL_NAME + " TEXT,"
                + COLUMN_MEAL_DESCRIPTION + " TEXT"
                + ")";
        db.execSQL(CREATE_MEALS_TABLE);

        // Create ingredients table with optional foreign key (meal_id can be NULL)
        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + COLUMN_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MEAL_ID + " INTEGER,"
                + COLUMN_SKLADNIK + " TEXT,"
                + COLUMN_ILOSC + " TEXT,"
                + COLUMN_JEDNOSTKA + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_MEAL_ID + ") REFERENCES " + TABLE_MEALS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_INGREDIENTS_TABLE);

        // Create Calendar table
        // Create calendar table (correcting foreign key reference)
        String CREATE_CALENDAR_TABLE = "CREATE TABLE " + TABLE_CALENDAR + "("
                + COLUMN_Calendar_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MEAL_ID_Foreign + " INTEGER,"
                + COLUMN_DATE_CALENDAR + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_MEAL_ID_Foreign + ") REFERENCES " + TABLE_MEALS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_CALENDAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }

    // Method to add a meal to the database (meal name with optional date)
    public long addMeal(String mealName, long dateInMillis, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_NAME, mealName);
        values.put(COLUMN_MEAL_DESCRIPTION, description);
        //values.put(COLUMN_DATE, null);

        // Insert and return the new meal ID
        long mealId = db.insert(TABLE_MEALS, null, values);
        db.close();
        return mealId;
    }

    public void deleteMeal(long mealId) {

        for (int i = 0; i < 100; i++) {
            System.out.println(mealId);
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // First, delete all ingredients associated with the meal (if needed)
        db.execSQL("DELETE FROM " + TABLE_INGREDIENTS + " WHERE " + COLUMN_MEAL_ID + " = " + mealId);

        // Then, delete the meal itself
        db.delete(TABLE_MEALS, COLUMN_ID + "=?", new String[]{String.valueOf(mealId)});


        db.close();
    }

    public long addMealToCalendar(long mealId, long dateInMillis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_ID_Foreign, mealId);  // Foreign key linking to the meals table
        values.put(COLUMN_DATE_CALENDAR, dateInMillis);  // Store the date in milliseconds

        // Insert and return the new calendar event ID
        long calendarId = db.insert(TABLE_CALENDAR, null, values);
        db.close();
        return calendarId;
    }

    public Cursor getAllMealsDates() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to join meals and calendar tables
        String query = "SELECT " + TABLE_MEALS + "." + COLUMN_MEAL_NAME + ", "
                + TABLE_CALENDAR + "." + COLUMN_DATE_CALENDAR
                + " FROM " + TABLE_CALENDAR
                + " INNER JOIN " + TABLE_MEALS
                + " ON " + TABLE_CALENDAR + "." + COLUMN_MEAL_ID_Foreign + " = " + TABLE_MEALS + "." + COLUMN_ID;

        // Return the cursor containing the meal names and dates
        return db.rawQuery(query, null);
    }

    // Get all meals sorted alphabetically
    // Get all meals sorted alphabetically (A-Z)
    public Cursor getAllMealsWithDatesSortedByNameAsc() {
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to join meals and calendar tables
        String query = "SELECT "
                + TABLE_MEALS + "." + COLUMN_ID + ", " // Include COLUMN_ID from TABLE_MEALS
                + TABLE_MEALS + "." + COLUMN_MEAL_NAME + ", "
                + TABLE_CALENDAR + "." + COLUMN_DATE_CALENDAR + " "
                + "FROM " + TABLE_MEALS + " "
                + "LEFT JOIN " + TABLE_CALENDAR + " "
                + "ON " + TABLE_MEALS + "." + COLUMN_ID + " = " + TABLE_CALENDAR + "." + COLUMN_MEAL_ID_Foreign + " "
                + "ORDER BY " + TABLE_MEALS + "." + COLUMN_MEAL_NAME + " ASC";

        // Return the cursor containing the meal names and dates
        return db.rawQuery(query, null);
    }

    public Cursor getAllMealsWithDatesSortedByNameDESC() {
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to join meals and calendar tables
        String query = "SELECT "
                + TABLE_MEALS + "." + COLUMN_ID + ", " // Include COLUMN_ID from TABLE_MEALS
                + TABLE_MEALS + "." + COLUMN_MEAL_NAME + ", "
                + TABLE_CALENDAR + "." + COLUMN_DATE_CALENDAR + " "
                + "FROM " + TABLE_MEALS + " "
                + "LEFT JOIN " + TABLE_CALENDAR + " "
                + "ON " + TABLE_MEALS + "." + COLUMN_ID + " = " + TABLE_CALENDAR + "." + COLUMN_MEAL_ID_Foreign + " "
                + "ORDER BY " + TABLE_MEALS + "." + COLUMN_MEAL_NAME + " DESC";

        // Return the cursor containing the meal names and dates
        return db.rawQuery(query, null);
    }


    public Cursor getAllMealsSortedByNameAsc() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MEALS, null, null, null, null, null, COLUMN_MEAL_NAME + " ASC");
    }

    // Get all meals sorted in reverse alphabetical order (Z-A)
    public Cursor getAllMealsSortedByNameDesc() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MEALS, null, null, null, null, null, COLUMN_MEAL_NAME + " DESC");
    }

    // Method to retrieve a meal by its ID
    public Cursor getMealById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MEALS, null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    // Method to update a meal in the database
    public int updateMeal(long id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.update(TABLE_MEALS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();  // Close the database connection after update
        return rowsAffected;
    }

}
