package com.gachon.mp_termproject.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calcounter.R; //

import com.gachon.mp_termproject.Activity.DisplayFoodsActivity;
import com.gachon.mp_termproject.data.DatabaseHandler;
import com.gachon.mp_termproject.model.Food;


public class FoodItemDetailsActivity extends AppCompatActivity {

    private TextView foodName, calories, dateTaken;
    private ImageButton shareButton, deleteButton;
    private int foodId;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foof_item_details);

        foodName = findViewById(R.id.detsFoodName);
        calories = findViewById(R.id.detsCaloriesValue);
        dateTaken = findViewById(R.id.detsDateText);
        deleteButton = findViewById(R.id.imageButton);

        shareButton = findViewById(R.id.button);

        Food food = (Food) getIntent().getSerializableExtra("userObj");

        foodName.setText(food.getFoodName());
        calories.setText(String.valueOf(food.getCalories()));
        dateTaken.setText(food.getRecordDate());

        foodId = food.getFoodId();

        //calories.setTextSize(34.9f);
        calories.setTextColor(Color.RED);

        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                shareCals();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(FoodItemDetailsActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("정말 삭제하시겠습니까?");
                alert.setNegativeButton("No", null);
                alert.setPositiveButton("yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                        dba.deleteFood(foodId);
                        Toast.makeText(getApplicationContext(),"삭제되었습니다", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FoodItemDetailsActivity.this, DisplayFoodsActivity.class));

                        // Remove the activity from the activity stack
                        FoodItemDetailsActivity.this.finish();
                    }
                });
                alert.show();
            }
        });
    }

    public void shareCals() {
        StringBuilder dateString = new StringBuilder();
        String name = foodName.getText().toString();
        String cals = calories.getText().toString();
        String date = dateTaken.getText().toString();

        dateString.append(" Food: " + name + "\n");
        dateString.append(" Calories: " + cals + "\n");
        dateString.append(" Eaten on: " + date);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "My calories Intake");
        i.putExtra(Intent.EXTRA_EMAIL, new String[] {"dhgusgh123@naver.com"});
        i.putExtra(Intent.EXTRA_TEXT, dateString.toString());

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        }catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "메일을 보내기 전에 설치해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}