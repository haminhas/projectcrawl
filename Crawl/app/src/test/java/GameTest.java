import org.junit.Before;
import org.junit.Test;

import uk.co.ivaylokhr.crawl.Controller.Game;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class GameTest {

    Game game;

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
    public void putMarbleInNextCupPlayer1Test() {
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
    public void putMarbleInNextCupJumpOppositePlayerCupPlayer1Test() {
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

    @Test
    public void putMarbleInNextCupPlayer2Test() {
        game.getPlayer1().setTurn(true);
        game.getPlayer2().setTurn(false);

        game.getBoardCups()[9].addMarbles(-5);
        game.getBoardCups()[10].addMarbles(8);
        game.getBoardCups()[11].addMarbles(2);
        game.getBoardCups()[12].addMarbles(11);
        game.getBoardCups()[13].addMarbles(-2);
        game.getBoardCups()[14].addMarbles(4);

        int idPressedCup = 8;
        game.getBoardCups()[idPressedCup].addMarbles(-1);
        int marblesFromPressedCup = game.getBoardCups()[idPressedCup].getMarbles();

        game.putMarblesInNextCups(idPressedCup, marblesFromPressedCup);

        assertEquals(game.getBoardCups()[idPressedCup+1].getMarbles(), 3);
        assertEquals(game.getBoardCups()[idPressedCup+2].getMarbles(), 16);
        assertEquals(game.getBoardCups()[idPressedCup+3].getMarbles(), 10);
        assertEquals(game.getBoardCups()[idPressedCup+4].getMarbles(), 19);
        assertEquals(game.getBoardCups()[idPressedCup+5].getMarbles(), 6);
        assertEquals(game.getBoardCups()[idPressedCup+6].getMarbles(), 12);

    }

    @Test
    public void putMarbleInNextCupJumpOppositePlayerCupPlayer2Test() {
        game.getPlayer1().setTurn(false);
        game.getPlayer2().setTurn(true);

        game.getBoardCups()[9].addMarbles(4);
        game.getBoardCups()[10].addMarbles(-4);
        game.getBoardCups()[11].addMarbles(8);
        game.getBoardCups()[12].addMarbles(3);
        game.getBoardCups()[13].addMarbles(1);
        game.getBoardCups()[14].addMarbles(0);
        game.getBoardCups()[15].addMarbles(2);
        game.getBoardCups()[0].addMarbles(-6);
        game.getBoardCups()[1].addMarbles(2);
        game.getBoardCups()[2].addMarbles(11);
        game.getBoardCups()[3].addMarbles(9);
        game.getBoardCups()[4].addMarbles(-1);
        game.getBoardCups()[5].addMarbles(0);
        game.getBoardCups()[6].addMarbles(3);
        game.getBoardCups()[7].addMarbles(0);

        int idPressedCup = 8;
        game.getBoardCups()[idPressedCup].addMarbles(9); //7 + 9 = 16 full circle
        int marblesFromPressedCup = game.getBoardCups()[idPressedCup].getMarbles();
        game.getBoardCups()[idPressedCup].addMarbles(-16);
        game.putMarblesInNextCups(idPressedCup, marblesFromPressedCup);

        assertEquals(game.getBoardCups()[8].getMarbles(), 1);
        assertEquals(game.getBoardCups()[9].getMarbles(), 13);
        assertEquals(game.getBoardCups()[10].getMarbles(), 4);
        assertEquals(game.getBoardCups()[11].getMarbles(), 16);
        assertEquals(game.getBoardCups()[12].getMarbles(), 11);
        assertEquals(game.getBoardCups()[13].getMarbles(), 9);
        assertEquals(game.getBoardCups()[14].getMarbles(), 8);
        assertEquals(game.getBoardCups()[15].getMarbles(), 3);
        assertEquals(game.getBoardCups()[0].getMarbles(), 2);
        assertEquals(game.getBoardCups()[1].getMarbles(), 10);
        assertEquals(game.getBoardCups()[2].getMarbles(), 19);
        assertEquals(game.getBoardCups()[3].getMarbles(), 17);
        assertEquals(game.getBoardCups()[4].getMarbles(), 7);
        assertEquals(game.getBoardCups()[5].getMarbles(), 8);
        assertEquals(game.getBoardCups()[6].getMarbles(), 11);
        assertEquals(game.getBoardCups()[7].getMarbles(), 0);
    }


// pressCup()
    @Test
    public void PressCupPlayer1Test() {
        game.getPlayer1().setTurn(true);
        game.getPlayer2().setTurn(false);
        game.setIsFirstTurn(false);
        int currentCupID = 2;
        int marblesFromPressedCup = game.getBoardCups()[currentCupID].getMarbles();
        int[] currentMarblesInCups = new int[marblesFromPressedCup]; //array to store current marbles for each next cup

        //get how many marbles there currently are in the next cups
        for(int i=0; i < marblesFromPressedCup; i++) {
            currentMarblesInCups[i] = game.getBoardCups()[currentCupID + i + 1].getMarbles();
        }

        game.pressCup(currentCupID);

        //check that the turn changed
        assertFalse(game.getPlayer1().getTurn());
        assertTrue(game.getPlayer2().getTurn());

        //check if the pressed cup is empty
        assertEquals(game.getBoardCups()[2].getMarbles(), 0);

        //check if the marbles have been added successfully to the next cups
        assertEquals(game.getBoardCups()[3].getMarbles(), currentMarblesInCups[0]+1);
        assertEquals(game.getBoardCups()[4].getMarbles(), currentMarblesInCups[1]+1);
        assertEquals(game.getBoardCups()[5].getMarbles(), currentMarblesInCups[2]+1);
        assertEquals(game.getBoardCups()[6].getMarbles(), currentMarblesInCups[3]+1);
        assertEquals(game.getBoardCups()[7].getMarbles(), currentMarblesInCups[4]+1);
        assertEquals(game.getBoardCups()[8].getMarbles(), currentMarblesInCups[5]+1);
        assertEquals(game.getBoardCups()[9].getMarbles(), currentMarblesInCups[6]+1);
    }

    @Test
    public void PressCupPlayer2Test() {
        game.getPlayer1().setTurn(false);
        game.getPlayer2().setTurn(true);
        game.setIsFirstTurn(false);
        int currentCupID = 10;
        int marblesFromPressedCup = game.getBoardCups()[currentCupID].getMarbles();
        int[] currentMarblesInCups = new int[marblesFromPressedCup]; //array to store current marbles for each next cup

        //get how many marbles there currently are in the next cups
        for(int i=0; i < marblesFromPressedCup; i++) {
            int cupID = currentCupID + i + 1;
            if(cupID > 15) {
                cupID = 0;
            }
            currentMarblesInCups[i] = game.getBoardCups()[cupID].getMarbles();
        }

        game.pressCup(currentCupID);

        //check that the turn changed
        assertTrue(game.getPlayer1().getTurn());
        assertFalse(game.getPlayer2().getTurn());

        //check if the pressed cup is empty
        assertEquals(game.getBoardCups()[10].getMarbles(), 0);

        //check if the marbles have been added successfully to the next cups
        assertEquals(game.getBoardCups()[11].getMarbles(), currentMarblesInCups[0] + 1);
        assertEquals(game.getBoardCups()[12].getMarbles(), currentMarblesInCups[1] + 1);
        assertEquals(game.getBoardCups()[13].getMarbles(), currentMarblesInCups[2] + 1);
        assertEquals(game.getBoardCups()[14].getMarbles(), currentMarblesInCups[3] + 1);
        assertEquals(game.getBoardCups()[15].getMarbles(), currentMarblesInCups[4] + 1);
        assertEquals(game.getBoardCups()[0].getMarbles(), currentMarblesInCups[5] + 1);
        assertEquals(game.getBoardCups()[1].getMarbles(), currentMarblesInCups[6]+1);
    }


//  firstTurn stuff
    @Test
    public void assureItsNoOnesTurnAtStart(){
        assertFalse(game.getPlayer1().getTurn());
        assertFalse(game.getPlayer2().getTurn());
    }

    @Test
    public void player1PressCupOnStart(){
        int firstTurnID = 2;
        int secondTurnID = 11;

        int[] marblesArray = new int[16];

        for (int i = 0; i < 16; i++){
            int marbles;
            if(i == firstTurnID || i == secondTurnID){
                marbles = 0;
            }
            else{
                marbles = game.getBoardCups()[i].getMarbles();
            }
            marblesArray[i] = marbles;
        }

        game.firstTurnPlay(firstTurnID);
        int marblesFromFirstCup = game.getBoardCups()[firstTurnID].getMarbles();

        assertFalse(game.getPlayer1().getTurn());
        assertTrue(game.getPlayer2().getTurn());

        game.firstTurnPlay(secondTurnID);
        int marblesFromSecondCup = game.getBoardCups()[secondTurnID].getMarbles();

        assertTrue(game.getPlayer1().getTurn());
        assertFalse(game.getPlayer2().getTurn());


        for (int i = 1; i <= marblesFromFirstCup; i++){
            int nextID = firstTurnID + i;
            if(nextID > 15){
                nextID -= 15;
            }
            assertEquals(game.getBoardCups()[nextID].getMarbles(), marblesArray[nextID]+1);
        }

        for (int i = 1; i <= marblesFromSecondCup; i++){
            int nextID = firstTurnID + i;
            if(nextID > 15){
                nextID -= 15;
            }
            assertEquals(game.getBoardCups()[nextID].getMarbles(), marblesArray[nextID]+1);
        }
    }

    @Test
    public void player2PressCupOnStart(){
        int firstTurnID = 11;
        int secondTurnID = 2;

        int[] marblesArray = new int[16];

        for (int i = 0; i < 16; i++){
            int marbles;
            if(i == firstTurnID || i == secondTurnID){
                marbles = 0;
            }
            else{
                marbles = game.getBoardCups()[i].getMarbles();
            }
            marblesArray[i] = marbles;
        }

        game.firstTurnPlay(firstTurnID);
        int marblesFromFirstCup = game.getBoardCups()[firstTurnID].getMarbles();

        assertTrue(game.getPlayer1().getTurn());
        assertFalse(game.getPlayer2().getTurn());

        game.firstTurnPlay(secondTurnID);
        int marblesFromSecondCup = game.getBoardCups()[secondTurnID].getMarbles();

        assertFalse(game.getPlayer1().getTurn());
        assertTrue(game.getPlayer2().getTurn());

        for (int i = 1; i <= marblesFromFirstCup; i++){
            int nextID = firstTurnID + i;
            if(nextID > 15){
                nextID -= 15;
            }
            assertEquals(game.getBoardCups()[nextID].getMarbles(), marblesArray[nextID]+1);
        }

        for (int i = 1; i <= marblesFromSecondCup; i++){
            int nextID = firstTurnID + i;
            if(nextID > 15){
                nextID -= 15;
            }
            assertEquals(game.getBoardCups()[nextID].getMarbles(), marblesArray[nextID]+1);
        }
    }

}
