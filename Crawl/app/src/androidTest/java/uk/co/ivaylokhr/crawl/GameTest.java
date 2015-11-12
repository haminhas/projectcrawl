package uk.co.ivaylokhr.crawl;

import android.test.ActivityInstrumentationTestCase2;

import uk.co.ivaylokhr.crawl.Controller.Game;

/**
 * Created by solan_000 on 09/11/2015.
 */
public class GameTest extends ActivityInstrumentationTestCase2<Game> {
    public GameTest() {
        super(Game.class);
    }
    public void testActivityExists() {
        Game activity = getActivity();
        assertNotNull(activity);

    }
}