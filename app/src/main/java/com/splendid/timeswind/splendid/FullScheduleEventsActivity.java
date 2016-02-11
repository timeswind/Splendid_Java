package com.splendid.timeswind.splendid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FullScheduleEventsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_schedule_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String type = intent.getStringExtra("type");

        AppBarLayout app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        CollapsingToolbarLayout toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        app_bar.setBackgroundColor(getResources().getColor(MainActivity.currentBarColor));
        toolbar_layout.setContentScrimColor(getResources().getColor(MainActivity.currentBarColor));
        toolbar_layout.setStatusBarScrimColor(getResources().getColor(MainActivity.currentBarColor));


        getSupportActionBar().setTitle(message);

        mRecyclerView = (RecyclerView) findViewById(R.id.eventList);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);


        mRecyclerView.setLayoutManager(mLayoutManager);



        // specify an adapter (see also next example)

        CustomAdapterForFullEvent EventsList = new CustomAdapterForFullEvent(createList(type));
        mRecyclerView.setAdapter(EventsList);


    }

    private List<CalendarEventInfo> createList(String event) {
        JSONObject obj;

        Calendar today = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            end.setTime(sdf.parse("07/31/2016"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<CalendarEventInfo> result = new ArrayList<CalendarEventInfo>();
        try {
            obj = new JSONObject(loadJSONFromAsset("calender.json"));

            while (start.getTimeInMillis() < end.getTimeInMillis()) {

                String startDateFormated = DateFormat.format("dd/MM/yyyy", start).toString();
                String startDateDisplayFormated = DateFormat.format("MM月dd日", start).toString();

                JSONObject events = obj.getJSONObject(startDateFormated);

                Object eventObject = events.get(event);

                String eventName = eventObject.toString();
                if (!eventName.isEmpty()) {
                    CalendarEventInfo cei = new CalendarEventInfo();
                    cei.date = startDateDisplayFormated;
                    cei.name = eventName;
                    cei.countdown = getDateDiffString(today, start);
                    result.add(cei);
                }

                start.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String loadJSONFromAsset(String name) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(name);
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

    private String getDateDiffString(Calendar dateOne, Calendar dateTwo) {
        long timeOne = dateOne.getTimeInMillis();
        long timeTwo = dateTwo.getTimeInMillis();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;

        if (delta >= 0) {
            return "距離" + delta + "天";
        } else {
            delta *= -1;
            return "X";
        }
    }


}
