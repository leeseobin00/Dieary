package com.gachon.dieary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CalendarActivity extends AppCompatActivity {
    
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, insert_Btn, del_Btn, save_Btn, share_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;
    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        insert_Btn = findViewById(R.id.insert_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        share_Btn = findViewById(R.id.share_Btn);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        contextEditText = findViewById(R.id.contextEditText);
        imageView = findViewById(R.id.imageView);

        imageView.setVisibility(View.INVISIBLE);

        // CalendarView 첫 화면
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                insert_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                share_Btn.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                contextEditText.setText("");
                checkDay(year, month, dayOfMonth);
            }
        });
        // 저장 버튼을 눌렀을 때 text는 저장되고, 수정, 삭제, 공유 버튼이 보이도록 함
        save_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveDiary(readDay);
                str = contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                insert_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                share_Btn.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
            }
        });
    }

    // 날짜에 따라 file에 text를 저장함
    public void checkDay(int cYear, int cMonth, int cDay)
    {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
        FileInputStream fis;

        try
        {
            fis = openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            insert_Btn.setVisibility(View.VISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);
            share_Btn.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);

            insert_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.INVISIBLE);
                    insert_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.VISIBLE);
                    del_Btn.setVisibility(View.VISIBLE);
                    share_Btn.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);

                    int[] images = new int[] {R.drawable.chicken, R.drawable.salade, R.drawable.hambugar, R.drawable.raman, R.drawable.apple};
                    int imageId = (int)(Math.random() * images.length);
                    imageView.setImageResource(images[imageId]);
                }

            });

            // 수정버튼을 누르면 text를 수정할 수 있음
            cha_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    insert_Btn.setVisibility(View.INVISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    share_Btn.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);

                    textView2.setText(contextEditText.getText());
                }

            });
            // 삭제 버튼을 누르면 저장된 text가 사라지고 초기 저장 버튼이 다시 보임
            del_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);

                    save_Btn.setVisibility(View.VISIBLE);
                    insert_Btn.setVisibility(View.INVISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    share_Btn.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);

                    removeDiary(readDay);
                }
            });
            // 공유 버튼을 누르면 내가 입력한 text를 공유할 수 있음
            share_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);

                    save_Btn.setVisibility(View.VISIBLE);
                    insert_Btn.setVisibility(View.INVISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    share_Btn.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    String sendMessage = readDay;

                    getIntent().putExtra(Intent.EXTRA_TEXT, sendMessage);
                    Intent shareIntent = Intent.createChooser(intent, "share");
                    startActivity(shareIntent);
                }
            });
            // 아무것도 입력하지 않은 경우
            if (textView2.getText() == null)
            {
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);

                save_Btn.setVisibility(View.VISIBLE);
                insert_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                share_Btn.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);

                contextEditText.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 파일에서 text값을 삭제함
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 파일에 text값을 저장함
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}