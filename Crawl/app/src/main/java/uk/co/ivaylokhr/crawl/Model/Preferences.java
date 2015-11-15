package uk.co.ivaylokhr.crawl.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferences {

    //Sends a integer "value" with a key "key" to be stored in the pKey
    public static void toPreferences(Context context, Integer value, String key, String pKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //retrieves an integer "value" with a key "key" from pKey
    public static Integer fromPreferences(Context context,Integer defaultValue, String key, String pKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pKey, Context.MODE_PRIVATE);
        Integer temp = sharedPreferences.getInt(key, defaultValue);
        return temp;
    }
}