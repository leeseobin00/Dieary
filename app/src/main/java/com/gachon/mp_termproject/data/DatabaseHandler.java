package com.gachon.mp_termproject.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.gachon.mp_termproject.model.Food;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Food> foodList = new ArrayList<>();

    public DatabaseHandler(Context context) {
        super(context, com.gachon.mp_termproject.data.Constants.DATABASE_NAME, null, com.gachon.mp_termproject.data.Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + com.gachon.mp_termproject.data.Constants.TABLE_NAME + "("
                + com.gachon.mp_termproject.data.Constants.KEY_ID + " INTEGER PRIMARY KEY, " + com.gachon.mp_termproject.data.Constants.FOOD_NAME + " TEXT, "
                + com.gachon.mp_termproject.data.Constants.FOOD_CALORIES_NAME + " INT, " + com.gachon.mp_termproject.data.Constants.DATE_NAME + " lONG);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + com.gachon.mp_termproject.data.Constants.TABLE_NAME);

            // create a new one
        onCreate(db);
    }

    // Get total items saved.
    public int getTotalItems() {
        int totalItems = 0;
        String query = "SELECT * FROM " + com.gachon.mp_termproject.data.Constants.TABLE_NAME;
        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);
        totalItems = cursor.getCount();
        cursor.close();
        return totalItems;
    }

    // Get total calories consumed.
    public int totalCalories() {
        int cals = 0;
        SQLiteDatabase dba = this.getReadableDatabase();
        String query = "SELECT SUM( " + com.gachon.mp_termproject.data.Constants.FOOD_CALORIES_NAME + " ) " +
                "FROM " + com.gachon.mp_termproject.data.Constants.TABLE_NAME;

        Cursor cursor = dba.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cals = cursor.getInt(0);
        }
        cursor.close();
        dba.close();

        return cals;
    }

    // Delete food item
    public void deleteFood(int id) {
        SQLiteDatabase dba = this.getWritableDatabase();
        dba.delete(com.gachon.mp_termproject.data.Constants.TABLE_NAME, com.gachon.mp_termproject.data.Constants.KEY_ID + " =?", new String[] {String.valueOf(id)});
        dba.close();
    }

    // add food item
    public void addFood(Food food) {
        SQLiteDatabase dba = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(com.gachon.mp_termproject.data.Constants.FOOD_NAME, food.getFoodName());
        values.put(com.gachon.mp_termproject.data.Constants.FOOD_CALORIES_NAME, food.getCalories());
        values.put(com.gachon.mp_termproject.data.Constants.DATE_NAME, System.currentTimeMillis());

        dba.insert(com.gachon.mp_termproject.data.Constants.TABLE_NAME, null, values);

        dba.close();
    }

    // get all food
    @SuppressLint("Range")
    public ArrayList<Food> getFoods() {
        foodList.clear();
        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.query(com.gachon.mp_termproject.data.Constants.TABLE_NAME,
                new String[]{com.gachon.mp_termproject.data.Constants.KEY_ID, com.gachon.mp_termproject.data.Constants.FOOD_NAME,
                com.gachon.mp_termproject.data.Constants.FOOD_CALORIES_NAME, com.gachon.mp_termproject.data.Constants.DATE_NAME},
                null, null, null, null, com.gachon.mp_termproject.data.Constants.FOOD_NAME + " DESC ");
        //Loop through..
        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setFoodName(cursor.getString(cursor.getColumnIndex(com.gachon.mp_termproject.data.Constants.FOOD_NAME)));
                food.setCalories(cursor.getInt(cursor.getColumnIndex(com.gachon.mp_termproject.data.Constants.FOOD_CALORIES_NAME)));
                food.setFoodId(cursor.getInt(cursor.getColumnIndex(com.gachon.mp_termproject.data.Constants.KEY_ID)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(com.gachon.mp_termproject.data.Constants.DATE_NAME))).getTime());
                food.setRecordDate(date);
                foodList.add(food);

            }while (cursor.moveToNext());
        }
        cursor.close();
        dba.close();
        return foodList;
    }
}
