package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends AppCompatActivity {
    private Cup[] cups;
    private Player player1, ai;
    private boolean isFinished;
    private boolean isFirstTurn;
    private String winner;
    Context context;

    public AIPlayer(Cup[] cups) {
        this.cups = cups;
        player1 = new Player((PlayerCup) cups[7]);
        ai = new Player((PlayerCup) cups[15]);
        isFinished = false;
        isFirstTurn = true;

        for (Cup c : cups) {
            c.setText(Integer.toString(c.getMarbles()));
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressCup(v);
                }
            });
        }

        for (int i = 8; i<15;i++){
            cups[i].setEnabled(false);
        }
    }

    private boolean Opposite(){
        for(int i = 8; i < 14;i++){
            int cupNumber = i;
            int marblesFromEmptiedCup = cups[i].getMarbles();
            for (int j = 0; j < marblesFromEmptiedCup; j++) {
                PocketCup nextPocketCup = (PocketCup) cups[cupNumber];
                //check at the last iteration if cup is empty
                if (j == marblesFromEmptiedCup && nextPocketCup.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean extraTurn(){
        for(int i = 8; i < 15;i++){
            if(cups[i].getMarbles() +cups[i].getId() == 15){
                return true;
            }
        }
        return false;
    }


    public void doMove(){
        ArrayList<PocketCup> temp = new ArrayList<>();
        //Assuming ai player is at the top
        for (int i = 8; i < 15; i++) {
            if (!((PocketCup) cups[i]).isEmpty()) {
                temp.add((PocketCup)cups[i]);
            }
        }

        if (extraTurn()){
            for(int i = 8; i < 15;i++)
                if (cups[i].getMarbles() + cups[i].getId() == 15) {
                    Log.i("Cup", Integer.toString(cups[i].getId()));
                    Log.i("marbles", Integer.toString(cups[i].getMarbles()));
                    putMarblesInNextCups(cups[i].getId(), ((PocketCup)cups[i]).emptyCup());
                    i = 8;

                }
            doMove();
            switchTurns(ai);
        }

        if (!Opposite()) {
            Random rand = new Random();
            int randCup = rand.nextInt(temp.size());
            PocketCup chosenCup = temp.get(randCup);
            int id = chosenCup.getId();
            int marblesFromEmptiedCup = chosenCup.emptyCup();
            putMarblesInNextCups(id, marblesFromEmptiedCup);
        } else if (Opposite()) {
            for(int i = 8; i < 14;i++){
                int cupNumber = i;
                int marblesFromEmptiedCup = cups[i].getMarbles();
                for (int j = 0; j < marblesFromEmptiedCup; j++) {
                    PocketCup nextPocketCup = (PocketCup) cups[cupNumber];

                    //check at the last iteration if cup is empty
                    if (j == marblesFromEmptiedCup && nextPocketCup.isEmpty()) {
                        PocketCup oppositeCup;
                        if (cupNumber > 7) {
                            oppositeCup = (PocketCup) cups[(14 - cupNumber)];
                            int oppositeCupNumbers = oppositeCup.emptyCup();
                            nextPocketCup.addMarbles(-1);
                            cups[15].addMarbles(oppositeCupNumbers + 1);
                        }
                    }
                }
            }
        }

        forceSwitch();

    }

    //adds Game.java content to the board class
    public void addContent(Context context){
        context = this.context;
    }

    public void pressCup(View view) {
        int finalButtonID =0;
        if (player1.getTurn()) {
            //get id of the pressed cup
            int id = ((PocketCup) view).getId();
            //empty cup
            PocketCup pressedPocketCup = (PocketCup) cups[id];
            int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
            //at this point please check if the cup in the array still has the marbles
            putMarblesInNextCups(id, marblesFromEmptiedCup);

            finalButtonID = id + marblesFromEmptiedCup;
            if (finalButtonID > 15) {
                finalButtonID -= 15;
            }
            decideTurn(finalButtonID);

        }
        updateButtonText();

        if (ai.getTurn()) {
            doMove();
        }

    if (isGameFinished()) {
        winner = checkWinner().getName();
    }
    //decides which turn is next by the id of the last modified cup
    //checks if it is the first turn
    //if it is, it gives the player who made the turn first to be the first player
    checkIfPlayerCanPlay();
    updateButtonText();
    }


    private void updateButtonText() {
        for (Cup c : cups) {
            c.setText(Integer.toString(c.getMarbles()));
        }
    }

    //This method loops through the current player's half and determine if you can make a move
    //If there is no valid move, it switches the player to make a move
    private void checkIfPlayerCanPlay() {
        if (player1.getTurn()) {
            //loops through the half ot player 1
            for (int i = 0; i < 7; i++) {
                // break if there is a valid move
                if (!((PocketCup) cups[i]).isEmpty()) {
                    break;
                }
                // at the end of the loop force the switch
                if (i == 6) {
                    forceSwitch();
                }
            }
        } else if (ai.getTurn()) {
            //loops through the half ot player 2
            for (int i = 8; i < 15; i++) {
                //break if there is a valid move
                if (!((PocketCup) cups[i]).isEmpty()) {
                    break;
                }
                // at the end of the loop force the switch
                if (i == 14) {
                    forceSwitch();
                }
            }
        }
    }

    //This forces the switch of turns.It is called when one player doesn't have valid moves
    private void forceSwitch(){
        if(player1.getTurn()){
            switchTurns(ai);
        } else {
            switchTurns(player1);
        }
    }

    //decides which turn is next depending on the id of the final marblees that has been put
    private void decideTurn(int finalButtonID) {
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
        //if it landed on the player2 cup and is his turn
        //we need this to make the button disabled
        else if (ai.getTurn()) {
            switchTurns(ai);
        }
    }

    //switches turn to the player who is given as a parameter
    private void switchTurns(Player player) {
        if (player.equals(ai)) {
            ai.setTurn(true);
            player1.setTurn(false);
        } else {
            ai.setTurn(false);
            player1.setTurn(true);
        }
        for (int i = 0; i < 15; i++) {
            if (i < 7 && player1.getTurn() && cups[i].getMarbles() != 0) {
                cups[i].setEnabled(true);
            } else if (i > 7 && ai.getTurn() && cups[i].getMarbles() != 0) {
                cups[i].setEnabled(false);
            } else if (cups[i].getMarbles() == 0){
                cups[i].setEnabled(false);
            }
        }
    }

    public void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
        int cupNumber = idCurrentCup + 1;
        for (int i = 0; i < marblesFromEmptiedCup; i++) {
            //condition for when the cup is the playerCup
            if (cupNumber == 7) {
                if (player1.getTurn()) {
                    player1.increaseScore(1);
                    cupNumber++; //jumps PlayerCup and goes to next one
                    continue; //finish current iteration on this point and to go to next iteration
                } else {
                    cupNumber = 8;
                }
            } else if (cupNumber == 15) {
                if (ai.getTurn()) {
                    ai.increaseScore(1);
                    cupNumber = 0;
                    continue; //finish current iteration on this point and to go to next iteration
                } else {
                    cupNumber = 0;
                }
            }

            PocketCup nextPocketCup = (PocketCup) cups[cupNumber];

            //check at the last iteration if cup is empty
            if (i == marblesFromEmptiedCup - 1 && nextPocketCup.isEmpty()) {
                PocketCup oppositeCup;
                if (player1.getTurn() && cupNumber < 7) {
                    oppositeCup = (PocketCup) cups[cupNumber + ((7 - cupNumber) * 2)];
                    int oppositeCupNumbers = oppositeCup.emptyCup();
                    nextPocketCup.addMarbles(-1);
                    player1.increaseScore(oppositeCupNumbers + 1);
                }
            }

            nextPocketCup.addMarbles(1);

            //update id for the next cup, and stay in the range of 15.
            if (cupNumber >= 15) {
                cupNumber = 0;
            } else {
                cupNumber++;
            }


        }//END OF FOR LOOP
    }

    public boolean isGameFinished() {
        //check if game is finished
        for (int i = 0; i < cups.length; i++) {
            if (i != 7 && i != 15) {
                PocketCup pocketCup = (PocketCup) cups[i];
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


    public Player checkWinner() {
        //checks who won
        if(player1.getScore() > ai.getScore()) {
            return player1;
        }
        return ai;
    }

}

