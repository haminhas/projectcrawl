package uk.co.ivaylokhr.crawl;

import android.util.Log;


public class Game {
    Player player1, player2;
    Board board;
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
    public boolean pressCup(int id) {
        if(isFirstTurn){
           return firstTurnPlay(id);
        }
        PocketCup pressedPocketCup = (PocketCup) board.getCups()[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        //at this point please check if the cup in the array still has the marbles
        Log.i("Pressed Cup", "New move");
        putMarblesInNextCups(id, marblesFromEmptiedCup);
        int finalButtonID = id + marblesFromEmptiedCup;
        if(finalButtonID > 15){
            finalButtonID -= 15;
        }
        //decides which turn is next by the id of the last modified cup
        return isPlayerOneTurn(finalButtonID);
    }


    /**
     * the logic behind the first turn, where the players do the turn together
     * after both players make their moves, the one that clicked first is first to go
     */
    private boolean firstTurnPlay(int id) {
        boolean playerOneEnabled = false;
        if (id < 7) {
            if (!secondHasPlayed) {
                firstPlayer = player1;
            }
            firstID = id;
            firstHasPlayed = true;
        } else {
            if (!firstHasPlayed) {
                firstPlayer = player2;
                playerOneEnabled = true;
            }
            secondID = id;
            secondHasPlayed = true;
            for (int i = 8; i < 16; i++) {
            }
        }
        if (firstHasPlayed && secondHasPlayed) {
            isFirstTurn = false;
            if(firstPlayer == player1){
                playerOneEnabled = true;
            }
            switchTurns(firstPlayer);
            applyFirstTurnChanges(firstID, secondID);
        }
        return playerOneEnabled;
    }

    /**
     * switches turn to the player who is given as a parameter
     */
    private void switchTurns(Player player) {
        if (player.equals(player2)) {
            player2.setTurn(true);
            player1.setTurn(false);
        } else {
            player2.setTurn(false);
            player1.setTurn(true);
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
    public void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
        int cupNumber = idCurrentCup + 1;
        for (int i = 0; i < marblesFromEmptiedCup; i++) {
            Log.i("Cup id:", Integer.toString(cupNumber));
            //condition for when the cup is the playerCup
            if (cupNumber == 7) {
                if (player1.getTurn()) {
                    board.playerCup1.addMarbles(1);
                    cupNumber++;
                    continue;
                } else {
                    cupNumber++;
                }
            } else if (cupNumber == 15) {
                if (player2.getTurn()) {
                    board.playerCup2.addMarbles(1);
                    cupNumber++;
                    continue;
                } else {
                    cupNumber = 0;
                }
            }
            PocketCup nextPocketCup = (PocketCup) board.cups[cupNumber];
            //check at the last iteration if cup is empty
            if (i == marblesFromEmptiedCup - 1 && nextPocketCup.isEmpty()) {
                PocketCup oppositeCup;
                if (player1.getTurn() && cupNumber < 7) {
                    oppositeCup = (PocketCup) board.cups[cupNumber + ((7 - cupNumber) * 2)];
                    int oppositeCupNumbers = oppositeCup.emptyCup();
                    //take last marble from the cup alongside the opposite cup's one.
                    nextPocketCup.addMarbles(-1);
                    board.playerCup1.addMarbles(oppositeCupNumbers + 1);
                } else if (player2.getTurn() && cupNumber > 7) {
                    oppositeCup = (PocketCup) board.cups[(14 - cupNumber)];
                    int oppositeCupNumbers = oppositeCup.emptyCup();
                    //take last marble from the cup alongside the opposite cup's one.
                    nextPocketCup.addMarbles(-1);
                    board.playerCup1.addMarbles(oppositeCupNumbers + 1);
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
        ((PocketCup)board.cups[firstID]).emptyCup();
        ((PocketCup)board.cups[secondID]).emptyCup();
        for (int i = 1; i < 8; i++){
            int nextCupID = i + firstID;
            board.cups[nextCupID].addMarbles(1);
        }
        for(int i = 1; i < 8; i++){
            int nextCupID = i + secondID;
            if(nextCupID > 15){
                nextCupID -= 16;
            }
            board.cups[nextCupID].addMarbles(1);
        }
    }

    public Board getBoard(){
        return board;
    }

}
