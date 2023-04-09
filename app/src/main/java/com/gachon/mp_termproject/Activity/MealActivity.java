package com.gachon.mp_termproject.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

import com.example.calcounter.R;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MealActivity extends AppCompatActivity{

    List<Integer> treeId = new ArrayList<Integer>();
    private ImageSwitcher mSwitcher;

    ViewGroup viewGroup;
    long pressedTime = 0;
    Toast toast;
    int[] tree = {R.drawable.tree1, R.drawable.tree2, R.drawable.tree3,
            R.drawable.tree3, R.drawable.tree4, R.drawable.tree5, R.drawable.tree6};
    int position = 0;
    ImageView treeImage;
    TextView Date, secondTV;
    EditText secondET;
    ProgressHandler handler;
    Button startBtn, endBtn;
    TimePicker alarm_timepicker;
    LocalTime now = LocalTime.now();
    int hour = now.getHour();
    int minute = now.getMinute();
    Vibrator vib;

    int second, key, treeSecond, treeGrow;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        mSwitcher = (ImageSwitcher) findViewById(R.id.meal_switcher);
        mSwitcher.setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(MealActivity.this);
                imageView.setImageResource(tree[position]);
                return imageView;
            }
        });

        mSwitcher.setInAnimation(this, android.R.anim.fade_out);
        mSwitcher.setInAnimation(this, android.R.anim.fade_in);
       /* mSwitcher.setImageResource(tree[0]);*/


        try {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    ex.printStackTrace();
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }


    }

    /*private void onSwitch(View view) {
        position = position + 1;
        mSwitcher.setBackgroundResource(tree[position]);
    }*/

    @Override
    public void onBackPressed() {

        //마지막으로 누른 '뒤로가기' 버튼 클릭 시간이 이전의 '뒤로가기' 버튼 클릭 시간과의 차이가 2초보다 크면
        if (System.currentTimeMillis() > pressedTime + 2000) {
            //현재 시간을 pressedTime 에 저장
            pressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "정말로 나가시겠습니까?", Toast.LENGTH_SHORT).show();
        }

        //마지막 '뒤로가기' 버튼 클릭시간이 이전의 '뒤로가기' 버튼 클릭 시간과의 차이가 2초보다 작으면
        else {
            Toast.makeText(getApplicationContext(), "종료 완료", Toast.LENGTH_SHORT).show();
            // 앱 종료
            finish();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart(){
        super.onStart();
        //<--알람 -->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarm_timepicker = findViewById(R.id.time_picker);

            //<-- 타이머 -->
            Date = findViewById(R.id.Date);
            secondET = findViewById(R.id.secondET);
            handler = new ProgressHandler();
            secondTV = findViewById(R.id.secondTV);

            startBtn= findViewById(R.id.btn_start);
            endBtn = findViewById(R.id.btn_finish);

            LinearLayout timeCountSettingLV = findViewById(R.id.timeCountSettingLV);
            LinearLayout timeCountLV = findViewById(R.id.timeCountLV);

            final Handler mealhandler = new Handler()
            {
                public void handleMessage(Message msg){
                    position += 1;
                    mSwitcher.setImageResource(tree[position]);
                }
            };


            startBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    timeCountSettingLV.setVisibility(View.GONE);
                    timeCountLV.setVisibility(View.VISIBLE);

                    secondTV.setText(secondET.getText().toString());
                    second = Integer.parseInt(secondET.getText().toString());

                    key = second;

                    treeGrow = 480;
                    treeSecond = 0;
                    int sethour = alarm_timepicker.getHour();
                    int setminute = alarm_timepicker.getMinute();

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask(){
                        @Override
                        public void run(){
                            if(treeSecond == treeGrow){
                                if(position != 5){
                                    Message msg = mealhandler.obtainMessage();
                                    mealhandler.sendMessage(msg);
                                    treeSecond++;
                                    treeGrow += treeGrow;
                                } else{
                                    treeGrow = 0;
                                }
                            } else{
                                treeSecond++;
                            }


                            if (second != 0) {
                                second--;
                            }
                            if (second <= 9) {
                                secondTV.setText("0" + second);
                            }
                            if (second == 0) {
                                second = key;
                                //타이머 종료시 알람(삼켜도 된다는 알람), 진동
                                postToastMessage("꿀꺽!");
                                vib.vibrate(500);
                                if(now.getHour() == sethour && now.getMinute() == setminute){
                                    //식사시간 종료 알람
                                    postToastMessage("식사 사간이 종료되었습니다.\n자동으로 메뉴로 돌아갑니다.");
                                }
                            }

                            //식사 종료 버튼을 눌렀을 때
                            endBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //메뉴 화면을 돌아가게
                                }
                            });
                        }
                    };
                    //타이머 실행
                    timer.schedule(timerTask, 0, 1000);
                }
            });

            runTime();
        }
    }
    public void runTime(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                while(true){
                    try{
                        time = sdf.format(new Date(System.currentTimeMillis()));

                        Message message = handler.obtainMessage();
                        handler.sendMessage(message);

                        Thread.sleep(1000);
                    }catch(InterruptedException ex){}
                }
            }
        });
        thread.start();
    }

    class ProgressHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            Date.setText(time);
        }
    }

    public void postToastMessage(final String message){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }



}