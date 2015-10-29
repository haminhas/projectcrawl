package uk.co.ivaylokhr.crawl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by solan_000 on 25/10/2015.
 */

    //Sends a integer "value" with a key "key" to be stored in the pKey
public class Prefrences {
    public static void toPreferences(Context context, Integer value, String key, String pKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void updateTime(Context context, Long value, String key, String pKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Long temp = sharedPreferences.getLong(key, 10);
        if (value > temp){
            temp = value;
        }
        editor.putLong(key, temp);
        editor.commit();
    }

    //retrieves an integer "value" with a key "key" from pKey
    public static Integer fromPreferences(Context context,Integer defaultValue, String key, String pKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pKey, Context.MODE_PRIVATE);
        Integer temp = sharedPreferences.getInt(key, defaultValue);
        return temp;
    }
}