import android.util.Log;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static junit.framework.Assert.*;

import uk.co.ivaylokhr.crawl.Controller.Game;
import uk.co.ivaylokhr.crawl.Model.Player;

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


//  areThereValidMoves()

    @Test
    public void noValidMovesForPlayer1Test() {
        //empty player1's cups
        for(int i=0; i<7; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        assertFalse(game.areThereValidMoves(game.getPlayer1()));
    }

    @Test
    public void noValidMovesForPlayer2Test() {
        for(int i=8; i<15; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        assertFalse(game.areThereValidMoves(game.getPlayer2()));
    }

    @Test
    public void validMovesForPlayer1Test() {
        //empty player1's cups
        for(int i=0; i<7; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        game.getBoardCups()[5].addMarbles(2);
        assertTrue(game.areThereValidMoves(game.getPlayer1()));
    }

    @Test
    public void validMovesForPlayer2Test() {
        for(int i=8; i<15; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        game.getBoardCups()[9].addMarbles(1);
        assertTrue(game.areThereValidMoves(game.getPlayer2()));
    }


//    switchTurn()
    @Ignore
    @Test
    public void switchTurnPlayer1() {
        game.getPlayer1().setTurn(true);
        game.getPlayer2().setTurn(false);
        assertEquals(game.getBoardCups()[10].getMarbles(), 7);

        assertTrue(game.areThereValidMoves(game.getPlayer2()));

        game.switchTurn();
        assertFalse(game.getPlayer1().getTurn());
        assertTrue(game.getPlayer2().getTurn());
    }

    @Ignore
    @Test
    public void switchTurnPlayer2() {
        game.getPlayer1().setTurn(false);
        game.getPlayer2().setTurn(true);
        assertEquals(game.getBoardCups()[3].getMarbles(), 7);

        assertTrue(game.areThereValidMoves(game.getPlayer1()));

        game.switchTurn();
        assertTrue(game.getPlayer1().getTurn());
        assertFalse(game.getPlayer2().getTurn());
    }

}
