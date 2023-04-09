package com.gachon.mp_termproject.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calcounter.R;

import java.util.ArrayList;

import com.gachon.mp_termproject.data.CustomListViewAdapter;
import com.gachon.mp_termproject.data.DatabaseHandler;
import com.gachon.mp_termproject.model.Food;
import com.gachon.mp_termproject.util.Utils;

import com.gachon.mp_termproject.util.Utils;

public class DisplayFoodsActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private ListView listView;
    private Food myFood;
    private TextView totalCals, totalFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_foods);

        listView = findViewById(R.id.list);
        totalCals = findViewById(R.id.totalAmountTextView);
        totalFoods = findViewById(R.id.totalItemsTextView);

        refreshData();
    }

    private void refreshData() {
        dbFoods.clear();
        dba = new DatabaseHandler(getApplicationContext());
        ArrayList<Food> foodsFromDB = dba.getFoods();
        int calsValues = dba.totalCalories();
        int totalItems = dba.getTotalItems();

        String formattedValue = Utils.formatNumber(calsValues);
        String formattedItems = Utils.formatNumber(totalItems);

        totalCals.setText("Total Calories " + formattedValue);
        totalFoods.setText("Total Foods: " + formattedItems);

        for (int i = 0; i < foodsFromDB.size(); i++) {
            String name = foodsFromDB.get(i).getFoodName();
            String dateText = foodsFromDB.get(i).getRecordDate();
            int cals = foodsFromDB.get(i).getCalories();
            int foodId = foodsFromDB.get(i).getFoodId();

            Log.v("FOODS IDS ", String.valueOf(foodId));

            myFood = new Food();
            myFood.setFoodName(name);
            myFood.setRecordDate(dateText);
            myFood.setCalories(cals);
            myFood.setFoodId(foodId);

            dbFoods.add(myFood);

        }

        dba.close();

        //setup adapter
        foodAdapter = new CustomListViewAdapter(DisplayFoodsActivity.this, R.layout.list_row, dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();
    }

}