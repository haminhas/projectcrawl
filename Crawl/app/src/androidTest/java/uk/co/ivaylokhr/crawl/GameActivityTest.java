package uk.co.ivaylokhr.crawl;

import android.test.ActivityInstrumentationTestCase2;
import uk.co.ivaylokhr.crawl.Activities.GameActivity;

public class GameActivityTest extends ActivityInstrumentationTestCase2<GameActivity> {
    GameActivity gameActivity;
    public GameActivityTest(){
        super(GameActivity.class);
    }
    protected void setUp() throws Exception{
        super.setUp();
        getInstrumentation().setInTouchMode(false);
        gameActivity= new GameActivity();
    }

    public void testActivityExists(){
        assertNotNull(GameActivity.class);
    }

}