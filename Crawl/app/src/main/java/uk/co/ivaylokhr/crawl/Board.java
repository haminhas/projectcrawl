package uk.co.ivaylokhr.crawl;

import android.util.Log;
import android.view.View;


public class Board {
    private Cup[] cups;
    private Player player1, player2;
    private boolean isFinished;
    private String winner;


    public Board(Cup[] cups){
        this.cups = cups;
        player1 = new Player((PlayerCup) cups[7]);
        player2 = new Player((PlayerCup) cups[15]);
        isFinished = false;
        player1.setTurn(true);
        decideTurn(0);

        for(Cup c :cups){
            c.setText(Integer.toString(c.getMarbles()));
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressCup(v);
                }
            });
        }
    }

    public boolean isFinished(){
        return isFinished;
    }


    public void pressCup(View view) {
        //get id of the pressed cup
        int id = ((PocketCup)view).getId();

        //empty cup
        PocketCup pressedPocketCup = (PocketCup)cups[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        //at this point please check if the cup in the array still has the marbles
        Log.i("Pressed Cup", "New move");
        putMarblesInNextCups(id, marblesFromEmptiedCup);

        if(isGameFinished()) {
            winner = checkWinner();
        }
        int finalButtonID = id + marblesFromEmptiedCup;
        if(finalButtonID > 15){
            finalButtonID -= 15;
        }

        //first it checks is one of the players
        decideTurn(finalButtonID);
        checkIfPlayerCanPlay();

        updateButtonText();
    }

    private void updateButtonText(){
        for(Cup c :cups){
            c.setText(Integer.toString(c.getMarbles()));
        }
    }

    //This method loops through the current player's half and determine if you can make a move
    //If there is no valid move, it switches the player to make a move
    private void checkIfPlayerCanPlay(){
        if(player1.getTurn()){
            //loops through the half ot player 1
            for (int i = 0; i < 7; i++){
                // break if there is a valid move
                if( !((PocketCup) cups[i]).isEmpty()){
                    break;
                }
                // at the end of the loop force the switch
                if(i == 6){
                    forceSwitch();
                }
            }
        }
        else if(player2.getTurn()){
            //loops through the half ot player 2
            for (int i = 8; i < 15; i++){
                //break if there is a valid move
                if( !((PocketCup) cups[i]).isEmpty()){
                    break;
                }
                // at the end of the loop force the switch
                if(i == 14){
                    forceSwitch();
                }
            }
        }
    }

    //This forces the switch of turns. It is only called when you one player doesn't have valid moves
    private void forceSwitch(){
        if(player1.getTurn()){
            switchTurns(player2);
        }
        else{
            switchTurns(player1);
        }
    }

    //decides which turn is next depending on the id of the final marblees that has been put
    private void decideTurn(int finalButtonID){
        if(player1.getTurn() && finalButtonID != 7){
            switchTurns(player2);
        }
        //if it landed on the player1 cup and is his turn
        //we need this to make the button disabled
        else if(player1.getTurn()){
            switchTurns(player1);
        }
        else if(player2.getTurn() && finalButtonID != 15){
            switchTurns(player1);
        }
        //if it landed on the player2 cup and is his turn
        //we need this to make the button disabled
        else if(player2.getTurn()){
            switchTurns(player2);
        }
    }

    //switches turn to the player who is given as a parameter
    private void switchTurns(Player player){
        if(player.equals(player2)){
            player2.setTurn(true);
            player1.setTurn(false);
        } else {
            player2.setTurn(false);
            player1.setTurn(true);
        }
        for (int i = 0; i < 15; i++){
            if(i < 7 && player1.getTurn() && cups[i].getMarbles() != 0){
                cups[i].setEnabled(true);
            }
            else if(i > 7 && player2.getTurn() && cups[i].getMarbles() != 0){
                cups[i].setEnabled(true);
            }
            else {
                cups[i].setEnabled(false);
            }
        }
    }
    
    public void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
        int cupNumber = idCurrentCup + 1;
        for(int i = 0; i < marblesFromEmptiedCup; i++) {
            Log.i("Cup id:", Integer.toString(cupNumber));
//            Log.i("Player's 1 turn:", Boolean.toString(player1.getTurn()));
//            Log.i("Player's 2 turn:", Boolean.toString(player2.getTurn()));
            //condition for when the cup is the playerCup
            if(cupNumber == 7) {
                if(player1.getTurn()) {
                    Log.i("PLAYER 1 TURN", Boolean.toString(player1.getTurn()));
                    //SHOULD I MODIFY THE player1.playerCup or the cup in the array??
                    player1.increaseScore(1);
                    //PlayerCup player1Cup = (PlayerCup)cups[i];
                    //player1Cup.addMarbles(1);
                    cupNumber++; //jumps PlayerCup and goes to next one
                    continue; //finish current iteration on this point and to go to next iteration
                }
                else {
                    cupNumber = 8;
                }
            }
            else if(cupNumber == 15) {
                if(player2.getTurn()) {
                    //SHOULD I MODIFY THE player2.playerCup or the cup in the array??
                    player2.increaseScore(1);
                    Log.i("player 2 score:", Integer.toString(player2.getScore()));
                    //PlayerCup player2Cup = (PlayerCup)cups[i];
                    //player2Cup.addMarbles(1);
                    cupNumber =0;
                    continue; //finish current iteration on this point and to go to next iteration
                }
                else{
                    cupNumber = 0;
                }
            }

            PocketCup nextPocketCup = (PocketCup) cups[cupNumber];

            //check at the last iteration if cup is empty
            if(i == marblesFromEmptiedCup - 1 && nextPocketCup.isEmpty() ) {
                PocketCup oppositeCup;
                if(player1.getTurn() && cupNumber < 7) {
                    oppositeCup = (PocketCup) cups[cupNumber + ((7 - cupNumber) * 2)];
                    //..or [(cupNumber -14)*-1]
                    if(!oppositeCup.isEmpty()){
                        int oppositeCupNumbers = oppositeCup.emptyCup();
                        //have to do it so it adds 1 more to the playerCup, sry :S
                        nextPocketCup.addMarbles(-1);
                        player1.increaseScore(oppositeCupNumbers + 1);
                    }
                }
                else if(player2.getTurn() && cupNumber > 7) {
                    oppositeCup = (PocketCup) cups[(14-cupNumber)];
                    if(!oppositeCup.isEmpty()){
                        int oppositeCupNumbers = oppositeCup.emptyCup();
                        //have to do it so it adds 1 more to the playerCup, sry :S
                        nextPocketCup.addMarbles(-1);
                        player2.increaseScore(oppositeCupNumbers + 1);
                    }
                }
            }

            nextPocketCup.addMarbles(1);

            //update id for the next cup, and stay in the range of 15.
            if(cupNumber>=15) {
                cupNumber = 0;
            }
            else{
                cupNumber++;
            }


        }//END OF FOR LOOP
    }


    public boolean isGameFinished() {
        //check if game is finished
        for(int i=0; i<cups.length; i++) {
            if(i != 7 && i != 15) {
                PocketCup pocketCup = (PocketCup) cups[i];
                if (!pocketCup.isEmpty()) {
                    break;
                }
            }

            if(i==14) {
                return true;
            }

        }

        if(player1.getScore() > 49 || player2.getScore() > 49) {
            return true;
        }

        return false;
    }


    public String checkWinner() {
        if(player1.getScore() > player2.getScore()) {
            return player1.getName();
        }
        return player2.getName();
    }

}
