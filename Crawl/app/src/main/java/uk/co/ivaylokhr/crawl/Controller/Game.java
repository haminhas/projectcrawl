package uk.co.ivaylokhr.crawl.Controller;


import uk.co.ivaylokhr.crawl.Model.Board;
import uk.co.ivaylokhr.crawl.Model.Cup;
import uk.co.ivaylokhr.crawl.Model.Player;
import uk.co.ivaylokhr.crawl.Model.PocketCup;

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
     * @param id
     */
    public void pressCup(int id) {
        if(isFirstTurn){
            firstTurnPlay(id);
        }
        PocketCup pressedPocketCup = (PocketCup) board.getCups()[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        putMarblesInNextCups(id, marblesFromEmptiedCup);

        int finalButtonID = id + marblesFromEmptiedCup;

        if(finalButtonID > 15){
            finalButtonID -= 15;
        }

        if(!isGameFinished()) {
            if(!giveAnotherTurn(finalButtonID)) {
                switchTurn();
            }
        }
    }

    /**
     * Checks if the last marble landed on the current player's playerCup
     * @param buttonID
     */
    public boolean giveAnotherTurn(int buttonID){
        if(player1.getTurn() && buttonID == 7){
            return true;
        }
        if(player2.getTurn() && buttonID == 15){
            return true;
        }
        return false;
    }

    /**
     * the logic behind the first turn, where the players do the turn together
     * after both players make their moves, the one that clicked first is first to go
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
    public boolean areThereValidMoves(Player player) {
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
    public void switchTurn(){
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
            //condition for when the cup is a playerCup
            if (cupNumber == 7) {
                if (player1.getTurn()) {
                    board.getPlayerCup1().addMarbles(1);
                    cupNumber++;
                    continue;
                }
                cupNumber++;
            } else if (cupNumber == 15) {
                if (player2.getTurn()) {
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
                } else if (player2.getTurn() && cupNumber > 7 && cupNumber < 15) {
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
        switchTurn();
    }

    /**
     * checks if all the cups have been emptied
     * @return boolean if the game is finished
     */
    public boolean isGameFinished() {
        return (!areThereValidMoves(player1) && !areThereValidMoves(player2));
    }

    public Player checkWinner() {
        if(board.getPlayerCup1Marbles() >= board.getPlayerCup2Marbles()) {
            return player1;
        }else {
            return player2;
        }
    }

    /**
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

    public boolean isDraw(){
        return isDraw;
    }

    public Board getBoard(){
        return board;
    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }

    public Cup[] getBoardCups(){
        return board.getCups();
    }

    public boolean isFirstTurn(){
        return isFirstTurn;
    }

    public int getFirstMoveID(){
        return firstID;
    }

}