package com.gachon.mp_termproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calcounter.R;

import com.gachon.mp_termproject.Activity.DisplayFoodsActivity;
import com.gachon.mp_termproject.data.DatabaseHandler;
import com.gachon.mp_termproject.model.Food;


public class CalculatorActivity extends AppCompatActivity {

    private EditText foodName, foodCals;
    private Button submitButton;
    private ImageButton view;
    private DatabaseHandler dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        dba = new DatabaseHandler(CalculatorActivity.this);
        foodName = findViewById(R.id.foodEditText);
        foodCals = findViewById(R.id.caloriesEditText);
        submitButton = findViewById(R.id.submitButton);
        view = findViewById(R.id.imageButton2);

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalculatorActivity.this, DisplayFoodsActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDB();
            }
        });
    }

    private void saveDataToDB() {
        Food food = new Food();
        String name = foodName.getText().toString().trim();
        String calsString = foodCals.getText().toString();

        int cals = Integer.parseInt(calsString);

        if (name.isEmpty() || calsString.isEmpty()) {
            Toast.makeText(getApplicationContext(), "음식과 칼로리를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else {
            food.setFoodName(name);
            food.setCalories(cals);

            dba.addFood(food);
            dba.close();

            // clear the form
            foodName.setText("");
            foodCals.setText("");

        }
    }
}