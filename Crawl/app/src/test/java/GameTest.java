import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

import uk.co.ivaylokhr.crawl.Controller.Game;

public class GameTest {

    Game game ;

    @Before
    public void setUp() {
        game = new Game();
    }


    @Test
    public void giveAnotherTurnToPlayerOneTest() {
        game.getPlayer1().setTurn(true);
        game.getPlayer2().setTurn(false);
        assertEquals(game.giveAnotherTurn(7), true);
    }

    @Test
    public void giveAnotherTurnToPlayerTwoTest() {
        game.getPlayer1().setTurn(false);
        game.getPlayer2().setTurn(true);
        assertEquals(game.giveAnotherTurn(15), true);
    }

    

}
