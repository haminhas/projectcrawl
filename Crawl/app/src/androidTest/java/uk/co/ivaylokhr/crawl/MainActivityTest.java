package uk.co.ivaylokhr.crawl;

import android.app.Activity;
import android.app.Application;
import android.opengl.Visibility;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.TouchUtils;
import android.util.Log;
import android.widget.Button;

import uk.co.ivaylokhr.crawl.Activities.AIGameActivity;
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

    protected void setUp() throws Exception {
        super.setUp();
        getInstrumentation().setInTouchMode(false);
        //setActivityInitialTouchMode(true);
        activity = getActivity();
        onePlayer = (Button)activity.findViewById(R.id.button);
        twoPlayer=(Button)activity.findViewById(R.id.button9);
        statistics=(Button)activity.findViewById(R.id.button2);
        settings=(Button)activity.findViewById(R.id.button3);
    }

    public void testActivityExists() {
        assertNotNull(activity);
    }
      /*public void testAI(){


          TouchUtils.clickView(this, onePlayer);
          assertNotNull(AIGameActivity.class);


       }*/
    public void testOnePlayerTextChange(){
        assertEquals("1 player",onePlayer.getText());
        TouchUtils.clickView(this, twoPlayer);
        assertEquals("Host", onePlayer.getText());
    }
    public void testTwoPlayerTextChange(){
        assertEquals("2 player",twoPlayer.getText());
        TouchUtils.clickView(this, twoPlayer);
        assertEquals("Connect",twoPlayer.getText());
    }
    public void testStatisticsTextChange(){
        assertEquals("Statistics",statistics.getText());
        TouchUtils.clickView(this, twoPlayer);
        assertEquals("Hotseat",statistics.getText());
    }
    public void testSettingsAndBackTextChange(){
        assertEquals("Settings",settings.getText());
        TouchUtils.clickView(this, twoPlayer);
        assertEquals("Back",settings.getText());

        TouchUtils.clickView(this, settings);
        assertEquals("1 player", onePlayer.getText());
        assertEquals("2 player", twoPlayer.getText());
        assertEquals("Statistics", statistics.getText());
        assertEquals("Settings",settings.getText());
    }

}