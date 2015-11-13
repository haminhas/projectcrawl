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
        //fail if marble does not land in PlayerCup
        for(int i = 0; i <= 15 ; i++) {
            if(i == 7) { continue ; }
            assertFalse(game.giveAnotherTurn(i));
        }
    }

    @Test
    public void giveAnotherTurnPlayerTwoFailTest() {
        game.getPlayer1().setTurn(false);
        game.getPlayer2().setTurn(true);
        //fail if marble does not land in PlayerCup
        for (int i = 0; i < 15 ; i++) {
            assertFalse(game.giveAnotherTurn(i));
        }
    }


//  areThereValidMoves()
    @Test
    public void noValidMovesForPlayer1Test() {
        game.getPlayer1().setTurn(true);
        //empty player1's cups
        for(int i=0; i<7; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        assertFalse(game.areThereValidMoves(game.getPlayer1()));
    }

    @Test
    public void noValidMovesForPlayer2Test() {
        game.getPlayer2().setTurn(true);
        for(int i=8; i<15; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        assertFalse(game.areThereValidMoves(game.getPlayer2()));
    }

    @Test
    public void validMovesForPlayer1Test() {
        game.getPlayer1().setTurn(true);
        //empty player1's cups
        for(int i=0; i<7; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        game.getBoardCups()[5].addMarbles(2);
        assertTrue(game.areThereValidMoves(game.getPlayer1()));
    }

    @Test
    public void validMovesForPlayer2Test() {
        game.getPlayer2().setTurn(true);
        for(int i=8; i<15; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        game.getBoardCups()[9].addMarbles(1);
        assertTrue(game.areThereValidMoves(game.getPlayer2()));
    }


//    switchTurn()
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

    
//    checkWinner()
    @Test
    public void checkWinnerPlayer1Test() {
        game.getBoardCups()[7].addMarbles(60);
        game.getBoardCups()[15].addMarbles(38);
        assertEquals(game.checkWinner(), game.getPlayer1());
    }

    @Test
    public void checkWinnerScorePlayer2Test() {
        game.getBoardCups()[7].addMarbles(27);
        game.getBoardCups()[15].addMarbles(71);
        assertEquals(game.checkWinner(), game.getPlayer2());
    }


//    isGameFinished()
    @Test
    public void isGamFinishedPassTest() {
        for(int i=0; i<15; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        assertTrue(game.isGameFinished());
    }

    @Test
    public void isNotGameFinishedPlayer1Test() {
        for(int i=0; i<15; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        //player 1 valid move
        game.getBoardCups()[5].addMarbles(2);
        assertFalse(game.isGameFinished());
    }

    @Test
    public void isNotGameFinishedPlayer3Test() {
        for(int i=0; i<15; i++) {
            game.getBoardCups()[i].addMarbles(-7);
        }
        //player 2 valid move
        game.getBoardCups()[10].addMarbles(3);
        assertFalse(game.isGameFinished());
    }


//    getFinalResults()
    @Test
    public void getFinalResultsWithPlayer1WinnerTest() {
        game.getBoardCups()[7].addMarbles(72);
        game.getBoardCups()[15].addMarbles(26);
        String[] results = game.getFinalResults();

        assertEquals(results[0], game.getPlayer1().getName());
        assertEquals(results[1], Integer.toString(game.getBoard().getPlayerCup1().getMarbles()));
        assertEquals(results[2], game.getPlayer2().getName());
        assertEquals(results[3], Integer.toString(game.getBoard().getPlayerCup2().getMarbles()));
    }

    @Test
    public void getFinalResultsWithPlayer2WinnerTest() {
        game.getBoardCups()[7].addMarbles(10);
        game.getBoardCups()[15].addMarbles(88);
        String[] results = game.getFinalResults();

        assertEquals(results[0], game.getPlayer2().getName());
        assertEquals(results[1], Integer.toString(game.getBoard().getPlayerCup2().getMarbles()));
        assertEquals(results[2], game.getPlayer1().getName());
        assertEquals(results[3], Integer.toString(game.getBoard().getPlayerCup1().getMarbles()));
    }


//  putMarbleInNextCup()

}
