package uk.co.ivaylokhr.crawl.Controller;

import java.util.ArrayList;
import java.util.Random;

import uk.co.ivaylokhr.crawl.Model.Board;
import uk.co.ivaylokhr.crawl.Model.Player;
import uk.co.ivaylokhr.crawl.Model.PocketCup;

/**
 * Created by solan_000 on 09/11/2015.
 */
public class AIGame extends Game {

    private Player aiPlayer;
    private int firstButton;
    private int marbles;

    //first turn set up
    public AIGame() {
        firstButton = 0;
        marbles = 0;
        player1 = new Player();
        aiPlayer = new Player();
        player2 = aiPlayer;
        board = new Board();
        isDraw = false;
        initialiseVariablesFirstTurn();
    }

    /**
     * Initialise values of the variables needed for first Turn
     */
    private void initialiseVariablesFirstTurn(){
        isFirstTurn = true;
        firstID = -1;
        player1.setTurn(true);
        aiPlayer.setTurn(false);
    }

    /**
     * @param id
     */
    public void setFirstHumanMove(int id){
        firstID = id;
        player1.setTurn(false);
        aiPlayer.setTurn(true);
    }

    /**
     * @return aiMoveID
     */
    public int generateFirstAIMove(){
        Random rand = new Random();
        int aiMoveID = rand.nextInt(6) + 8;
        return aiMoveID;
    }

    public void doMove(){
        //get id of the pressed cup
        ArrayList<Integer> temp = new ArrayList<>();
        //Assuming ai player is at the top
        for (int i = 8; i < 15; i++) {
            if (!board.getCups()[i].isEmpty()) {
                temp.add(i);
            }
        }
        if(temp.isEmpty()){
            switchTurn();
            return;
        }
        int id;
        if(extraTurn() != -1) {
            id = extraTurn();
        }else if(opposite() != -1){
            id = opposite();
        }else{
            Random rand = new Random();
            id = temp.get(rand.nextInt(temp.size()));
        }
        //empty cup
        PocketCup pressedPocketCup = (PocketCup) board.getCups()[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        firstButton = id;
        marbles = marblesFromEmptiedCup;
        putMarblesInNextCups(id, marblesFromEmptiedCup);
    }

    /**
     * decide turn after the AI has played
     * @param id
     * @param marblesFromEmptiedCup
     */
    public void switchTurnsAfterAITurn(int id, int marblesFromEmptiedCup){
        //at this point please check if the cup in the array still has the marbles
        int finalButtonID = id + marblesFromEmptiedCup;
        if (finalButtonID > 15) {
            finalButtonID -= 15;
        }
        if (!isGameFinished() && !giveAnotherTurn(finalButtonID)) {
            switchTurn();
        }
    }

    /**
     * checks to see if there is an cup that can be stolen
     * @return i
     */
    private int opposite() {
        for(int i = 8; i < 15;i++){
            int op = (board.getCups()[i].getMarbles() + i)%16;
            if((op != 7 && op != 15) && board.getCups()[i].getMarbles() != 0){
                PocketCup nextPocketCup = (PocketCup) board.getCups()[op];
                if(nextPocketCup.isEmpty()){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     *checks to see if the AI gets another turn
     * @return
     */
    private int extraTurn(){
        for(int i = 14; i > 7;i--){
            if((board.getCups()[i].getMarbles() + i)%15 == 0){
                return i;
            }
        }
        return -1;
    }

    /**
     *  This is called only in the first turn to update the board after both players made their moves
     * @param firstID
     * @param secondID
     */
    //should @Override if this methos goes public in Game
    public void applyFirstTurnChanges(int firstID, int secondID){
        int humanMoveMarbles = ((PocketCup)board.getCups()[firstID]).emptyCup();
        int aiMovieMarbles = ((PocketCup)board.getCups()[secondID]).emptyCup();
        for (int i = 1; i < humanMoveMarbles + 1; i++){
            int nextCupID = i + firstID;
            board.getCups()[nextCupID].addMarbles(1);
        }
        for(int i = 1; i < aiMovieMarbles + 1; i++){
            int nextCupID = i + secondID;
            if(nextCupID > 15){
                nextCupID -= 16;
            }
            board.getCups()[nextCupID].addMarbles(1);
        }
        aiPlayer.setTurn(false);
        player1.setTurn(true);
        isFirstTurn = false;
    }

    /**
     * @return marbles
     */
    public int returnMarbles(){
        return marbles;
    }

    /**
     * @return firstButton
     */
    public int returnFirstButton(){
        return firstButton;
    }
    /**
     * @return player1
     */
    public Player getHumanPlayer(){
        return getPlayer1();
    }
    /**
     * @return player2
     */
    public Player getAIPlayer(){
        return getPlayer2();
    }

}