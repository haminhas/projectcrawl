package uk.co.ivaylokhr.crawl;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import uk.co.ivaylokhr.crawl.Activities.HighScores;


public class HighScoresTest extends ActivityInstrumentationTestCase2<HighScores> {
    Button back;
    HighScores highScores;


    public HighScoresTest() {
        super(HighScores.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        highScores = getActivity();
        back = (Button)highScores.findViewById(R.id.button4);
    }
    public void testActivityExists() {
        assertNotNull(highScores);
    }

}
