package com.splendid.timeswind.splendid;

/**
 * Created by timeswind on 10/12/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TabFragment2 extends Fragment {

    private RecyclerView mRecyclerViewForHoliday;
    private RecyclerView mRecyclerViewForActivity;
    private RecyclerView.LayoutManager mLayoutManagerForHoliday;
    private RecyclerView.LayoutManager mLayoutManagerForActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);

        mRecyclerViewForHoliday = (RecyclerView) view.findViewById(R.id.holidayList);

        mRecyclerViewForActivity = (RecyclerView) view.findViewById(R.id.activityList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewForHoliday.setHasFixedSize(true);

        mRecyclerViewForActivity.setHasFixedSize(true);


        // use a linear layout manager
        mLayoutManagerForHoliday = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManagerForActivity = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        mRecyclerViewForHoliday.setLayoutManager(mLayoutManagerForHoliday);

        mRecyclerViewForActivity.setLayoutManager(mLayoutManagerForActivity);


        // specify an adapter (see also next example)

        CustomAdapterForHoliday holidayEventsList = new CustomAdapterForHoliday(createList("holiday"));
        CustomAdapterForActivity activityEventsList = new CustomAdapterForActivity(createList("activity"));
        mRecyclerViewForHoliday.setAdapter(holidayEventsList);
        mRecyclerViewForActivity.setAdapter(activityEventsList);
        return view;
    }

    private List<CalendarEventInfo> createList(String event) {
        JSONObject obj;

        Calendar today = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_MONTH, 30);

        //        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

//        try {
//            end.setTime(sdf.parse("07/31/2016"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


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