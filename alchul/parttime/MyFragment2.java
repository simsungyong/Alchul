package com.example.hong.alchul.parttime;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.R;
import com.example.hong.alchul.request.IdCheckRequest;
import com.example.hong.alchul.request.eventRequest;
import com.example.hong.alchul.decorators.EventDecorator;
import com.example.hong.alchul.decorators.OneDayDecorator;
import com.example.hong.alchul.decorators.SaturdayDecorator;
import com.example.hong.alchul.decorators.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.EventListener;

public class MyFragment2 extends Fragment {
    String time, kcal, menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;   //캘린더뷰 형식.
    View view;
    private Context context;
    List list = new ArrayList();
    TextView frag1, frag2, frag3;
    int pay_hour = 8000;



    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_part2, container, false);
        context = container.getContext();

        String userId = getArguments().getString("UserId");
        String userName = getArguments().getString("UserName");
        String userPhoneNum = getArguments().getString("UserPhoneNum");
        String userStat = getArguments().getString("UserStat");
        frag1 = (TextView)view.findViewById(R.id.frag1);
        frag1.setText("이름:   "+ userName);

        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);    //달력 아이디받아옴

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);           //달력꾸미는 소스들 추가.



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



                    new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());        //점찍는 클래스 호출


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        };

        eventRequest event1 = new eventRequest(userName, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(event1);



        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                String userName = getArguments().getString("UserName");

                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();
                String startday = Year + "-" + Month + "-" + Day;

                Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                    public void onResponse(String response) {

                        frag2 = (TextView)view.findViewById(R.id.frag2);
                        frag3 = (TextView)view.findViewById(R.id.frag3);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray success = jsonResponse.getJSONArray("response");
                            if(success.length()==0){
                                frag2.setText("근무기록 : 휴무입니다");
                                frag3.setText("일당 :   0원");
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
                                    frag2.setText("근무기록 :  "+workstart+" ~ "+workend);
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
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(event2);              //날짜눌렷을때 근무기록 요청




            }
        });


        return view;
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
            context = getActivity();    //fragment에서 엑티비티 가져옴
            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, context));   //eventDecorator호출
        }

    }

}

