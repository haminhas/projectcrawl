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


//    giveAnotherTurn()
    @Test
    public void giveAnotherTurnToPlayerOneTest() {
        game.getPlayer1().setTurn(true);
        game.getPlayer2().setTurn(false);
        assertTrue(game.giveAnotherTurn(7));
    }

    @Test
    public void giveAnotherTurnToPlayerTwoTest() {
        game.getPlayer1().setTurn(false);
        game.getPlayer2().setTurn(true);
        assertTrue(game.giveAnotherTurn(15));
    }

    @Test
    public void giveAnotherTurnPlayerOneFailTest() {
        game.getPlayer1().setTurn(true);
        game.getPlayer2().setTurn(false);
        for(int i = 0; i <= 15 ; i++) {
            if(i == 7) { continue ; }
            assertFalse(game.giveAnotherTurn(i));
        }
    }

    @Test
    public void giveAnotherTurnPlayerTwoFailTest() {
        game.getPlayer1().setTurn(false);
        game.getPlayer2().setTurn(true);
        for (int i = 0; i < 15 ; i++) {
            assertFalse(game.giveAnotherTurn(i));
        }
    }


//  firstTurnPlay()



}
