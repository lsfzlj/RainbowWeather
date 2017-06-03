package com.iflytek.weatherforecast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class AddRemind extends AppCompatActivity implements View.OnClickListener{
    final String TAG = "Addremind";
    private Button shezhishijian;
    private Button shezhiriqi;
    private Button returnRemindPage;
    private Button saveRemind;
    private Calendar calendar;
    private boolean setDateFlag;
    private boolean setTimeFlag;
    private Date mDate;
    private String myCity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remind);

        Intent getCity =getIntent();
        myCity = getCity.getStringExtra("toAddRemind");

        shezhiriqi = (Button)findViewById(R.id.shezhiriqi);
        shezhishijian = (Button)findViewById(R.id.shezhishijian);
        returnRemindPage = (Button)findViewById(R.id.return_remindpage);
        saveRemind = (Button)findViewById(R.id.sava_remindpage);
        shezhiriqi.setOnClickListener(this);
        shezhishijian.setOnClickListener(this);
        returnRemindPage.setOnClickListener(this);
        saveRemind.setOnClickListener(this);
    //    DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker5);

        mDate = new Date();mDate.setSeconds(0);
        calendar = Calendar.getInstance(Locale.CHINA);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shezhiriqi:
                new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        mDate.setMonth(month);
                        mDate.setYear(year-1900);
                        mDate.setDate(day);
                        // 更新EditText控件日期 小于10加0
                        shezhiriqi.setText(new StringBuilder()
                                .append(year)
                                .append("-")
                                .append((month + 1) < 10 ? "0"
                                        + (month + 1) : (month + 1))
                                .append("-")
                                .append((day < 10) ? "0" + day : day));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                setDateFlag = true;
                break;
            case R.id.shezhishijian:
                new TimePickerDialog( this,new TimePickerDialog.OnTimeSetListener(){
                @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                     mDate.setHours(hourOfDay);
                     mDate.setMinutes(minute);
                    shezhishijian.setText(new StringBuilder()
                            .append(hourOfDay)
                            .append(":")
                            .append(minute)
                    );
                  }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
                //更新EditText控件时间
                setTimeFlag = true;
                break;
            case R.id.sava_remindpage:
                if(!setDateFlag||!setTimeFlag){
                    Toast.makeText(this,"日期或者时间参数无效，请确认",Toast.LENGTH_SHORT).show();
                }else{
                    addOrDeleteDate(myCity,mDate,true);
                    Toast.makeText(this,"日期与时间参数已保存",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.return_remindpage:
                finish();
                Intent intent = new Intent(this,RemindPageActivity.class);
                intent.putExtra("toRemindPage",myCity);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    // if addordelete == ture  : add  mDate to city
    //  else delete mDate from city
    public  void addOrDeleteDate(String city,Date mDate,boolean addordelete){
        SharedPreferences sharedPreferences = getSharedPreferences(city,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String in = formatter.format(mDate);
        set = sharedPreferences.getStringSet(city,set);
        for(Iterator<String> it=set.iterator();it.hasNext();){
            String temp = it.next();
            if(addordelete||(!temp.equals(in))){
                set2.add(temp);
            }
        }
        Log.d(TAG, "addOrDeleteDate: ");
        if(addordelete)
            set2.add(in);
        editor.putStringSet(city,set2);
        editor.apply();
    }
}
