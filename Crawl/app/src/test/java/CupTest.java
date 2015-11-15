import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

import uk.co.ivaylokhr.crawl.Model.Cup;

public class CupTest {

    Cup cup ;

    @Before
    public void setUp() {
        cup = new Cup();
    }

    @Test
    public void addMarblesTest() {
        cup.addMarbles(5);
        assertEquals(cup.getMarbles(), 5);
        cup.addMarbles(6);
        assertEquals(cup.getMarbles(), 11);
    }

}
