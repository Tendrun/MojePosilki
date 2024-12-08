package Project.mojeposilki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.tabs.TabLayout;

public class MealDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meals.db";
    private static final int DATABASE_VERSION = 17;


    // Table for meals
    private static final String TABLE_MEALS = "meals";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_MEAL_NAME = "meal_name";
    private static final String COLUMN_MEAL_DESCRIPTION = "description";


    // Table for ingredients
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String COLUMN_INGREDIENT_ID = "_id";
    private static final String COLUMN_MEAL_ID = "meal_id"; // Optional foreign key, can be null
    private static final String COLUMN_SKLADNIK = "skladnik";
    private static final String COLUMN_ILOSC = "ilosc";
    private static final String COLUMN_JEDNOSTKA = "jednostka";
    private static final String COLUMN_KATERGORIA = "kategoria";
    private static final String COLUMN_IS_BOUGHT = "is_bought";
    private static final String COLUMN_AT_HOME = "is_at_home";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_PROTEIN = "protein";
    private static final String COLUMN_FATS = "fats";
    private static final String COLUMN_CARBOHYDRATES = "carbohydrates";





    // Table for Calendar
    private static final String TABLE_CALENDAR = "Calendar";
    private static final String COLUMN_Calendar_ID = "_id";
    private static final String COLUMN_MEAL_ID_Foreign = "meal_id"; // Optional foreign key, can be null
    private static final String COLUMN_DATE_CALENDAR = "meal_date"; // date in milliseconds


    // Table for Weight

    private static final String TABLE_WEIGHT = "Weight";
    private static final String COLUMN_Weight_ID = "_id";
    private static final String COLUMN_WEIGHT = "weightKG";

    public MealDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

        // Create meals table
    String CREATE_MEALS_TABLE = "CREATE TABLE " + TABLE_MEALS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MEAL_NAME + " TEXT,"
            + COLUMN_MEAL_DESCRIPTION + " TEXT"
            + ")";

    // Create ingredients table with optional foreign key (meal_id can be NULL)
    // New column to track if the ingredient is bought

    // Update the CREATE_INGREDIENTS_TABLE with the new column
    String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
            + COLUMN_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MEAL_ID + " INTEGER,"
            + COLUMN_SKLADNIK + " TEXT,"
            + COLUMN_KATERGORIA + " TEXT,"
            + COLUMN_ILOSC + " TEXT,"
            + COLUMN_JEDNOSTKA + " TEXT,"
            + COLUMN_FATS + " TEXT,"
            + COLUMN_CARBOHYDRATES + " TEXT,"
            + COLUMN_PROTEIN + " TEXT,"
            + COLUMN_CALORIES + " TEXT,"
            + COLUMN_AT_HOME + " INTEGER DEFAULT 0,"  // New column with default unchecked state
            + COLUMN_IS_BOUGHT + " INTEGER DEFAULT 0,"  // New column with default unchecked state
            + "FOREIGN KEY(" + COLUMN_MEAL_ID + ") REFERENCES " + TABLE_MEALS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
            + ")";


    // Create Calendar table
    // Create calendar table (correcting foreign key reference)
    String CREATE_CALENDAR_TABLE = "CREATE TABLE " + TABLE_CALENDAR + "("
            + COLUMN_Calendar_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MEAL_ID_Foreign + " INTEGER,"
            + COLUMN_DATE_CALENDAR + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_MEAL_ID_Foreign + ") REFERENCES " + TABLE_MEALS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
            + ")";

    String CREATE_WEIGHT_TABLE = "CREATE TABLE " + TABLE_WEIGHT + "("
            + COLUMN_Weight_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // Unique ID for the weight entry
            + COLUMN_WEIGHT + " REAL," // Weight in kilograms (use REAL for decimal values)
            + COLUMN_DATE_CALENDAR + " INTEGER," // Date for the weight entry, stored as milliseconds
            + "FOREIGN KEY(" + COLUMN_DATE_CALENDAR + ") REFERENCES " + TABLE_CALENDAR + "(" + COLUMN_DATE_CALENDAR + ")"
            + ")";


    // Table for Exercises
    private static final String TABLE_EXERCISES = "exercises";
    private static final String COLUMN_EXERCISE_ID = "_id";
    private static final String COLUMN_EXERCISE_TYPE = "exercise_type";
    private static final String COLUMN_EXERCISE_DATE = "exercise_date"; // Date in milliseconds
    private static final String COLUMN_CALORIES_BURNED = "calories_burned";

    String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES + "("
            + COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EXERCISE_TYPE + " TEXT,"
            + COLUMN_EXERCISE_DATE + " INTEGER,"
            + COLUMN_CALORIES_BURNED + " REAL"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MEALS_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE);
        db.execSQL(CREATE_CALENDAR_TABLE);
        db.execSQL(CREATE_WEIGHT_TABLE);
        db.execSQL(CREATE_EXERCISES_TABLE);
    }



    public void DeleteAllTables(){
        System.out.println("Deleting Database");

        SQLiteDatabase db = this.getWritableDatabase();

        //Delete Tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);

        onCreate(db);

        System.out.println("Deleting Database Completed");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);


        onCreate(db);
    }

    // Method to add a meal to the database (meal name with optional date)
    public long addMeal(String mealName, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_NAME, mealName);
        values.put(COLUMN_MEAL_DESCRIPTION, description);

        // Insert and return the new meal ID
        long mealId = db.insert(TABLE_MEALS, null, values);
        db.close();
        return mealId;
    }

    public Cursor getAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_INGREDIENTS;
        return db.rawQuery(query, null);
    }


    public void deleteMeal(long mealId) {
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


    // Method to update a meal in the database
    public int updateMeal(long id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.update(TABLE_MEALS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();  // Close the database connection after update
        return rowsAffected;
    }



    public void addIngredient(String skladnik, String ilosc, String jednostka, String kategoria,
            String fat, String carbo, String protein, String MealID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SKLADNIK, skladnik);
        values.put(COLUMN_ILOSC, ilosc);
        values.put(COLUMN_JEDNOSTKA, jednostka);
        values.put(COLUMN_KATERGORIA, kategoria);
        values.put(COLUMN_FATS, fat);
        values.put(COLUMN_CARBOHYDRATES, carbo);
        values.put(COLUMN_PROTEIN, protein);
        values.put(COLUMN_CALORIES, String.valueOf(countCalories(carbo, fat, protein)));

        values.put(COLUMN_MEAL_ID, MealID);


        // Insert and return the new meal ID
        db.insert(TABLE_INGREDIENTS, null, values);
        db.close();
    }

    int countCalories(String carbo, String fat, String protein){
        System.out.println("carbo " + carbo + " fat " + " protein " + protein);
        return Integer.parseInt(carbo) * 4 + Integer.parseInt(fat) * 8
                + Integer.parseInt(protein) * 4;
    }

    public void printAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INGREDIENTS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_INGREDIENT_ID));
                String skladnik = cursor.getString(cursor.getColumnIndex(COLUMN_SKLADNIK));
                String ilosc = cursor.getString(cursor.getColumnIndex(COLUMN_ILOSC));
                String jednostka = cursor.getString(cursor.getColumnIndex(COLUMN_JEDNOSTKA));
                String kategoria = cursor.getString(cursor.getColumnIndex(COLUMN_KATERGORIA));
                String MealID = cursor.getString(cursor.getColumnIndex(COLUMN_MEAL_ID));
/*
                // Print each ingredient's details
                System.out.println("ID: " + id + ", Skladnik: " + skladnik + ", Ilosc: " + ilosc +
                        ", Jednostka: " + jednostka + ", Kategoria: " + kategoria + "COLUMN_MEAL_ID " +
                        "= " + MealID);
*/
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    // Method to get all ingredients for meals on a specific date
    public Cursor getIngredientsByDate(long dateInMillis, String category) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        String[] selectionArgs;

        if ("All Categories".equals(category)) {
            // If "All Categories" is selected, exclude category filter
            query = "SELECT i." + COLUMN_INGREDIENT_ID + ", i." + COLUMN_SKLADNIK + ", i." + COLUMN_KATERGORIA +
                    " FROM " + TABLE_INGREDIENTS + " i" +
                    " JOIN " + TABLE_CALENDAR + " c ON i." + COLUMN_MEAL_ID + " = c." + COLUMN_MEAL_ID_Foreign +
                    " WHERE c." + COLUMN_DATE_CALENDAR + " = ?";
            selectionArgs = new String[] { String.valueOf(dateInMillis) };
        } else {
            // Filter by the hardcoded "Meat" category
            query = "SELECT i." + COLUMN_INGREDIENT_ID + ", i." + COLUMN_SKLADNIK + ", i." + COLUMN_KATERGORIA +
                    " FROM " + TABLE_INGREDIENTS + " i" +
                    " JOIN " + TABLE_CALENDAR + " c ON i." + COLUMN_MEAL_ID + " = c." + COLUMN_MEAL_ID_Foreign +
                    " WHERE c." + COLUMN_DATE_CALENDAR + " = ?" +
                    " AND TRIM(i." + COLUMN_KATERGORIA + ") = ? COLLATE NOCASE";
            selectionArgs = new String[] { String.valueOf(dateInMillis), category };
            System.out.println("QUERY WITH HARD-CODED CATEGORY: " + query);
        }

        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getNutritionalInfoCursorByDate(long dateInMillis) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT i." + COLUMN_INGREDIENT_ID + " AS _id, " +
                "SUM(i." + COLUMN_CALORIES + ") AS total_calories, " +
                "SUM(i." + COLUMN_PROTEIN + ") AS total_protein, " +
                "SUM(i." + COLUMN_FATS + ") AS total_fats, " +
                "SUM(i." + COLUMN_CARBOHYDRATES + ") AS total_carbohydrates " +
                "FROM " + TABLE_INGREDIENTS + " i " +
                "JOIN " + TABLE_CALENDAR + " c ON i." + COLUMN_MEAL_ID + " = c." + COLUMN_MEAL_ID_Foreign + " " +
                "WHERE c." + COLUMN_DATE_CALENDAR + " = ?";

        return db.rawQuery(query, new String[]{String.valueOf(dateInMillis)});
    }

    public void deleteMealAndRelatedData(long mealId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction(); // Start transaction for safe operations

            // Delete all related entries in the Calendar table
            int calendarRowsDeleted = db.delete(TABLE_CALENDAR, COLUMN_MEAL_ID_Foreign + "=?", new String[]{String.valueOf(mealId)});
            System.out.println("Deleted " + calendarRowsDeleted + " entries from Calendar table.");

            // Delete all related entries in the Ingredients table
            int ingredientRowsDeleted = db.delete(TABLE_INGREDIENTS, COLUMN_MEAL_ID + "=?", new String[]{String.valueOf(mealId)});
            System.out.println("Deleted " + ingredientRowsDeleted + " ingredients associated with meal ID " + mealId);

            db.setTransactionSuccessful(); // Commit transaction
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        } finally {
            db.endTransaction(); // End transaction
            db.close(); // Close database
        }
    }


    // Method to delete an ingredient by its ID
    public void deleteIngredientById(long ingredientId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction(); // Start a transaction for safe operation

            // Check for related calendar entries
            String calendarQuery = "SELECT * FROM " + TABLE_CALENDAR + " WHERE " + COLUMN_MEAL_ID_Foreign +
                    " = (SELECT " + COLUMN_MEAL_ID + " FROM " + TABLE_INGREDIENTS + " WHERE " +
                    COLUMN_INGREDIENT_ID + " = ?)";
            Cursor cursor = db.rawQuery(calendarQuery, new String[]{String.valueOf(ingredientId)});
            if (cursor != null && cursor.moveToFirst()) {
                // If related rows exist in the Calendar table
                System.out.println("Found related calendar entries for ingredient ID: " + ingredientId);
            }

            // Delete the ingredient
            int rowsDeleted = db.delete(TABLE_INGREDIENTS, COLUMN_INGREDIENT_ID + "=?", new String[]{String.valueOf(ingredientId)});
            if (rowsDeleted > 0) {
                System.out.println("Ingredient with ID " + ingredientId + " deleted successfully.");
            } else {
                System.out.println("No ingredient found with ID " + ingredientId);
            }

            db.setTransactionSuccessful(); // Commit the transaction
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions
        } finally {
            db.endTransaction(); // End the transaction
            db.close(); // Close the database connection
        }
    }


    // Get checkbox state for a specific ingredient ID
    public boolean isIngredientBought(long ingredientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_IS_BOUGHT + " FROM " + TABLE_INGREDIENTS + " WHERE " + COLUMN_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
        boolean isBought = false;
        if (cursor.moveToFirst()) {
            isBought = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_BOUGHT)) == 1;
        }
        cursor.close();
        return isBought;
    }

    // Set checkbox state for a specific ingredient ID
    public void setIngredientBought(long ingredientId, boolean isBought) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_BOUGHT, isBought ? 1 : 0);
        db.update(TABLE_INGREDIENTS, values, COLUMN_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
    }

    public boolean isIngredientAtHome(long ingredientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_AT_HOME + " FROM " + TABLE_INGREDIENTS + " WHERE " + COLUMN_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
        boolean AtHome = false;
        if (cursor.moveToFirst()) {
            AtHome = cursor.getInt(cursor.getColumnIndex(COLUMN_AT_HOME)) == 1;
        }
        cursor.close();
        return AtHome;
    }

    public void setIngredientAtHome(long ingredientId, boolean AtHome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AT_HOME, AtHome ? 1 : 0);
        db.update(TABLE_INGREDIENTS, values, COLUMN_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
    }

    public Cursor getAllWeightRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_WEIGHT + ", " + COLUMN_DATE_CALENDAR +
                " FROM " + TABLE_WEIGHT +
                " ORDER BY " + COLUMN_DATE_CALENDAR + " ASC", null);
    }

    public void addWeight(double weight, long dateInMillis) {

        System.out.println("TUTAJJJ");
        System.out.println(weight + "dateInMillis" + dateInMillis);


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEIGHT, weight);  // Foreign key linking to the meals table
        values.put(COLUMN_DATE_CALENDAR, dateInMillis);  // Store the date in milliseconds

        db.insert(TABLE_WEIGHT, null, values);

        // Insert and return the new calendar event ID
        db.close();
    }

    public void insertExerciseRecord(String exerciseType, long dateInMillis, double caloriesBurned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_TYPE, exerciseType);
        values.put(COLUMN_EXERCISE_DATE, dateInMillis);
        values.put(COLUMN_CALORIES_BURNED, caloriesBurned);
        db.insert(TABLE_EXERCISES, null, values);
    }


}
