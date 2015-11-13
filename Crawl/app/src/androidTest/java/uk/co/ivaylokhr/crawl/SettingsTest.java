package uk.co.ivaylokhr.crawl;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Test;

import java.util.jar.Manifest;

import uk.co.ivaylokhr.crawl.Activities.MainActivity;
import uk.co.ivaylokhr.crawl.Activities.Settings;

/**
 * Created by Hassan on 13/11/2015.
 */
public class SettingsTest extends ActivityInstrumentationTestCase2<Settings> {
    private Settings mActivity;
    private EditText one;
    private EditText two;
    private Button save1;
    private String s;
    private String s2;
    private Intent mMainIntent;

    public SettingsTest() {
        super(Settings.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        one = (EditText) mActivity.findViewById(R.id.editText);
        two = (EditText) mActivity.findViewById(R.id.editText2);
        save1 = (Button) mActivity.findViewById(R.id.button6);
        s = mActivity.player1;
        s2 = mActivity.player2;
        mMainIntent = new Intent(Intent.ACTION_MAIN);

    }
    @Test
    public void testPreconditions() {
        assertNotNull(one);
        assertNotNull(two);
    }
    @Test
    public void testEnsure() {
        // Type text and then press the button.
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                one.setText("test");
                two.setText("two");
            }
        });
        TouchUtils.clickView(this, save1);
        assertEquals(s, "test");
        assertEquals(s2, "two");
    }

    public void testOpenNextActivity(){
        Button cancel = (Button) mActivity.findViewById(R.id.button5);
        TouchUtils.clickView(this, cancel);
    }

    public void testCancel() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        Button cancel = (Button) mActivity.findViewById(R.id.button5);
        TouchUtils.clickView(this, cancel);
        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        MainActivity nextActivity = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 10000);
        // next activity is opened and captured.
        assertEquals(MainActivity.class, nextActivity);
        assertNotNull(nextActivity);
        nextActivity.finish();
    }
}
