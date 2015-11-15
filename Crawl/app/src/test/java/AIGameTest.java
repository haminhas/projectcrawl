import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import uk.co.ivaylokhr.crawl.Controller.AIGame;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Ivaylo on 14/11/2015.
 */
public class AIGameTest {

    AIGame aiGame;

    @Before
    public void setUp(){
        aiGame = new AIGame();
    }

    private void setAITurn(){
        aiGame.getAIPlayer().setTurn(true);
        aiGame.getHumanPlayer().setTurn(false);
        aiGame.setIsFirstTurn(false);
    }

    private void setHumanTurn(){
        aiGame.getAIPlayer().setTurn(false);
        aiGame.getHumanPlayer().setTurn(true);
        aiGame.setIsFirstTurn(false);
    }

    @Test
    public void assureItsNoOnesTurnAtStart(){
        assertTrue(aiGame.getPlayer1().getTurn());
        assertFalse(aiGame.getPlayer2().getTurn());
    }

    @Test
    public void setFirstHumanMove(){
        aiGame.setFirstHumanMove(3);
        assertEquals(aiGame.getFirstMoveID(), 3);
        assertTrue(aiGame.getAIPlayer().getTurn());
        assertFalse(aiGame.getHumanPlayer().getTurn());
    }

    @Test
    public void swithchAfterAITurn(){
        setAITurn();
        int pressedButton = 9;
        aiGame.switchTurnsAfterAITurn(pressedButton, aiGame.getBoardCups()[pressedButton].getMarbles());
        assertFalse(aiGame.getAIPlayer().getTurn());
        assertTrue(aiGame.getHumanPlayer().getTurn());
    }

    @Test
    public void notSwitchAfterAITurn(){
        setAITurn();
        for (int i = 0; i < 7; i++){
            aiGame.getBoardCups()[i].addMarbles(-7);
        }
        for (int i = 8; i < 15; i++){
            aiGame.getBoardCups()[i].addMarbles(-6);
        }
        int pressedButton = 10;
        aiGame.switchTurnsAfterAITurn(pressedButton, aiGame.getBoardCups()[pressedButton].getMarbles());
        assertTrue(aiGame.getAIPlayer().getTurn());
        assertFalse(aiGame.getHumanPlayer().getTurn());
    }

    @Test
    public void notSwitchAfterAITurnOnGameFinish(){
        setAITurn();
        for (int i = 0; i < 7; i++){
            aiGame.getBoardCups()[i].addMarbles(-7);
        }
        for (int i = 8; i < 15; i++){
            aiGame.getBoardCups()[i].addMarbles(-7);
        }
        aiGame.getBoardCups()[11].addMarbles(1);
        int pressedButton = 11;
        aiGame.switchTurnsAfterAITurn(pressedButton, aiGame.getBoardCups()[pressedButton].getMarbles());
        assertTrue(aiGame.getAIPlayer().getTurn());
        assertFalse(aiGame.getHumanPlayer().getTurn());
    }


    //doMove
    @Test
    public void notDoMoveWithNoValidMoves(){
        setAITurn();
        for (int i = 8; i < 15; i++){
            aiGame.getBoardCups()[i].addMarbles(-7);
        }
        aiGame.doMove();
        assertTrue(aiGame.getHumanPlayer().getTurn());
        assertFalse(aiGame.getAIPlayer().getTurn());
    }

    @Test
    public void goForOwnPlayerCup(){
        setAITurn();
        aiGame.doMove();
        int moveID = aiGame.returnFirstButton();
        assertEquals(moveID, 8);
    }

    @Test
    //make sure that it looks from left to right when choosing a move
    public void goForOwnPlayerCup2(){
        setAITurn();
        aiGame.getBoardCups()[14].addMarbles(-6);
        aiGame.doMove();
        int moveID = aiGame.returnFirstButton();
        assertEquals(moveID, 14);
    }

    @Test
    public void goForSteal(){
        setAITurn();
        aiGame.getBoardCups()[13].addMarbles(-7);
        aiGame.getBoardCups()[11].addMarbles(-5);
        aiGame.getBoardCups()[8].addMarbles(-7);
        aiGame.doMove();
        int moveID = aiGame.returnFirstButton();
        assertEquals(moveID, 11);
    }

    @Test
    public void goForOwnCupInsteadOfSteal(){
        setAITurn();
        aiGame.getBoardCups()[13].addMarbles(-7);
        aiGame.getBoardCups()[11].addMarbles(-5);
        aiGame.doMove();
        int moveID = aiGame.returnFirstButton();
        assertEquals(moveID, 8);
    }

    @Test
    public void dontPlayEmpltyCup(){
        setAITurn();
        for (int i = 8; i < 15; i++){
            aiGame.getBoardCups()[i].addMarbles(-7);
        }
        aiGame.getBoardCups()[10].addMarbles(3);
        aiGame.doMove();
        for (int i = 0; i < 20; i++){
            assertEquals(aiGame.returnFirstButton(), 10);
        }
    }


    //first turn stuff
    @Test
    public void humanTurnAfter1(){
        aiGame.setFirstHumanMove(3);
        Random rand = new Random();
        int aiMove = rand.nextInt(7)+8;
        aiGame.applyFirstTurnChanges(3, aiMove);
        assertTrue(aiGame.getHumanPlayer().getTurn());
        assertFalse(aiGame.getAIPlayer().getTurn());
    }

    @Test
    public void AIAfterPress(){
        aiGame.setFirstHumanMove(4);
        assertFalse(aiGame.getHumanPlayer().getTurn());
        assertTrue(aiGame.getAIPlayer().getTurn());
    }

    @Test
    public void applyChangesTurn1(){
        int humanTurn = 2;
        int[] marblesArray = new int[16];


        for (int i = 0; i < 16; i++){
            int marbles;
            marbles = aiGame.getBoardCups()[i].getMarbles();
            marblesArray[i] = marbles;
        }


        aiGame.setFirstHumanMove(humanTurn);
        int marblesFromFirstCup = aiGame.getBoardCups()[humanTurn].getMarbles();
        assertFalse(aiGame.getPlayer1().getTurn());
        assertTrue(aiGame.getAIPlayer().getTurn());


        int aiTurn = aiGame.generateFirstAIMove();
        int marblesFromSecondCup = aiGame.getBoardCups()[aiTurn].getMarbles();
        for (int i = 0; i < 15; i++){
            assertEquals(aiGame.getBoardCups()[i].getMarbles(),marblesArray[i]);
        }

        marblesArray[humanTurn] = 0;
        marblesArray[aiTurn] = 0;

        aiGame.applyFirstTurnChanges(humanTurn, aiTurn);

        assertTrue(aiGame.getHumanPlayer().getTurn());
        assertFalse(aiGame.getAIPlayer().getTurn());

        for (int i = 1; i <= marblesFromFirstCup; i++){
            int nextID = humanTurn + i;
            if(nextID > 15){
                nextID -= 16;
            }
            int change = 1;
            if(nextID > aiTurn || nextID + 9 <= aiTurn){
                change = 2;
            }
            assertEquals(aiGame.getBoardCups()[nextID].getMarbles(), marblesArray[nextID]+change);
        }

        for (int i = 1; i <= marblesFromSecondCup; i++){
            int nextID = aiTurn + i;
            int change = 1;
            if(nextID > humanTurn + 16 || nextID - humanTurn <= 7){
                change = 2;
            }
            if(nextID > 15){
                nextID -= 16;
            }
            assertEquals(aiGame.getBoardCups()[nextID].getMarbles(), marblesArray[nextID]+change);
        }
    }

}