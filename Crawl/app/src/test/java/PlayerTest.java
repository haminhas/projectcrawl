import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

import uk.co.ivaylokhr.crawl.Model.Player;

public class PlayerTest {

    Player player ;

    @Before
    public void setUp() {
        player = new Player();
    }

    @Test
    public void setTurnTest() {
        player.setTurn(true);
        assertTrue(player.getTurn());
        player.setTurn(false);
        assertFalse(player.getTurn());
    }

}
