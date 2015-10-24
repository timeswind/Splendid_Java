package com.splendid.timeswind.splendid;

/**
 * Created by timeswind on 10/12/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;


public class TabFragment3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);


        //获取本地设置的储存
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean setting_dse = sharedPref.getBoolean("setting_dse", true);
//        boolean setting_weather = sharedPref.getBoolean("setting_weather", true);
        String setting_grade = sharedPref.getString("setting_grade", "1");
        String setting_class = sharedPref.getString("setting_class", "A");


        //显示设置的按钮
        SwitchCompat switchForDse = (SwitchCompat) view.findViewById(R.id.switchForDse);
//        SwitchCompat switchForWeather = (SwitchCompat) view.findViewById(R.id.switchForWeather);
        switchForDse.setChecked(setting_dse);
        switchForDse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Context context = getActivity();
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("setting_dse", true);
                    editor.commit();
                } else {
                    Context context = getActivity();
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("setting_dse", false);
                    editor.commit();
                }

            }
        });
//        switchForWeather.setChecked(setting_weather);
//        switchForWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    Context context = getActivity();
//                    SharedPreferences sharedPref = context.getSharedPreferences(
//                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putBoolean("setting_weather", true);
//                    editor.commit();
//                } else {
//                    Context context = getActivity();
//                    SharedPreferences sharedPref = context.getSharedPreferences(
//                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putBoolean("setting_weather", false);
//                    editor.commit();
//                }
//
//            }
//        });

        final Spinner grade_spinner = (Spinner) view.findViewById(R.id.grade_spinner);
        ArrayAdapter<CharSequence> grade_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.grade_arrays, android.R.layout.simple_spinner_item);
        grade_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade_spinner.setAdapter(grade_adapter);
        grade_spinner.setSelection(getInt(setting_grade) - 1);//讀取記錄的年級數據

        grade_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count = 0;

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (count >= 1) {


                    int gradeInInt = position + 1;
                    String gradeInString = String.valueOf(gradeInInt);

                    Context context = getActivity();
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("setting_grade", gradeInString);
                    editor.commit();

                    ((MainActivity) getActivity()).updateClassSchedule();
                }
                count++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        final Spinner class_spinner = (Spinner) view.findViewById(R.id.class_spinner);
        ArrayAdapter<CharSequence> class_adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.class_arrays, android.R.layout.simple_spinner_item);

        String[] classArray = getResources().getStringArray(R.array.class_arrays);
        int classIndex = Arrays.asList(classArray).indexOf(setting_class);


        class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        class_spinner.setAdapter(class_adapter);
        class_spinner.setSelection(classIndex);

        class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count = 0;

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (count >= 1) {
                    String classInString = class_spinner.getSelectedItem().toString();
                    Context context = getActivity();
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("setting_class", classInString);
                    editor.commit();

                    ((MainActivity) getActivity()).updateClassSchedule();
                }

                count++;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        return view;
    }

    public int getInt(String s) {
        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }


}
