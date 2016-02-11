package com.splendid.timeswind.splendid;

/**
 * Created by Mingtian Yang
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.splendid.timeswind.splendid.MESSAGE";
    public static Integer currentBarColor = R.color.colorPrimary;
    private View mRevealView;
    private View mRevealBackgroundView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        mRevealView = findViewById(R.id.reveal);
        mRevealBackgroundView = findViewById(R.id.revealBackground);

        Calendar c = Calendar.getInstance();

        final String todayTitle = (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日 " + getWeekday();

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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            animateAppAndStatusBar(currentBarColor, R.color.cyan_500, tabPos);
                        }
                        break;
                    case 1:
                        getSupportActionBar().setTitle("校曆");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            animateAppAndStatusBar(currentBarColor, R.color.blue_500, tabPos);
                        }
                        break;
                    case 2:
                        getSupportActionBar().setTitle("設置");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            animateAppAndStatusBar(currentBarColor, R.color.blue_grey_900, tabPos);
                        }


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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateAppAndStatusBar(int fromColor, final int toColor, int tabnumber) {
        int startX = 6;
        int startRadius = 1;
        switch (tabnumber) {
            case 0:
                startX = 6;
                startRadius = gouGuCaculate(mToolbar.getWidth(), mToolbar.getHeight());
                break;
            case 1:
                startX = 2;
                startRadius = gouGuCaculate(mToolbar.getWidth() / 2, mToolbar.getHeight());
                break;
            case 2:
                startX = 1;
                startRadius = gouGuCaculate(mToolbar.getWidth(), mToolbar.getHeight());
        }




        Animator animator = ViewAnimationUtils.createCircularReveal(
                mRevealView,
                mToolbar.getWidth() / startX,
                mToolbar.getHeight() * 2 , 0,
                startRadius
        );

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRevealView.setBackgroundColor(getResources().getColor(toColor));
            }
        });

        mRevealBackgroundView.setBackgroundColor(getResources().getColor(fromColor));
        animator.setStartDelay(200);
        animator.setDuration(125);
        animator.start();
        mRevealView.setVisibility(View.VISIBLE);

        currentBarColor = toColor;
    }

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

    private int gouGuCaculate(double one, double two){

        return (int) Math.sqrt(Math.pow(one, 2)+Math.pow(two, 2));
    }
}
