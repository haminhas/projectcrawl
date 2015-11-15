package uk.co.ivaylokhr.crawl;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import uk.co.ivaylokhr.crawl.Activities.HighScores;
import uk.co.ivaylokhr.crawl.Activities.MainActivity;

public class HighScoresTest extends ActivityInstrumentationTestCase2<HighScores> {
    Button back;
    HighScores highScores;


    public HighScoresTest() {
        super(HighScores.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        getInstrumentation().setInTouchMode(false);
        highScores = getActivity();
        back = (Button)highScores.findViewById(R.id.button4);
    }
    public void testActivityExists() {
        assertNotNull(highScores);
    }

    public void testBackClick() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        highScores.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                back.performClick();
            }
        });
        MainActivity nextActivity = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor,5000);
        assertNull(nextActivity);
    }
}
