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
    @Test
    public void putMarbleInNextCupPlayer1() {
        game.getPlayer1().setTurn(true);
        game.getPlayer2().setTurn(false);

        game.getBoardCups()[1].addMarbles(-3);
        game.getBoardCups()[2].addMarbles(-6);
        game.getBoardCups()[3].addMarbles(-5);
        game.getBoardCups()[4].addMarbles(3);
        game.getBoardCups()[5].addMarbles(8);

        int idPressedCup = 0;
        game.getBoardCups()[idPressedCup].addMarbles(-2);
        int marblesFromPressedCup = game.getBoardCups()[idPressedCup].getMarbles();

        game.putMarblesInNextCups(idPressedCup, marblesFromPressedCup);

        assertEquals(game.getBoardCups()[idPressedCup + 1].getMarbles(), 5);
        assertEquals(game.getBoardCups()[idPressedCup+2].getMarbles(), 2);
        assertEquals(game.getBoardCups()[idPressedCup+3].getMarbles(), 3);
        assertEquals(game.getBoardCups()[idPressedCup+4].getMarbles(), 11);
        assertEquals(game.getBoardCups()[idPressedCup+5].getMarbles(), 16);
    }

    @Test
    public void putMarbleInNextCupJumpOppositePlayerCupPlayer1() {
        game.getPlayer1().setTurn(true);
        game.getPlayer2().setTurn(false);

        game.getBoardCups()[5].addMarbles(-3);
        game.getBoardCups()[6].addMarbles(-6);
        game.getBoardCups()[7].addMarbles(3);
        game.getBoardCups()[8].addMarbles(3);
        game.getBoardCups()[9].addMarbles(8);
        game.getBoardCups()[10].addMarbles(5);
        game.getBoardCups()[11].addMarbles(3);
        game.getBoardCups()[12].addMarbles(7);
        game.getBoardCups()[13].addMarbles(12);
        game.getBoardCups()[14].addMarbles(1);
        game.getBoardCups()[15].addMarbles(0);
        game.getBoardCups()[0].addMarbles(4);
        game.getBoardCups()[1].addMarbles(3);
        game.getBoardCups()[2].addMarbles(13);
        game.getBoardCups()[3].addMarbles(-3);

        int idPressedCup = 4;
        game.getBoardCups()[idPressedCup].addMarbles(9); //7 + 9 = 16 full circle
        int marblesFromPressedCup = game.getBoardCups()[idPressedCup].getMarbles();
        game.getBoardCups()[idPressedCup].addMarbles(-16);
        game.putMarblesInNextCups(idPressedCup, marblesFromPressedCup);


        assertEquals(game.getBoardCups()[idPressedCup].getMarbles(), 1);
        assertEquals(game.getBoardCups()[idPressedCup + 1].getMarbles(), 6);
        assertEquals(game.getBoardCups()[idPressedCup + 2].getMarbles(), 2);
        assertEquals(game.getBoardCups()[idPressedCup+3].getMarbles(), 4);
        assertEquals(game.getBoardCups()[idPressedCup+4].getMarbles(), 11);
        assertEquals(game.getBoardCups()[idPressedCup+5].getMarbles(), 16);
        assertEquals(game.getBoardCups()[idPressedCup+6].getMarbles(), 13);
        assertEquals(game.getBoardCups()[idPressedCup+7].getMarbles(), 11);
        assertEquals(game.getBoardCups()[idPressedCup+8].getMarbles(), 15);
        assertEquals(game.getBoardCups()[idPressedCup+9].getMarbles(), 20);
        assertEquals(game.getBoardCups()[idPressedCup+10].getMarbles(), 9);
        assertEquals(game.getBoardCups()[idPressedCup+11].getMarbles(), 0);
        assertEquals(game.getBoardCups()[idPressedCup-idPressedCup].getMarbles(), 12);
        assertEquals(game.getBoardCups()[idPressedCup-idPressedCup+1].getMarbles(), 11);
        assertEquals(game.getBoardCups()[idPressedCup-idPressedCup+2].getMarbles(), 21);
        assertEquals(game.getBoardCups()[idPressedCup-idPressedCup+3].getMarbles(), 5);

    }
}
