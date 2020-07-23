package com.example.hong.alchul.manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hong.alchul.LoginActivity;
import com.example.hong.alchul.R;
import com.example.hong.alchul.parttime.MyFragment1;
import com.example.hong.alchul.parttime.MyFragment3;
import com.example.hong.alchul.parttime.partime_home;

public class manager_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Bundle bundle = new Bundle(7);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView bottom = (BottomNavigationView)findViewById(R.id.mainactivity_bottomnavigationview);
        bottom.setOnNavigationItemSelectedListener(this);





        Intent intent = getIntent();   //로그인유저의 정보를 받아온다.
        final String userId = intent.getStringExtra("UserId");
        String userName = intent.getStringExtra("UserName");
        String userPhoneNum = intent.getStringExtra("UserPhoneNum");
        String userStat = intent.getStringExtra("UserStat");
        String storeCode = intent.getStringExtra("StoreCode");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");


        String message = "회원정보: " + userStat + "\n안녕하십니까 " + userId + "님";


        //LinearLayout button1 = (LinearLayout)findViewById(R.id.button1);
        //LinearLayout button2 = (LinearLayout)findViewById(R.id.button2);

        if (title != null && content != null) {
            bundle.putString("UserId", userId);
            bundle.putString("UserName", userName);
            bundle.putString("UserPhoneNum", userPhoneNum);
            bundle.putString("UserStat", userStat);
            bundle.putString("StoreCode", storeCode);
            bundle.putString("title", title);
            bundle.putString("content", content);
            //  if(userStat == "part")
            MyFragment3 fragment3 = new MyFragment3();
            fragment3.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment3).commit();
        } else {

            bundle.putString("UserId", userId);         //bundle에 정보를 추가한다.
            bundle.putString("UserName", userName);
            bundle.putString("UserPhoneNum", userPhoneNum);
            bundle.putString("UserStat", userStat);
            bundle.putString("StoreCode", storeCode);


            MyFragment3 fragment3 = new MyFragment3();
            fragment3.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment3).commit();
        }




    }
    @SuppressWarnings("statementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        Log.i("test", "sfsfsd");
        int id = item.getItemId();

        if(id==R.id.logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(manager_home.this);
            builder.setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(manager_home.this, LoginActivity.class);
                            manager_home.this.startActivity(intent);
                        }
                    }).setNegativeButton("취소",null)
                    .create()
                    .show();
            return true;

        }
        else if(id==R.id.people){
            manager_frag1 fragment1 = new manager_frag1();
            fragment1.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment1).commit();
            return true;
        }
        else if(id==R.id.notice){
        MyFragment3 fragment3 = new MyFragment3();
        fragment3.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment3).commit();
            return true;
        }

        return true;
    }

}
