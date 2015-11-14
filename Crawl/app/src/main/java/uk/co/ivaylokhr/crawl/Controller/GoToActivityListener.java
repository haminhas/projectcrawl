package uk.co.ivaylokhr.crawl.Controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by Ivaylo on 14/11/2015.
 */

//This is for the dialog. It goes to the main menu if you say you want to
public class GoToActivityListener implements DialogInterface.OnClickListener{

    private Intent newActivity;
    private Activity currentActivity;

    public GoToActivityListener(Activity current, Intent intent){
        currentActivity = current;
        newActivity = intent;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        currentActivity.finish();
        currentActivity.startActivity(newActivity);
    }
}