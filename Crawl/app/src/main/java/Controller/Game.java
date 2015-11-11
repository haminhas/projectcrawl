package Controller;


import Model.Board;
import Model.Cup;
import Model.Player;
import Model.PocketCup;

public class Game {
    private Player player1, player2;
    private Board board;
    //first turn stuff
    private Player firstPlayer;
    private int firstID, secondID;
    private boolean firstHasPlayed, secondHasPlayed;
    private boolean isFirstTurn;

    public Game() {
        player1 = new Player();
        player2 = new Player();
        board = new Board();
        initialiseVariablesFirstTurn();
    }

    /**
     * Initialise values of the variables needed for first Turn
     */
    private void initialiseVariablesFirstTurn(){
        player1.setTurn(false);
        player2.setTurn(false);
        //The player who will have the first play
        firstPlayer = null;
        firstID = -1; secondID = -1;
        firstHasPlayed = false; secondHasPlayed = false;
        isFirstTurn = true;
    }

    /**
     * Action triggered when you press a Cup on the screen
     * @param id
     * @return
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
            checkForAnotherTurn(finalButtonID);
            forceSwitch();
        }
    }

    private void checkForAnotherTurn(int another){
        if(player1.getTurn() && another == 7){
            forceSwitch();
        }
        if(player2.getTurn() && another == 15){
            forceSwitch();
        }
    }

    /**
     * the logic behind the first turn, where the players do the turn together
     * after both players make their moves, the one that clicked first is first to go
     */
    private void firstTurnPlay(int id) {
        if (id < 7) {
            if (!secondHasPlayed) {
                firstPlayer = player1;
                player2.setTurn(true);
            }
            firstID = id;
            firstHasPlayed = true;
        } else {
            if (!firstHasPlayed) {
                firstPlayer = player2;
                player1.setTurn(true);
            }
            secondID = id;
            secondHasPlayed = true;
        }
        if (firstHasPlayed && secondHasPlayed) {
            isFirstTurn = false;
            switchTurns(firstPlayer);

            applyFirstTurnChanges(firstID, secondID);
        }
    }

    /**
     * switches turn to the player who is given as a parameter
     */
    private void switchTurns(Player player) {
        if (player.equals(player2)) {
            player1.setTurn(false);
            player2.setTurn(true);
        } else if (player.equals(player1)){
            player1.setTurn(true);
            player2.setTurn(false);
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
        } else if (player2.getTurn()) {
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
            switchTurns(player2);
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
            switchTurns(player2);
        }
        //if it landed on the player1 cup and is his turn
        //we need this to make the button disabled
        else if (player1.getTurn()) {
            switchTurns(player1);
        } else if (player2.getTurn() && finalButtonID != 15) {
            switchTurns(player1);
        }
        //if it landed on the player2 cup and is his turn
        //we need this to make the button disabled
        else if (player2.getTurn()) {
            switchTurns(player2);
        }
        if(player1.getTurn()){
            playerOneTurn = true;
        }
        return playerOneTurn;
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
            results[2] = player2.getName();
            results[3] = String.valueOf(board.getPlayerCup2Marbles());
        }else if(board.getPlayerCup1Marbles() < board.getPlayerCup2Marbles()) {
            results[2] = player1.getName();
            results[3] = String.valueOf(board.getPlayerCup1Marbles());
            results[0] = player2.getName();
            results[1] = String.valueOf(board.getPlayerCup2Marbles());
        } else{
            results[0] = player1.getName();
            results[2] = player2.getName();
        }
        return results;
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

}
