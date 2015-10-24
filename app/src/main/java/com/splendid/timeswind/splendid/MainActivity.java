package com.splendid.timeswind.splendid;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.ref.SoftReference;
import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.splendid.timeswind.splendid.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        Calendar c = Calendar.getInstance();

        final String todayTitle = c.get(Calendar.MONTH) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日 " + getWeekday();

        getSupportActionBar().setTitle(todayTitle);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("今天"));
        tabLayout.addTab(tabLayout.newTab().setText("校曆"));
        tabLayout.addTab(tabLayout.newTab().setText("設置"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());


                int tabPos = tab.getPosition();
                switch (tabPos) {
                    case 0:
                        getSupportActionBar().setTitle(todayTitle);
                        break;
                    case 1:
                        getSupportActionBar().setTitle("校曆");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("設置");

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        return id == R.id.action_settings || super.onOptionsItemSelected(item);
//
//    }

    private String getWeekday() {

        Calendar c = Calendar.getInstance();

        String weekDay = null;

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "星期一";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "星期二";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "星期三";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "星期四";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "星期五";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "星期六";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "星期日";
        }

        return weekDay;

    }

    public void updateClassSchedule() {

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(1);

    }

    public void goToFullHolidayEvents(View view) {

        Intent intent = new Intent(this, FullScheduleEventsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "所有假期");
        intent.putExtra("type", "holiday");

        startActivity(intent);


    }

    public void goToFullActivityEvents(View view) {

        Intent intent = new Intent(this, FullScheduleEventsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "所有活動/事項");
        intent.putExtra("type", "activity");


        startActivity(intent);


    }

    public void goToFullClassSchedule(String Schoolday) {

        Intent intent = new Intent(this, FullClassScheduleActivity.class);

        if (Schoolday.isEmpty()) {
            intent.putExtra("schoolday", "A");

        } else {
            intent.putExtra("schoolday", Schoolday);


        }

        startActivity(intent);
    }
}
