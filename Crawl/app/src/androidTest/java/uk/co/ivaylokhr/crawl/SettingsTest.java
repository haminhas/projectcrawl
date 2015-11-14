package uk.co.ivaylokhr.crawl;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.jar.Manifest;

import uk.co.ivaylokhr.crawl.Activities.MainActivity;
import uk.co.ivaylokhr.crawl.Activities.Settings;

/**
 * Created by Hassan on 13/11/2015.
 */
public class SettingsTest extends ActivityInstrumentationTestCase2<Settings> {
    private Settings settings;
    private EditText one;
    private EditText two;
    private Button save1;
    private String s;
    private String s2;
    private Button cancel;

    public SettingsTest() {
        super(Settings.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        settings = getActivity();
        one = (EditText) settings.findViewById(R.id.editText);
        two = (EditText) settings.findViewById(R.id.editText2);
        save1 = (Button) settings.findViewById(R.id.button6);
        s = settings.getPlayer1();
        s2 = settings.getPlayer2();
        cancel = (Button) settings.findViewById(R.id.button5);
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
        Button cancel = (Button) settings.findViewById(R.id.button5);
        TouchUtils.clickView(this, cancel);
    }

    @Test
    public void testCancelClick() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        settings.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancel.performClick();
            }
        });
        MainActivity nextActivity = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor,5000);
        assertNull(nextActivity);
    }
}
