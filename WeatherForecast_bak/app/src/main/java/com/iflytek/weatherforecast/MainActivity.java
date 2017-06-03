package com.iflytek.weatherforecast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.iflytek.cloud.SpeechUtility;

public class MainActivity extends Activity implements View.OnClickListener{
    private EditText editText;
    private String inputCity;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private Weather wea;
    String TAG="mainAct";
    static private SharedPreferences prefs;
    static private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        prefs = getSharedPreferences("cities",MODE_PRIVATE);

        if(!sharedPreferenceIsEmpty()){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),ViewPageActivity.class);
            startActivity(intent);
        }
        //define search Button and voiceSearch Button
        Button search = (Button)findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.cityName);
        search.setOnClickListener(this);
        Button voiceSearch = (Button) findViewById(R.id.voiceSearch);
        voiceSearch.setOnClickListener(this);

        //define City Button on home page and setOnclickListener
        Button button1 = (Button)findViewById(R.id.beijing);
        Button button2 = (Button)findViewById(R.id.changchun);
        Button button3 = (Button)findViewById(R.id.chongqing);
        Button button4 = (Button)findViewById(R.id.changsha);
        Button button5 = (Button)findViewById(R.id.dalian);
        Button button6 = (Button)findViewById(R.id.guangzhou);
        Button button7 = (Button)findViewById(R.id.haerbin);
        Button button8 = (Button)findViewById(R.id.hefei);
        Button button9 = (Button)findViewById(R.id.nanjing);
        Button button10 = (Button)findViewById(R.id.shanghai);
        Button button11 = (Button)findViewById(R.id.shenyang);
        Button button12 = (Button)findViewById(R.id.shenzhen);
        Button button13 = (Button)findViewById(R.id.tianjing);
        Button button14 = (Button)findViewById(R.id.wuhan);
        Button button15 = (Button)findViewById(R.id.xian);
        Button button16 = (Button)findViewById(R.id.zhengzhou);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
        button13.setOnClickListener(this);
        button14.setOnClickListener(this);
        button15.setOnClickListener(this);
        button16.setOnClickListener(this);

//        //NetworkBroadcast
//        intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        networkChangeReceiver = new NetworkChangeReceiver();
//        registerReceiver(networkChangeReceiver,intentFilter);
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=592523d0");
    }
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        Log.d(TAG, "onDestroy:");
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.search:
                inputCity = editText.getText().toString();

                ExecutorService executorService = Executors.newCachedThreadPool();
                Future<Weather> futureWeather = executorService.submit(new GetWeatherThread(inputCity));
                try {
                    wea=futureWeather.get();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }finally {
                    executorService.shutdown();
                }
                if(wea.status.equals("ok")){
                    addCityThread addcity = new addCityThread(wea.city);
                    Thread threadAddCity = new Thread(addcity);
                    threadAddCity.start();
                    Intent intentDis = new Intent(MainActivity.this,ViewPageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("weather",wea);
                    intentDis.putExtras(bundle);
                    startActivity(intentDis);
                }
                break;
            case R.id.beijing:
            case R.id.changchun:
            case R.id.changsha:
            case R.id.chongqing:
            case R.id.dalian:
            case R.id.guangzhou:
            case R.id.haerbin:
            case R.id.hefei:
            case R.id.nanjing:
            case R.id.shenyang:
            case R.id.shanghai:
            case R.id.shenzhen:
            case R.id.tianjing:
            case R.id.wuhan:
            case R.id.xian:
            case R.id.zhengzhou :
                Button temp = (Button)v;
                inputCity = temp.getText().toString();
                ExecutorService executorService2 = Executors.newCachedThreadPool();
                Future<Weather> futureWeather2 = executorService2.submit(new GetWeatherThread(inputCity));
                try {
                    wea=futureWeather2.get();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }finally {
                    executorService2.shutdown();
                }
                if(wea.status.equals("ok")){
                    addCityThread addcity = new addCityThread(wea.city);
                    Thread threadAddCity = new Thread(addcity);
                    threadAddCity.start();
                    Intent intentDis = new Intent(MainActivity.this,ViewPageActivity.class);
                    startActivity(intentDis);
                }
                break;
            case R.id.voiceSearch:
                initSpeech(this);
                break;
            default:
                break;
        }
    }
    class NetworkChangeReceiver extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent){
            ConnectivityManager connectionManger = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManger.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isAvailable() ){
                Toast.makeText(context,"网络可用",Toast.LENGTH_SHORT).show();
                Log.d("internet", "网络可用");
            }else {
                Toast.makeText(context,"网络不可用",Toast.LENGTH_SHORT).show();
                Log.d("internet", "网络不可用");
            }
        }
    }
   static class addCityThread implements Runnable {
       public addCityThread(String in) {
           city = in;
           containCity = false;
       }

       private String city;
       private  boolean containCity;
       @Override
       public void run() {
           Set<String> set = new HashSet<>();
           Set<String> set2 = new TreeSet<>();
           set = prefs.getStringSet("cities", set);
           for (Iterator<String> it = set.iterator(); it.hasNext(); ){
               String temp = it.next();
               if (containCity) {
                   set2.add(new ElementStore(set2.size(), ElementStore.elementStringToCity(temp)).toString());
               } else {
                   if (temp.contains(city)) {
                       containCity = true;
                   } else {
                       set2.add(new ElementStore(set2.size(), ElementStore.elementStringToCity(temp)).toString());
                   }
               }
           }
           editor = prefs.edit();
           ElementStore e = new ElementStore(set2.size(), city);
           set2.add(e.toString());
           editor.putStringSet("cities", set2);
           editor.apply();
           Log.d("mainact", "run: " + set2.toString());
       }
   }
    static class deleteCityThread implements Runnable{
        private String city;
        deleteCityThread(String in){city=in;}
        @Override
        public void run() {
            Set<String> set = new HashSet<>();
            Set<String> set2 = new HashSet<>();
            set = prefs.getStringSet("cities",set);
            int flag=0;
            for(Iterator<String> it=set.iterator();it.hasNext();){
                String temp = it.next();
                if(temp.contains(city)){
                    flag=1;
                }else{
                    if(flag==1){
                        set2.add(new ElementStore(set2.size(),ElementStore.elementStringToCity(temp)).toString());
                    }else
                        set2.add(temp);
                }
            }
            editor = prefs.edit();
            editor.putStringSet("cities",set2);
            editor.apply();
        }
    }

    public static String[] getCities(){
        String[] citiesSHaredPreference;
        Set<String> temp = new HashSet<>();
        Set<String> temp2=new TreeSet<>();
        temp = prefs.getStringSet("cities", temp);
        citiesSHaredPreference = new String[temp.size()];
        for (Iterator<String> it = temp.iterator(); it.hasNext();) {
            temp2.add(it.next());
        }
        int i=temp2.size()-1;
        for(Iterator<String>it=temp2.iterator();it.hasNext();i--){
            citiesSHaredPreference[i] = ElementStore.elementStringToCity(it.next());
        }
        return citiesSHaredPreference;
    }
    public boolean sharedPreferenceIsEmpty(){
        Set<String> set = new HashSet<>();
        set = prefs.getStringSet("cities",set);
        return set.isEmpty();
    }
    static public void sharePreferencePrint(){
        Set<String> set = new HashSet<>();
        set = prefs.getStringSet("cities",set);
        for(Iterator<String> it=set.iterator();it.hasNext();){
            Log.d("main act", "sharePreferencePrint: "+it.next());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }


    public void initSpeech(final Context context) {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context,null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    String result = parseVoice(recognizerResult.getResultString());
                    editText.setText(result);
                }
            }
            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * 语音对象封装
     */
    public class Voice {

        public ArrayList<WSBean> ws;

        public class WSBean {
            public ArrayList<CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }
}
