package com.splendid.timeswind.splendid;

/**
 * Created by timeswind on 10/12/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TabFragment1 extends Fragment {

    public View view;
    public String weekDay = null;
    public String weekDayForTomorrow = null;
    public String SchoolDayForSchoolDayCardText = null;
    public String TomorrowSchoolDayForSchoolDayCardText;
    public String setting_grade;
    public String setting_class;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setting_grade = sharedPref.getString("setting_grade", "1");
        setting_class = sharedPref.getString("setting_class", "A");

        Calendar calendar = Calendar.getInstance();

        Calendar calendarForTomorrow = Calendar.getInstance();
        calendarForTomorrow.add(Calendar.DAY_OF_YEAR, 1);

        final Date todayDate = new Date();
        final Date tomorrow = calendarForTomorrow.getTime();

        final String todayDateFormatted = DateFormat.format("dd/MM/yyyy", todayDate).toString();
        final String tomorrowDateFormatted = DateFormat.format("dd/MM/yyyy", tomorrow).toString();


        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final int dayOfWeekFroTomorrow = calendarForTomorrow.get(Calendar.DAY_OF_WEEK);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scroll_container);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY == 0) swipeLayout.setEnabled(true);
                else swipeLayout.setEnabled(false);

            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Context context = getActivity();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                setting_grade = sharedPref.getString("setting_grade", "1");
                setting_class = sharedPref.getString("setting_class", "A");

                Calendar calendar = Calendar.getInstance();

                Calendar calendarForTomorrow = Calendar.getInstance();
                calendarForTomorrow.add(Calendar.DAY_OF_YEAR, 1);

                final Date todayDate = new Date();
                final Date tomorrow = calendarForTomorrow.getTime();

                final String todayDateFormatted = DateFormat.format("dd/MM/yyyy", todayDate).toString();
                final String tomorrowDateFormatted = DateFormat.format("dd/MM/yyyy", tomorrow).toString();


                final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                final int dayOfWeekFroTomorrow = calendarForTomorrow.get(Calendar.DAY_OF_WEEK);

                weekDay = getWeekday(dayOfWeek);
                weekDayForTomorrow = getWeekday(dayOfWeekFroTomorrow);
                setDseCountdown(todayDate);
                setSchoolDay(todayDateFormatted, tomorrowDateFormatted);
                setSchedule();
                setActivity();
                String actionbarTitle = calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日 " + weekDay;
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(actionbarTitle);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }

        });

        swipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2);


        weekDay = getWeekday(dayOfWeek);
        weekDayForTomorrow = getWeekday(dayOfWeekFroTomorrow);


        //計算DSE
        setDseCountdown(todayDate);


        //計算今天的SchoolDay
        setSchoolDay(todayDateFormatted, tomorrowDateFormatted);

        //顯示今日課程表
        setSchedule();

        //activity
        setActivity();


        //Weather
        boolean setting_weather = sharedPref.getBoolean("setting_weather", true);
        if (setting_weather == true) {
            getWeather();
        } else {
            View WeatherCardView = view.findViewById(R.id.WeatherCardView);
            WeatherCardView.setVisibility(View.GONE);
        }

        return view;
    }


    private String getDateDiffString(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;

        if (delta > 0) {
            return "還有" + delta + "天";
        } else {
            delta *= -1;
            return "DSE正在進行";
        }
    }

    private String loadJSONFromAsset(String name) {
        String json = null;
    try {
        InputStream is = getActivity().getAssets().open(name);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");
    } catch (IOException ex) {
        ex.printStackTrace();
        return null;
    }
    return json;
}

    private String getWeekday(int dayOfWeek) {

        String weekDay = null;

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

    private void setSchoolDay(String todayDateFormatted, String tomorrowDateFormatted) {

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset("calender.json"));
            JSONObject calendarToday = obj.getJSONObject(todayDateFormatted);
            JSONObject calendarTomorrow = obj.getJSONObject(tomorrowDateFormatted);

            Object calendarTodayValue = calendarToday.get("day");
            Object calendarTomorrowValue = calendarTomorrow.get("day");

            SchoolDayForSchoolDayCardText = calendarTodayValue.toString();
            TomorrowSchoolDayForSchoolDayCardText = calendarTomorrowValue.toString();

            if (SchoolDayForSchoolDayCardText.isEmpty()) {
                View SchoolDayCard = view.findViewById(R.id.SchoolDayCard);
                View NoSchoolDayCard = view.findViewById(R.id.NoSchooldayCard);

                TextView TodayWeekdayForNoSchoolDayCard = (TextView) view.findViewById(R.id.TodayWeekdayForNoSchoolDayCard);
                TodayWeekdayForNoSchoolDayCard.setText(weekDay);


                SchoolDayCard.setVisibility(View.GONE);
                NoSchoolDayCard.setVisibility(View.VISIBLE);

                final Button button = (Button) view.findViewById(R.id.goToFullClassScheduleFromNoSchooldayCard);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).goToFullClassSchedule("A");

                    }
                });

            } else {
                final Button button = (Button) view.findViewById(R.id.goToFullClassSchedule);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).goToFullClassSchedule(SchoolDayForSchoolDayCardText);

                    }
                });

                View SchoolDayCard = view.findViewById(R.id.SchoolDayCard);
                View NoSchoolDayCard = view.findViewById(R.id.NoSchooldayCard);

                SchoolDayCard.setVisibility(View.VISIBLE);
                NoSchoolDayCard.setVisibility(View.GONE);

                TextView SchoolDayForSchoolDayCard = (TextView) view.findViewById(R.id.SchoolDayForSchoolDayCard);
                TextView TodayWeekday = (TextView) view.findViewById(R.id.TodayWeekday);

                String SchoolDayTodayForDisplay = "Day " + SchoolDayForSchoolDayCardText;
                SchoolDayForSchoolDayCard.setText(SchoolDayTodayForDisplay);
                TodayWeekday.setText(weekDay);

            }

            if (TomorrowSchoolDayForSchoolDayCardText.isEmpty()) {
                TextView TomorrowSchoolDayForSchoolDayCard = (TextView) view.findViewById(R.id.TomorrowSchoolDayForSchoolDayCard);
                TextView TomorrowWeekday = (TextView) view.findViewById(R.id.TomorrowWeekday);

                TextView TomorrowWeekdayForNoSchoolDayCard = (TextView) view.findViewById(R.id.TomorrowWeekdayForNoSchoolDayCard);
                TextView indicatorForTomorrowSchoolDay = (TextView) view.findViewById(R.id.indicatorForTomorrowSchoolDay);

                indicatorForTomorrowSchoolDay.setVisibility(View.GONE);


                TomorrowSchoolDayForSchoolDayCard.setText("無");
                TomorrowWeekday.setText(weekDayForTomorrow);

                TomorrowWeekdayForNoSchoolDayCard.setText(weekDayForTomorrow);


            } else {
                TextView TomorrowSchoolDayForSchoolDayCard = (TextView) view.findViewById(R.id.TomorrowSchoolDayForSchoolDayCard);
                TextView TomorrowWeekday = (TextView) view.findViewById(R.id.TomorrowWeekday);
                TextView TomorrowWeekdayForNoSchoolDayCard = (TextView) view.findViewById(R.id.TomorrowWeekdayForNoSchoolDayCard);

                TextView indicatorForTomorrowSchoolDay = (TextView) view.findViewById(R.id.indicatorForTomorrowSchoolDay);

                indicatorForTomorrowSchoolDay.setVisibility(View.VISIBLE);

                String SchoolDayTomorrowForDisplay = "Day " + TomorrowSchoolDayForSchoolDayCardText;
                TomorrowSchoolDayForSchoolDayCard.setText(SchoolDayTomorrowForDisplay);
                TomorrowWeekday.setText(weekDayForTomorrow);
                TomorrowWeekdayForNoSchoolDayCard.setText(weekDayForTomorrow);

                indicatorForTomorrowSchoolDay.setText("明天上課日是：Day " + TomorrowSchoolDayForSchoolDayCardText);
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void setSchedule() {

        View ClassScheduleView = view.findViewById(R.id.ClassScheduleView);

        if (SchoolDayForSchoolDayCardText != null && SchoolDayForSchoolDayCardText.isEmpty()) {
            ClassScheduleView.setVisibility(View.GONE);
        } else {

            if (ClassScheduleView != null) {
                ClassScheduleView.setVisibility(View.VISIBLE);
            }

            try {


                TextView TodayScheduleGradeClassText = (TextView) view.findViewById(R.id.TodayScheduleGradeClassText);
                String scheudleClassTitle = setting_grade + setting_class;
                TodayScheduleGradeClassText.setText(scheudleClassTitle);

                JSONObject obj = new JSONObject(loadJSONFromAsset("schedule.json"));
                JSONArray ScheduleArrayForClass = obj.getJSONArray(setting_grade + setting_class);
                JSONObject jsonObject = ScheduleArrayForClass.getJSONObject(0);
                JSONObject jsonObject2 = jsonObject.optJSONObject(SchoolDayForSchoolDayCardText);


                TextView schedule_one = (TextView) view.findViewById(R.id.schedule_one);
                TextView schedule_two = (TextView) view.findViewById(R.id.schedule_two);
                TextView schedule_three = (TextView) view.findViewById(R.id.schedule_three);
                TextView schedule_four = (TextView) view.findViewById(R.id.schedule_four);
                TextView schedule_five = (TextView) view.findViewById(R.id.schedule_five);
                TextView schedule_six = (TextView) view.findViewById(R.id.schedule_six);
                TextView schedule_seven = (TextView) view.findViewById(R.id.schedule_seven);
                TextView schedule_eight = (TextView) view.findViewById(R.id.schedule_eight);
                TextView schedule_nine = (TextView) view.findViewById(R.id.schedule_nine);
                schedule_one.setText(jsonObject2.get("one").toString());
                schedule_two.setText(jsonObject2.get("two").toString());
                schedule_three.setText(jsonObject2.get("three").toString());
                schedule_four.setText(jsonObject2.get("four").toString());
                schedule_five.setText(jsonObject2.get("five").toString());
                schedule_six.setText(jsonObject2.get("six").toString());
                schedule_seven.setText(jsonObject2.get("seven").toString());
                schedule_eight.setText(jsonObject2.get("eight").toString());
                schedule_nine.setText(jsonObject2.get("nine").toString());

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }


    }

    private void setActivity() {
        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset("calender.json"));

            Calendar today = Calendar.getInstance();
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);

            String todayDateFormated = DateFormat.format("dd/MM/yyyy", today).toString();
            String tomorrowDateFormated = DateFormat.format("dd/MM/yyyy", tomorrow).toString();


            JSONObject todayEvents = obj.getJSONObject(todayDateFormated);
            JSONObject tomorrowEvents = obj.getJSONObject(tomorrowDateFormated);


            Object todayActivity = todayEvents.get("activity");
            Object tomorrowActivity = tomorrowEvents.get("activity");

            View SchoolActivityCardView = view.findViewById(R.id.SchoolActivityCardView);


            if (todayActivity.toString().isEmpty()) {
                TextView textViewForActivityToday = (TextView) view.findViewById(R.id.textViewForActivityToday);

                textViewForActivityToday.setText("無");

            } else {
                TextView textViewForActivityToday = (TextView) view.findViewById(R.id.textViewForActivityToday);

                SchoolActivityCardView.setVisibility(View.VISIBLE);

                textViewForActivityToday.setText(todayActivity.toString());
            }

            if (tomorrowActivity.toString().isEmpty()) {
                TextView textViewForActivityTomorrow = (TextView) view.findViewById(R.id.textViewForActivityTomorrow);

                textViewForActivityTomorrow.setText("無");


            } else {
                TextView textViewForActivityTomorrow = (TextView) view.findViewById(R.id.textViewForActivityTomorrow);


                textViewForActivityTomorrow.setText(tomorrowActivity.toString());
            }

            if (todayActivity.toString().isEmpty() && tomorrowActivity.toString().isEmpty()) {
                SchoolActivityCardView.setVisibility(View.GONE);
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void setDseCountdown(Date todayDate) {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        boolean setting_dse = sharedPref.getBoolean("setting_dse", true);
        if (setting_dse == true) {
            View DseCountDownCard = view.findViewById(R.id.DSEcv);
            DseCountDownCard.setVisibility(View.VISIBLE);
            TextView DSE = (TextView) view.findViewById(R.id.DSE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // here set the pattern as you date in string was containing like date/month/year
            Date dseDate = null;
            try {
                dseDate = sdf.parse("30/03/2016");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DSE.setText(getDateDiffString(todayDate, dseDate));
        } else {
            View DseCountDownCard = view.findViewById(R.id.DSEcv);
            DseCountDownCard.setVisibility(View.GONE);
        }

    }

    public void getWeather() {
        View weathercard = view.findViewById(R.id.WeatherCardView);
        weathercard.setVisibility(View.GONE );
//        String httpUrl = "http://api.heweather.com/x3/weather?cityid=CN101320101&key=3b73af05dcba475c9318e6adb024a480";
//        String jsonResult = request(httpUrl);
//
//        Log.d("json", jsonResult + "lol");




    }

    public static String request(String httpUrl) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void switchToTomorrowMode(View v){

        Log.d("tomorrowmode", "tomorrowmode");

    }

}
