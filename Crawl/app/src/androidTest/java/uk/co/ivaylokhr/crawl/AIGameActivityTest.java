package uk.co.ivaylokhr.crawl;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import org.junit.Before;

import uk.co.ivaylokhr.crawl.Activities.AIGameActivity;

public class AIGameActivityTest extends ActivityInstrumentationTestCase2<AIGameActivity> {

    Button[] buttonArray;
    Button button0;

    public AIGameActivityTest() {
        super(AIGameActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        getInstrumentation().setInTouchMode(false);


        //imgButton = (ImageButton) aiGameActivity.findViewById(R.id.imageButton);
    }

    public void testActivityExits() {
        AIGameActivity aiGameActivity = new AIGameActivity();
        assertNotNull(aiGameActivity);
    }
    public void testEnabledOrDisabled(){
        AIGameActivity aiGameActivity = new AIGameActivity();
        button0= (Button)aiGameActivity.findViewById(R.id.b0);
        boolean initEnabled;
        initEnabled=button0.isEnabled();
        assertEquals(true,initEnabled);
    }
}
