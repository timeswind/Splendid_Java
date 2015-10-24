package com.splendid.timeswind.splendid;

/**
 * Created by timeswind on 10/16/15.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CustomAdapterForHoliday extends RecyclerView.Adapter<CustomAdapterForHoliday.ContactViewHolder> {

    private List<CalendarEventInfo> contactList;

    public CustomAdapterForHoliday(List<CalendarEventInfo> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        CalendarEventInfo ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.name);
        contactViewHolder.vDate.setText(ci.date);
        contactViewHolder.vCountdown.setText(ci.countdown);

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.holiday_calendar_item, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vDate;
        protected TextView vCountdown;


        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.holiday_calendar_name);
            vDate = (TextView) v.findViewById(R.id.holiday_calendar_date);
            vCountdown = (TextView) v.findViewById(R.id.holiday_countdown);


        }
    }
}
