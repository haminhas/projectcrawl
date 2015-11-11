package uk.co.ivaylokhr.crawl.Controller;

import java.util.ArrayList;
import java.util.Random;

import uk.co.ivaylokhr.crawl.Model.Board;
import uk.co.ivaylokhr.crawl.Model.Cup;
import uk.co.ivaylokhr.crawl.Model.Player;
import uk.co.ivaylokhr.crawl.Model.PocketCup;

/**
 * Created by solan_000 on 09/11/2015.
 */
public class AIGame extends Game {

    private Player player1, ai;
    private Board board;
    private int firstButton;
    private int marbles;

    public AIGame() {
        firstButton = 0;
        marbles = 0;
        player1 = new Player();
        ai = new Player();
        board = new Board();
    }

    /**
     * Initialise values of the variables needed for first Turn
     */
    private void initialiseVariablesFirstTurn(){
        player1.setTurn(true);
        ai.setTurn(false);
    }

    /**
     * Action triggered when you press a Cup on the screen
     * @param id
     * @return
     */
    public void pressCup(int id) {
        PocketCup pressedPocketCup = (PocketCup) board.getCups()[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        putMarblesInNextCups(id, marblesFromEmptiedCup);
        int finalButtonID = id + marblesFromEmptiedCup;
        if (finalButtonID > 15) {
            finalButtonID -= 15;
        }
        if (!isGameFinished()) {
            checkForAnotherTurn(finalButtonID);
            forceSwitch();
        }
    }

    public void doMove(){
        int finalButtonID =0;
        boolean again = false;
        //get id of the pressed cup
        ArrayList<PocketCup> temp = new ArrayList<>();
        //Assuming ai player is at the top
        for (int i = 8; i < 15; i++) {
            if (!((PocketCup) board.getCups()[i]).isEmpty()) {
                temp.add((PocketCup)board.getCups()[i]);
            }
        }
        if(temp.isEmpty()){
            forceSwitch();
            return;
        }
        int id;
        if(!(extraTurn() == -1)) {
            id = extraTurn();
            again = true;
        }else if(!(opposite() == -1)){
            id = opposite();
        }else{
            Random rand = new Random();
            id = rand.nextInt(6) + 8;
        }
        //empty cup
        PocketCup pressedPocketCup = (PocketCup) board.getCups()[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        firstButton = id;
        marbles = marblesFromEmptiedCup;
        //at this point please check if the cup in the array still has the marbles
        putMarblesInNextCups(id, marblesFromEmptiedCup);

        finalButtonID = id + marblesFromEmptiedCup;
        if (finalButtonID > 15) {
            finalButtonID -= 15;
        }
        if (!isGameFinished()) {
            checkForAnotherTurn(finalButtonID);
            forceSwitch();
        }
    }

    private int opposite() {
        for(int i = 8; i < 15;i++){
            int op = (board.getCups()[i].getMarbles() +i)%16;
            if((op != 7 && op != 15) && board.getCups()[i].getMarbles() != 0){
                PocketCup nextPocketCup = (PocketCup) board.getCups()[op];
                if(nextPocketCup.isEmpty()){
                    return i;
                }
            }
        }
        return -1;
    }

    private int extraTurn(){
        for(int i = 14; i > 7;i--){
            if((board.getCups()[i].getMarbles() + i)%15 == 0){
                return i;
            }
        }
        return -1;
    }

    private void checkForAnotherTurn(int another){
        if(player1.getTurn() && another == 7){
            forceSwitch();
        }
        if(ai.getTurn() && another == 15) {
            forceSwitch();
        }
    }


    /**
     * switches turn to the player who is given as a parameter
     */
    private void switchTurns(Player player) {
        if (player.equals(ai)) {
            player1.setTurn(false);
            ai.setTurn(true);
        } else if (player.equals(player1)){
            player1.setTurn(true);
            ai.setTurn(false);
        }
        //checks if there is a valid method
        //If not, change the turn to the other player
        checkAreThereMarblesInCups();
    }

    /**
     * This method loops through the current player's half and determine if you can make a move
     * If there is no valid move, it switches the player to make a move
     */
    private void checkAreThereMarblesInCups() {
        if (player1.getTurn()) {
            //loops through the half ot player 1
            for (int i = 0; i < 7; i++) {
                // break if there is a valid move
                if (!((PocketCup) board.getCups()[i]).isEmpty()) {
                    return;
                }
            }
        } else if (ai.getTurn()) {
            //loops through the half ot player 2
            for (int i = 8; i < 15; i++) {
                //break if there is a valid move
                if (!((PocketCup) board.getCups()[i]).isEmpty()) {
                    return;
                }
            }
        }
        // if it didn't break, force the switch of turns
        forceSwitch();
    }

    /**
     * This forces the switch of turns.It is called when one player doesn't have valid moves
     */
    private void forceSwitch(){
        if(player1.getTurn()){
            switchTurns(ai);
        } else {
            switchTurns(player1);
        }
    }

    /**
     * decides which turn is next depending on the id of the final marblees that has been put
     * @param finalButtonID
     * @return
     */
    private boolean isPlayerOneTurn(int finalButtonID) {
        boolean playerOneTurn = false;
        if (player1.getTurn() && finalButtonID != 7) {
            switchTurns(ai);
        }
        //if it landed on the player1 cup and is his turn
        //we need this to make the button disabled
        else if (player1.getTurn()) {
            switchTurns(player1);
        } else if (ai.getTurn() && finalButtonID != 15) {
            switchTurns(player1);
        }
        //if it landed on the ai cup and is his turn
        //we need this to make the button disabled
        else if (ai.getTurn()) {
            switchTurns(ai);
        }
        if(player1.getTurn()){
            playerOneTurn = true;
        }
        return playerOneTurn;
    }

    public int returnMarbles(){
        return marbles;
    }

    public int returnFirstButton(){
        return firstButton;
    }

    /**
     * @param idCurrentCup
     * @param marblesFromEmptiedCup
     */
    private void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
        int cupNumber = idCurrentCup + 1;
        for (int i = 0; i < marblesFromEmptiedCup; i++) {
            //condition for when the cup is a playerCup
            if (cupNumber == 7) {
                if (player1.getTurn()) {
                    board.getPlayerCup1().addMarbles(1);
                    cupNumber++;
                    continue;
                }
                cupNumber++;
            } else if (cupNumber == 15) {
                if (ai.getTurn()) {
                    board.getPlayerCup2().addMarbles(1);
                    cupNumber = 0;
                    continue;
                }
                cupNumber = 0;
            }
            PocketCup nextPocketCup = (PocketCup) board.getCups()[cupNumber];
            //check at the last iteration if cup is empty
            if ((i == marblesFromEmptiedCup - 1) && nextPocketCup.isEmpty()) {
                PocketCup oppositeCup;
                if (player1.getTurn() && cupNumber < 7) {
                    oppositeCup = (PocketCup) board.getCups()[cupNumber + ((7 - cupNumber) * 2)];
                    int oppositeCupNumbers = oppositeCup.emptyCup();
                    //take last marble from the cup alongside the opposite cup's one.
                    nextPocketCup.addMarbles(-1);
                    board.getPlayerCup1().addMarbles(oppositeCupNumbers + 1);
                } else if (ai.getTurn() && cupNumber > 7 && cupNumber < 15) {
                    oppositeCup = (PocketCup) board.getCups()[(14 - cupNumber)];
                    int oppositeCupNumbers = oppositeCup.emptyCup();
                    //take last marble from the cup alongside the opposite cup's one.
                    nextPocketCup.addMarbles(-1);
                    board.getPlayerCup2().addMarbles(oppositeCupNumbers + 1);
                }
            }

            nextPocketCup.addMarbles(1);

            cupNumber++;
            //stay in the range of 16 cups.
            if (cupNumber > 15) {
                cupNumber = 0;
            }

        }//END OF FOR LOOP
    }


    /**
     *  This is called only in the first turn to update the board after both players made their moves
     * @param firstID
     * @param secondID
     */
    private void applyFirstTurnChanges(int firstID, int secondID){
        ((PocketCup)board.getCups()[firstID]).emptyCup();
        ((PocketCup)board.getCups()[secondID]).emptyCup();
        for (int i = 1; i < 8; i++){
            int nextCupID = i + firstID;
            board.getCups()[nextCupID].addMarbles(1);
        }
        for(int i = 1; i < 8; i++){
            int nextCupID = i + secondID;
            if(nextCupID > 15){
                nextCupID -= 16;
            }
            board.getCups()[nextCupID].addMarbles(1);
        }
    }

    public Board getBoard(){
        return board;
    }

    public boolean isPlayerOneTurn() {
        if(player1.getTurn()) {
            return true;
        }
        return false;
    }

    public boolean isGameFinished() {
        //check if game is finished
        for (int i = 0; i < board.getCups().length; i++) {
            if (i != 7 && i != 15) {
                PocketCup pocketCup = (PocketCup) board.getCups()[i];
                if (!pocketCup.isEmpty()) {
                    break;
                }
            }

            if (i == 14) {
                return true;
            }

        }
        return false;
    }

    public int checkWinnerScore() {
        //checks who won
        if(board.getPlayerCup1Marbles() > board.getPlayerCup2Marbles()) {
            return board.getPlayerCup1Marbles();
        }else if(board.getPlayerCup1Marbles() < board.getPlayerCup2Marbles()) {
            return board.getPlayerCup2Marbles();
        } else{
            return 0;
        }
    }


    public String[] checkWinner() {
        String[] results = new String[4];
        //checks who won
        if(board.getPlayerCup1Marbles() > board.getPlayerCup2Marbles()) {
            results[0] = player1.getName();
            results[1] = String.valueOf(board.getPlayerCup1Marbles());
            results[2] = "Computer";
            results[3] = String.valueOf(board.getPlayerCup2Marbles());
        }else if(board.getPlayerCup1Marbles() < board.getPlayerCup2Marbles()) {
            results[2] = player1.getName();
            results[3] = String.valueOf(board.getPlayerCup1Marbles());
            results[0] = "Computer";
            results[1] = String.valueOf(board.getPlayerCup2Marbles());
        } else{
            results[0] = player1.getName();
            results[2] = ai.getName();
        }
        return results;
    }

    public Player getAIPlayer(){
        return ai;
    }

    public Player getHumanPlayer(){
        return player1;
    }

    public Cup[] getBoardCups(){
        return board.getCups();
    }

}
