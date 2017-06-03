package com.iflytek.weatherforecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RemindPageActivity extends AppCompatActivity implements View.OnClickListener{
    private String city;
    private ListView listDates;
    private ArrayAdapter<String> adapter;
 //   private SharedPreferences.Editor dataEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_page);
        Button returnOnRemindPage = (Button)findViewById(R.id.return_remindpage);
        Button newOnRemindPage = (Button)findViewById(R.id.new_remindpage);
        returnOnRemindPage.setOnClickListener(this);
        newOnRemindPage.setOnClickListener(this);
        listDates = (ListView)findViewById(R.id.remind_list);
        Intent intent = getIntent();
        city = intent.getStringExtra("toRemindPage");
        Log.d(this.toString(), "onCreate: "+city);

        ArrayList<String> dates =getDates();
        if(dates.isEmpty())
            Toast.makeText(RemindPageActivity.this,city+" 没有对应的提醒，可以新建提醒",Toast.LENGTH_SHORT).show();
        else{
            adapter = new ArrayAdapter<String>(RemindPageActivity.this,android.R.layout.simple_list_item_1,dates);
            listDates.setAdapter(adapter);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_remindpage:
                Intent intentNewRemind = new Intent(this,AddRemind.class);
                intentNewRemind.putExtra("toAddRemind",city);
                startActivity(intentNewRemind);
                break;
            case R.id.return_remindpage:
                finish();
                break;
        }
    }
    public ArrayList<String> getDates(){
        SharedPreferences sharedPreferences = getSharedPreferences(city,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>();
        set = sharedPreferences.getStringSet(city,set);
        ArrayList<String>dates = new ArrayList<String>(set);
        return dates;
    }
}
