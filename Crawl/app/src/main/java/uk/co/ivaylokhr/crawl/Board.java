package uk.co.ivaylokhr.crawl;

import android.util.Log;
import android.view.View;

/**
 * Created by k1464377 on 09/11/15.
 */
public class Board {
    PlayerCup playerCup1, playerCup2;
    Cup[] cups;

    public Board() {
        playerCup1 = new PlayerCup();
        playerCup2 = new PlayerCup();
        cups = new Cup[16];
        fillCupsArray();
    }

    public void fillCupsArray(){
        for (int i = 0; i < cups.length; i++) {
            //ignore the player cups
            if (i == 7) {
                cups[i] = playerCup1;
            }else if(i == 15){
                cups[i] = playerCup2;
            }
            else {
                cups[i] = new PocketCup() ;
            }
        }
    }

    public void pressCup(int id) {
        //empty cup
        if(isFirstTurn){
            firstTurn(id);
            return;
        }
        PocketCup pressedPocketCup = (PocketCup) cups[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        //at this point please check if the cup in the array still has the marbles
        Log.i("Pressed Cup", "New move");
        putMarblesInNextCups(id, marblesFromEmptiedCup);
        if(isGameFinished()) {
            updateScores();
            if(checkWinner().equals(player1)) {
                activity.endGame(checkWinner(), player2);
            }else if(checkWinner().equals(player2)){
                activity.endGame(checkWinner(), player1);
            }else{
                activity.endGame(checkWinner(), player1);
            }
        }
        int finalButtonID = id + marblesFromEmptiedCup;
        if(finalButtonID > 15){
            finalButtonID -= 15;
        }
        //decides which turn is next by the id of the last modified cup
        decideTurn(finalButtonID);
        updateBoardView();
    }
}
