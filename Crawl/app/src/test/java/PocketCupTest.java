import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

import uk.co.ivaylokhr.crawl.Model.PocketCup;

public class PocketCupTest {

    PocketCup pocketCup ;

    @Before
    public void setUp() {
        pocketCup = new PocketCup();
    }

    @Test
    public void emptyCupTest() {
        assertEquals(pocketCup.getMarbles(), 7);
        pocketCup.emptyCup();
        assertEquals(pocketCup.getMarbles(), 0);
    }
}
