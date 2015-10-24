package com.splendid.timeswind.splendid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class FullClassScheduleActivity extends AppCompatActivity {

    public String SchooldayToday = null;
    public String setting_grade;
    public String setting_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_class_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("課程表");

        Intent intent = getIntent();
        SchooldayToday = intent.getStringExtra("schoolday");

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setting_grade = sharedPref.getString("setting_grade", "1");
        setting_class = sharedPref.getString("setting_class", "A");

        setSchedule(SchooldayToday);

        final RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.dayRadio);

        switch (SchooldayToday) {
            case "A":
                RadioButton radio_day_a = (RadioButton) radiogroup1.findViewById(R.id.radio_day_a);
                radio_day_a.setChecked(true);
                break;
            case "B":
                RadioButton radio_day_b = (RadioButton) radiogroup1.findViewById(R.id.radio_day_b);
                radio_day_b.setChecked(true);
                break;
            case "C":
                RadioButton radio_day_c = (RadioButton) radiogroup1.findViewById(R.id.radio_day_c);
                radio_day_c.setChecked(true);
                break;
            case "D":
                RadioButton radio_day_d = (RadioButton) radiogroup1.findViewById(R.id.radio_day_d);
                radio_day_d.setChecked(true);
                break;
            case "E":
                RadioButton radio_day_e = (RadioButton) radiogroup1.findViewById(R.id.radio_day_e);
                radio_day_e.setChecked(true);
                break;
            case "F":
                RadioButton radio_day_f = (RadioButton) radiogroup1.findViewById(R.id.radio_day_f);
                radio_day_f.setChecked(true);
                break;
            default:
                RadioButton defult = (RadioButton) radiogroup1.findViewById(R.id.radio_day_a);
                defult.setChecked(true);


        }


        //set change listener
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                RadioButton selectedRadio = (RadioButton) radioGroup.findViewById(checkedId);

                setSchedule(selectedRadio.getText().toString());

            }
        });

    }

    private void setSchedule(String Schoolday) {


        try {


            TextView TodayScheduleGradeClassText = (TextView) findViewById(R.id.full_TodayScheduleGradeClassText);
            String scheudleClassTitle = setting_grade + setting_class;
            TodayScheduleGradeClassText.setText(scheudleClassTitle);

            JSONObject obj = new JSONObject(loadJSONFromAsset("schedule.json"));
            JSONArray ScheduleArrayForClass = obj.getJSONArray(setting_grade + setting_class);
            JSONObject jsonObject = ScheduleArrayForClass.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject.optJSONObject(Schoolday);


            TextView schedule_one = (TextView) findViewById(R.id.full_schedule_one);
            TextView schedule_two = (TextView) findViewById(R.id.full_schedule_two);
            TextView schedule_three = (TextView) findViewById(R.id.full_schedule_three);
            TextView schedule_four = (TextView) findViewById(R.id.full_schedule_four);
            TextView schedule_five = (TextView) findViewById(R.id.full_schedule_five);
            TextView schedule_six = (TextView) findViewById(R.id.full_schedule_six);
            TextView schedule_seven = (TextView) findViewById(R.id.full_schedule_seven);
            TextView schedule_eight = (TextView) findViewById(R.id.full_schedule_eight);
            TextView schedule_nine = (TextView) findViewById(R.id.full_schedule_nine);
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

}
