package com.splendid.timeswind.splendid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by timeswind on 10/17/15.
 */

public class CustomAdapterForFullEvent extends RecyclerView.Adapter<CustomAdapterForFullEvent.ContactViewHolder> {

    private List<CalendarEventInfo> eventList;

    public CustomAdapterForFullEvent(List<CalendarEventInfo> eventList) {
        this.eventList = eventList;
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        CalendarEventInfo ci = eventList.get(i);
        contactViewHolder.vName.setText(ci.name);
        contactViewHolder.vDate.setText(ci.date);
        contactViewHolder.vCountdown.setText(ci.countdown);

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.calendar_item, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vDate;
        protected TextView vCountdown;


        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.calendar_name);
            vDate = (TextView) v.findViewById(R.id.calendar_date);
            vCountdown = (TextView) v.findViewById(R.id.countdown);

        }
    }
}
