package com.example.hong.alchul.manager;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.R;
import com.example.hong.alchul.decorators.EventDecorator;
import com.example.hong.alchul.decorators.OneDayDecorator;
import com.example.hong.alchul.decorators.SaturdayDecorator;
import com.example.hong.alchul.decorators.SundayDecorator;
import com.example.hong.alchul.parttime.MyFragment2;
import com.example.hong.alchul.request.eventRequest;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class user_calendar extends AppCompatActivity {
    String userName;
    String userPhoneNum;
    String storeCode;
    TextView frag1, frag2, frag3;
    MaterialCalendarView materialCalendarView;
    int pay_hour = 8000;

    List list = new ArrayList();
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_calendar);
        Intent intent = getIntent();



        userName = intent.getStringExtra("userName");
        userPhoneNum = intent.getStringExtra("userPhoneNum");
        storeCode = intent.getStringExtra("storeCode");
        frag1 = (TextView) findViewById(R.id.frag1);
        frag1.setText("직 원 :   " + userName + " 의 근무");

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray success = jsonResponse.getJSONArray("response");
                    for(int i=0; i<success.length(); i++){

                        JSONObject item = success.getJSONObject(i);
                        String time = item.getString("startday");

                        list.add(time);
                    }

                    String[] result = new String[list.size()];
                    for(int i= 0; i< list.size(); i++){
                        result[i] = list.get(i).toString();//유저근무기록 리스트로만듬

                    }



                    new user_calendar.ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());        //점찍는 클래스 호출


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        };

        eventRequest event1 = new eventRequest(userName, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(event1);



        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = getIntent();

                userName = intent.getStringExtra("userName");

                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();
                String startday = Year + "-" + Month + "-" + Day;

                Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                    public void onResponse(String response) {

                        frag2 = (TextView)findViewById(R.id.frag2);
                        frag3 = (TextView)findViewById(R.id.frag3);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray success = jsonResponse.getJSONArray("response");
                            if(success.length()==0){
                                frag2.setText("근무기록: 휴무입니다");
                                frag3.setText("일당:   0원");
                            }
                            else{

                                for(int i=0; i<1; i++){

                                    JSONObject item = success.getJSONObject(i);
                                    String workstart = item.getString("workstart");
                                    String workend = item.getString("workend");

                                    SimpleDateFormat transFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    SimpleDateFormat transFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date to1 = transFormat1.parse(workstart);   //근무기록 뽑아온것형식변환
                                    Date to2 = transFormat2.parse(workend);
                                    double diff = Math.round((to2.getTime()-to1.getTime())*pay_hour/3600000.0);
                                    int payday = (int)diff;
                                    Log.i("test", "dfd"+diff);
                                    frag2.setText("근무기록: "+workstart+" ~ "+workend);
                                    frag3.setText("일당: "+ payday+"원");
                                }


                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };
                materialCalendarView.clearSelection();

                eventRequest event2 = new eventRequest(userName, startday, responseListener1);
                RequestQueue queue = Volley.newRequestQueue(user_calendar.this);
                queue.add(event2);              //날짜눌렷을때 근무기록 요청




            }
        });


        //return view;
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/

        //string 문자열인 result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){



                String[] time = Time_Result[i].split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);
                calendar.set(year,month-1,dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);

                //calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }


        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, user_calendar.this));   //eventDecorator호출
        }

    }

}








