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
    private static final String COLUMN_DATE = "meal_date"; // date in milliseconds

    // Table for ingredients
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String COLUMN_INGREDIENT_ID = "_id";
    private static final String COLUMN_MEAL_ID = "meal_id"; // Optional foreign key, can be null
    private static final String COLUMN_SKLADNIK = "skladnik";
    private static final String COLUMN_ILOSC = "ilosc";
    private static final String COLUMN_JEDNOSTKA = "jednostka";

    public MealDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create meals table
        String CREATE_MEALS_TABLE = "CREATE TABLE " + TABLE_MEALS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MEAL_NAME + " TEXT,"
                + COLUMN_DATE + " INTEGER"
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }

    //Sortowanie alfabetyczne
    public Cursor getAllMealsSorted() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MEALS + " ORDER BY " + COLUMN_MEAL_NAME + " ASC", null);
    }

    // Method to add a meal to the database (meal name with optional date)
    public long addMeal(String mealName, long dateInMillis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_NAME, mealName);
        values.put(COLUMN_DATE, dateInMillis);

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

        db.execSQL("DELETE FROM " + TABLE_MEALS);

        // First, delete all ingredients associated with the meal (if needed)
        db.execSQL("DELETE FROM " + TABLE_INGREDIENTS + " WHERE " + COLUMN_MEAL_ID + " = " + mealId);

        // Then, delete the meal itself
        db.delete(TABLE_MEALS, COLUMN_ID + "=?", new String[]{String.valueOf(mealId)});



        db.close();
    }



    // Method to add an ingredient to the database (without needing mealId initially)
    public void addIngredient(String skladnik, String ilosc, String jednostka) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SKLADNIK, skladnik);
        values.put(COLUMN_ILOSC, ilosc);
        values.put(COLUMN_JEDNOSTKA, jednostka);

        // Insert ingredient with meal_id being NULL initially
        db.insert(TABLE_INGREDIENTS, null, values);
        db.close();
    }

    // Method to update ingredients with a meal_id after the meal is added
    public void updateIngredientWithMealId(long ingredientId, long mealId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_ID, mealId); // Assign meal_id to the ingredient

        db.update(TABLE_INGREDIENTS, values, COLUMN_INGREDIENT_ID + "=?", new String[]{String.valueOf(ingredientId)});
        db.close();
    }

    // Method to retrieve all meals
    public Cursor getAllMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MEALS, null);
    }

    // Method to retrieve all ingredients
    public Cursor getAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_INGREDIENTS, null);
    }
}
