package uk.co.ivaylokhr.crawl;

import android.util.Log;
import android.view.View;


public class Board {
    private Cup[] cups;
    private Player player1, player2;
    private boolean isFinished;
    private boolean takeOpponentMarbles;
    private String winner;


    public Board(Cup[] cups){
        this.cups = cups;
        player1 = new Player((PlayerCup) cups[7]);
        player2 = new Player((PlayerCup) cups[15]);
        isFinished = false;
        takeOpponentMarbles = false;
        player1.setTurn(true);
        player2.setTurn(false);

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

        //put default value of the boolean which cheks if you had taken opponent marbles
        takeOpponentMarbles = false;

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

        //checks if you took the opposit marbles. If not, it decides which turn is it
        //If yes, it forces the turn switch
        if(!takeOpponentMarbles && marblesFromEmptiedCup < 7){
            decideTurn(finalButtonID);
        }
        else {
            forceSwitch();
        }

        updateButtonText();
    }

    private void updateButtonText(){
        for(Cup c :cups){
            c.setText(Integer.toString(c.getMarbles()));
        }
    }

    //This forces the switch of turns. It is only called when you took opposite marbles
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
        if(player1.getTurn() && finalButtonID <= 7){
            switchTurns(player1);
        }
        else if(player1.getTurn() && finalButtonID > 7) {
            switchTurns(player2);
        }
        else if(player2.getTurn() && finalButtonID >= 7){
            switchTurns(player2);
        }
        else if(player2.getTurn() && finalButtonID < 7){
            switchTurns(player1);
        }
    }

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
                    //SHOULD I MODIFY THE player1.playerCup or the cup in the array??
                    player1.increaseScore(1);
                    //PlayerCup player1Cup = (PlayerCup)cups[i];
                    //player1Cup.addMarbles(1);
                    cupNumber++; //jumps PlayerCup and goes to next one
                }
                continue; //finish current iteration on this point and to go to next iteration
            }
            else if(cupNumber == 15) {
                if(player2.getTurn()) {
                    //SHOULD I MODIFY THE player2.playerCup or the cup in the array??
                    player2.increaseScore(1);
                    Log.i("player 2 score:", Integer.toString(player2.getScore()));
                    //PlayerCup player2Cup = (PlayerCup)cups[i];
                    //player2Cup.addMarbles(1);
                    cupNumber =0;
                }
                continue; //finish current iteration on this point and to go to next iteration
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
                        takeOpponentMarbles = true;
                    }
                }
                else if(player2.getTurn() && cupNumber > 7) {
                    oppositeCup = (PocketCup) cups[(14-cupNumber)];
                    if(!oppositeCup.isEmpty()){
                        int oppositeCupNumbers = oppositeCup.emptyCup();
                        //have to do it so it adds 1 more to the playerCup, sry :S
                        nextPocketCup.addMarbles(-1);
                        player2.increaseScore(oppositeCupNumbers + 1);
                        takeOpponentMarbles = true;
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
