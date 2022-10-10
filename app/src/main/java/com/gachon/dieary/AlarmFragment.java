package com.gachon.dieary;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


@RequiresApi(api = Build.VERSION_CODES.O)
public class AlarmFragment extends Fragment {

    TextView Date, secondTV;
    EditText secondET;
    ProgressHandler handler;
    Button startBtn, endBtn;
    TimePicker alarm_timepicker;
    LocalTime now = LocalTime.now();
    int hour = now.getHour();
    int minute = now.getMinute();
    Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

    int second, key;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart(){
        super.onStart();
        //<--알람 -->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarm_timepicker = getActivity().findViewById(R.id.time_picker);
            int sethour = alarm_timepicker.getHour();
            int setminute = alarm_timepicker.getMinute();

            //<-- 타이머 -->
            Date = getActivity().findViewById(R.id.Date);
            secondET = getActivity().findViewById(R.id.secondET);
            handler = new ProgressHandler();
            secondTV = getActivity().findViewById(R.id.secondTV);

            startBtn= getActivity().findViewById(R.id.btn_start);
            endBtn = getActivity().findViewById(R.id.btn_finish);

            LinearLayout timeCountSettingLV = getActivity().findViewById(R.id.timeCountSettingLV);
            LinearLayout timeCountLV = getActivity().findViewById(R.id.timeCountLV);

            startBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    timeCountSettingLV.setVisibility(View.GONE);
                    timeCountLV.setVisibility(View.VISIBLE);

                    secondTV.setText(secondET.getText().toString());
                    second = Integer.parseInt(secondET.getText().toString());

                    key = second;

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask(){
                        @Override
                        public void run(){
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
                                    if(hour== sethour && minute == setminute){
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

    class ProgressHandler extends Handler{
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
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
        });
    }
}