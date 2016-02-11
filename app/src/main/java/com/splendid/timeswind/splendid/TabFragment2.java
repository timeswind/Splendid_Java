package com.splendid.timeswind.splendid;

/**
 * Created by Mingtian Yang
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TabFragment2 extends Fragment {

    private RecyclerView HolidaysRecycleView;
    private RecyclerView ActivitiesRecycleView;
    private RecyclerView.LayoutManager LayoutManagerForHolidayRV;
    private RecyclerView.LayoutManager LayoutManagerForActivitiesRV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);


        HolidaysRecycleView = (RecyclerView) view.findViewById(R.id.holidayList);
        ActivitiesRecycleView = (RecyclerView) view.findViewById(R.id.activityList);


        HolidaysRecycleView.setNestedScrollingEnabled(false);
        ActivitiesRecycleView.setNestedScrollingEnabled(false);

        HolidaysRecycleView.setHasFixedSize(true);
        ActivitiesRecycleView.setHasFixedSize(true);


        LayoutManagerForHolidayRV = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        LayoutManagerForActivitiesRV = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        HolidaysRecycleView.setLayoutManager(LayoutManagerForHolidayRV);

        ActivitiesRecycleView.setLayoutManager(LayoutManagerForActivitiesRV);


        CustomAdapterForHoliday holidayEventsList = new CustomAdapterForHoliday(createList("holiday"));
        CustomAdapterForActivity activityEventsList = new CustomAdapterForActivity(createList("activity"));
        HolidaysRecycleView.setAdapter(holidayEventsList);
        ActivitiesRecycleView.setAdapter(activityEventsList);
        return view;
    }

    private List<CalendarEventInfo> createList(String event) {
        JSONObject obj;

        Calendar today = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_MONTH, 30);


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
        String json;
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
        long day_distance = (timeTwo - timeOne) / oneDay;

        if (day_distance >= 0) {
            return "距離" + day_distance + "天";
        } else {
            return "X";
        }
    }


}