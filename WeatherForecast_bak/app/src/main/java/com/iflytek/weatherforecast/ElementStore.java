package com.iflytek.weatherforecast;

/**
 * Created by Administrator on 2017/5/26.
 */

class ElementStore {
    private int key;
    private String city;
    public ElementStore(int k,String s){
        key=k;
        city=s;
    }
    public int getKey() {
        return key;
    }

    public String getCity() {
        return city;
    }
    public void setKey(int k){key=k;}
    public void setCity(String s){city=s;}
    public String toString(){
        return String.format(key+city);
    }
    static String elementStringToCity(String in){
        String result=new String(in);
        return result.replaceAll("\\d+","");
    }
    static int  elementStringToKey(String in){
        int result = Integer.parseInt(in);
        return result;
    }
}