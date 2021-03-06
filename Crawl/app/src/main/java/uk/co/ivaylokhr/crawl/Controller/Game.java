package uk.co.ivaylokhr.crawl.Controller;


import uk.co.ivaylokhr.crawl.Model.Board;
import uk.co.ivaylokhr.crawl.Model.Cup;
import uk.co.ivaylokhr.crawl.Model.Player;
import uk.co.ivaylokhr.crawl.Model.PocketCup;

/**
 * This class holds all the logic for a two-player game.
 */

public class Game {

    protected Player player1, player2;
    protected Board board;
    protected boolean isFirstTurn;
    protected int firstID;
    protected boolean firstHasPlayed;
    protected boolean isDraw;
    //first turn stuff
    private int secondID;
    private boolean secondHasPlayed;

    /**
     * Initiates a two player game.
     */
    public Game() {
        player1 = new Player();
        player2 = new Player();
        board = new Board();
        isDraw = false;
        initialiseVariablesFirstTurn();
    }

    /**
     * Initialise values of the variables needed for first Turn
     */
    private void initialiseVariablesFirstTurn(){
        player1.setTurn(false);
        player2.setTurn(false);
        //The player who will have the first play
        firstID = -1; secondID = -1;
        firstHasPlayed = false; secondHasPlayed = false;
        isFirstTurn = true;
    }

    /**
     * Action triggered when you press a Cup on the screen
     * @param id the ID of the pressed cup
     */
    public void pressCup(int id) {
        if(isFirstTurn){
            firstTurnPlay(id);
            return;
        }
        PocketCup pressedPocketCup = (PocketCup) board.getCups()[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        putMarblesInNextCups(id, marblesFromEmptiedCup);

        int finalButtonID = id + marblesFromEmptiedCup;

        if(finalButtonID > 15){
            finalButtonID -= 15;
        }

        if(!isGameFinished()) {
            if(giveAnotherTurn(finalButtonID)){
                switchTurn();
            }
            switchTurn();
        }
    }

    /**
     * Checks if the last marble landed on the current player's playerCup
     * @param buttonID
     */
    protected boolean giveAnotherTurn(int buttonID){
        if(player1.getTurn() && buttonID == 7 && areThereValidMoves(player1)){
            return true;
        }
        if(player2.getTurn() && buttonID == 15 && areThereValidMoves(player2)){
            return true;
        }
        return false;
    }

    /**
     * PressCup method only for the first turn of the game.
     * after both players make their moves, the one that clicked first is first to go
     * @param id the id of the pressed cup
     */
    public void firstTurnPlay(int id) {
        if (id < 7) {
            if (!secondHasPlayed) {
                player2.setTurn(true);
            }
            firstID = id;
            firstHasPlayed = true;
        } else {
            if (!firstHasPlayed) {
                player1.setTurn(true);
            }
            secondID = id;
            secondHasPlayed = true;
        }
        if (firstHasPlayed && secondHasPlayed) {
            isFirstTurn = false;
            applyFirstTurnChanges(firstID, secondID);
        }
    }

    /**
     * This method loops through the current player's half and determine if you can make a move
     * If there is no valid move, it switches the player to make a move
    */
    protected boolean areThereValidMoves(Player player) {
        int cupIndex = 0;

        if(player.equals(player2)) {
            cupIndex += 8;
        }

        for (int i = cupIndex; i < 7 + cupIndex; i++) {
            // break if there is a valid move
            if (board.getCups()[i].getMarbles() > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * This forces the switch of turns.It is called when one player doesn't have valid moves
     */
    protected void switchTurn(){
        if(player1.getTurn() && areThereValidMoves(player2)){
            player2.setTurn(true);
            player1.setTurn(false);
        } else if (player2.getTurn() && areThereValidMoves(player1)) {
            player1.setTurn(true);
            player2.setTurn(false);
        }
    }

    /**
     * @param idCurrentCup
     * @param marblesFromEmptiedCup
     */
    protected void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
        int cupNumber = idCurrentCup + 1;
        for (int i = 0; i < marblesFromEmptiedCup; i++) {
            if (cupNumber == 7 && player2.getTurn()) {
                cupNumber++;
            } else if (cupNumber == 15 && player1.getTurn()) {
                cupNumber = 0;
            }
            Cup nextPocketCup = board.getCups()[cupNumber];
            //check at the last iteration if cup is empty
            if ((i == marblesFromEmptiedCup - 1) && nextPocketCup.isEmpty() && cupNumber != 7 && cupNumber != 15) {
                stealOppositeCup(cupNumber);
            }
            nextPocketCup.addMarbles(1);
            cupNumber++;
            //stay in the range of 16 cups.
            if (cupNumber > 15) {
                cupNumber = 0;
            }
        }
    }

    protected void stealOppositeCup(int cupNumber) {
        PocketCup nextPocketCup = (PocketCup) board.getCups()[cupNumber];
        PocketCup oppositeCup;
        if (player1.getTurn() && cupNumber < 7) {
            oppositeCup = (PocketCup) board.getCups()[cupNumber + ((7 - cupNumber) * 2)];
            int oppositeCupNumbers = oppositeCup.emptyCup();
            //take last marble from the cup alongside the opposite cup's one.
            nextPocketCup.addMarbles(-1);
            board.getPlayerCup1().addMarbles(oppositeCupNumbers + 1);
        } else if (player2.getTurn() && cupNumber > 7 && cupNumber < 15) {
            oppositeCup = (PocketCup) board.getCups()[(14 - cupNumber)];
            int oppositeCupNumbers = oppositeCup.emptyCup();
            //take last marble from the cup alongside the opposite cup's one.
            nextPocketCup.addMarbles(-1);
            board.getPlayerCup2().addMarbles(oppositeCupNumbers + 1);
        }
    }

    /**
     *  This is called only in the first turn to update the board after both players made their moves
     * @param firstID
     * @param secondID
     */
    private void applyFirstTurnChanges(int firstID, int secondID){
        int firstCupMarbles = ((PocketCup)board.getCups()[firstID]).emptyCup();
        int secondCupMarbles = ((PocketCup)board.getCups()[secondID]).emptyCup();
        for (int i = 1; i < firstCupMarbles + 1; i++){
            int nextCupID = i + firstID;
            board.getCups()[nextCupID].addMarbles(1);
        }
        for(int i = 1; i < secondCupMarbles + 1; i++){
            int nextCupID = i + secondID;
            if(nextCupID > 15){
                nextCupID -= 16;
            }
            board.getCups()[nextCupID].addMarbles(1);
        }
        switchTurn();
    }

    /**
     * Checks if the game has finished
     * @return boolean if the game is finished
     */
    public boolean isGameFinished() {
        return (!areThereValidMoves(player1) && !areThereValidMoves(player2));
    }

    /**
     * Checks which of the two players is the winner.
     * If the game is a draw, the returned player is Player 1 (the one that plays on the botoom part of the screen).
     * @see Player
     * @return the winner of the game.
    */
    public Player checkWinner() {
        if(board.getPlayerCup1Marbles() >= board.getPlayerCup2Marbles()) {
            return player1;
        }else {
            return player2;
        }
    }

    /**
     * Returns the names of both of the players and their score as an array. The order of the elements is: The winner's name,
     * his score, other player's name and finally the other player's name.
     * @return array with the 2 players and their respective scores
     */
    public String[] getFinalResults() {
        String winner ;
        int winnerScore ;
        String loser ;
        int loserScore ;

        if(checkWinner().equals(player1)) {
            winner = player1.getName();
            winnerScore = board.getPlayerCup1Marbles();
            loser = player2.getName();
            loserScore = board.getPlayerCup2Marbles();
        } else {
            winner = player2.getName();
            winnerScore  = board.getPlayerCup2Marbles();
            loser = player1.getName();
            loserScore = board.getPlayerCup1Marbles();
        }

        String[] results = {winner, Integer.toString(winnerScore), loser, Integer.toString(loserScore)};
        return results;
    }

    /**
     * Checks if the game ended as a draw
     * @return boolean representing whether or not the ended game had no winner
     */
    public boolean isDraw(){
        return isDraw;
    }

    /**
     * Gets the board object of the class, representing the current state of the game board
     * @return board object of the game
     * @see Board
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Get the player who is playing on the bottom half of the board
     * @return the playing playing on the bottom side
     * @see Player
     */
    public Player getPlayer1(){
        return player1;
    }

    /**
     * Get the player who is playing on the top half of the board
     * @return the playing playing on the top side
     * @see Player
     */
    public Player getPlayer2(){
        return player2;
    }

    /**
     * Get the Cups on the board
     * @return array of the cups on the board
     * @see Cup
     * @see Board
     */
    public Cup[] getBoardCups(){
        return board.getCups();
    }

    /**
     * sets the first turn to be true or false
     * @param b that sets if it is a first turn or not
     */
    public void setIsFirstTurn(boolean b) {
        this.isFirstTurn = b;
    }

    /**
     * Gets if the game is in its first turn or not
     * @return whether or not it is the first turn of the game
     */
    public boolean isFirstTurn(){
        return isFirstTurn;
    }

    /**
     * Gets the id of the first cup that got interacted on the board
     * @return the id of the first cup that was played
     */
    public int getFirstMoveID(){
        return firstID;
    }

}