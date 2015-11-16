package uk.co.ivaylokhr.crawl;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.opengl.Visibility;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.TouchUtils;
import android.util.Log;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Set;

import uk.co.ivaylokhr.crawl.Activities.AIGameActivity;
import uk.co.ivaylokhr.crawl.Activities.GameActivity;
import uk.co.ivaylokhr.crawl.Activities.HighScores;
import uk.co.ivaylokhr.crawl.Activities.MainActivity;
import uk.co.ivaylokhr.crawl.Activities.Settings;
import uk.co.ivaylokhr.crawl.Controller.AIGame;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    Button onePlayer,twoPlayer,statistics,settings;
    MainActivity activity;
    AIGameActivity acitvityAI;

    public MainActivityTest() {
        super(MainActivity.class);
    }
    @Before
    protected void setUp() throws Exception {
        super.setUp();
        getInstrumentation().setInTouchMode(false);
        //setActivityInitialTouchMode(true);
        activity = getActivity();
        onePlayer = (Button)activity.findViewById(R.id.button);
        twoPlayer=(Button)activity.findViewById(R.id.button9);
        statistics=(Button)activity.findViewById(R.id.button2);
        settings=(Button)activity.findViewById(R.id.button3);
        Log.i("tag","SetUp");
    }

    @Test
    public void testActivityExists() {
        assertNotNull(activity);
        Log.i("tag", "testActivityExists");

    }

    @Test
    public void testZOnePlayerClick() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(AIGameActivity.class.getName(), null, false);
        final Button ai = (Button) activity.findViewById(R.id.button);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ai.performClick();
            }
        });
        AIGameActivity nextActivity = (AIGameActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor,5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        assertNotNull(activity);
        //nextActivity.finish();
        Log.i("tag", "Done testOnePlayerClick");

    }
    @Test
    public void testZStatsClick() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(HighScores.class.getName(), null, false);
        final Button stat = (Button) activity.findViewById(R.id.button2);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stat.performClick();
            }
        });
        //TouchUtils.clickView(this, cancel);
        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        HighScores nextActivity = (HighScores) getInstrumentation().waitForMonitorWithTimeout(activityMonitor,5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity.finish();
        Log.i("tag", "Done testStatsClick");

    }
    @Test
    public void testZSettingsClick() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Settings.class.getName(), null, false);
        final Button sett = (Button) activity.findViewById(R.id.button3);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sett.performClick();
            }
        });
        //TouchUtils.clickView(this, cancel);
        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        Settings nextActivity = (Settings) getInstrumentation().waitForMonitorWithTimeout(activityMonitor,5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity.finish();
        Log.i("tag", "Done testSettingsClick");

    }

}