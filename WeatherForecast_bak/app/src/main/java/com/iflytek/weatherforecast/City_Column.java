package com.iflytek.weatherforecast;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class City_Column extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private ArrayList<String>cities;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city__column);
        Button returnBack = (Button)findViewById(R.id.returnback);
        Button addCity = (Button)findViewById(R.id.addCity);
        listView =(ListView)findViewById(R.id.cities_list);
        returnBack.setOnClickListener(this);
        addCity.setOnClickListener(this);
        cities = getCities();
        adapter = new ArrayAdapter<String>(City_Column.this,android.R.layout.simple_list_item_1,cities);
        listView.setAdapter(adapter);
       this.registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("删除+设置");
        menu.add(1,1,1,"删除");
        menu.add(1,2,1,"设置");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenu.ContextMenuInfo info = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo contextMenuinfo = (AdapterView.AdapterContextMenuInfo)info;
        int position = contextMenuinfo.position;
        String city = cities.get(position);

        switch (item.getItemId()){
            case 1:
                MainActivity.deleteCityThread deletecityThread = new MainActivity.deleteCityThread(city);
                Thread threadDeleteCity  = new Thread(deletecityThread);
                threadDeleteCity.start();
                Toast.makeText(City_Column.this,city+"is deltede",Toast.LENGTH_SHORT).show();
                cities.remove(position);
                adapter.notifyDataSetChanged();
                //清除 删除城市下的 提醒
                SharedPreferences shareDates = getSharedPreferences(city,MODE_PRIVATE);
                SharedPreferences.Editor editorShareDates = shareDates.edit();
                editorShareDates.clear();
                break;
            case 2:
                Intent toRemindPage = new Intent(this,RemindPageActivity.class);
                toRemindPage.putExtra("toRemindPage",city);
                startActivity(toRemindPage);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    public ArrayList<String> getCities(){
        ArrayList<String>result = new ArrayList<String>();
        String[]cityArray = MainActivity.getCities();
       for(int i=0;i<cityArray.length;i++)
           result.add(cityArray[i]);
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addCity:
                Intent intent = new Intent();
                intent.setClass(this,MainActivity.class);
                intent.putExtra("query","getQuery");
                startActivity(intent);
                break;
            case R.id.returnback:
                finish();
                Intent backViewPage = new Intent(City_Column.this,ViewPageActivity.class);
                startActivity(backViewPage);
                break;
            default:
                break;
        }
    }
}
