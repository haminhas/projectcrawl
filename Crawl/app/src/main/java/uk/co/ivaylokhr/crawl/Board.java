package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class Board extends AppCompatActivity {
    private Cup[] cups;
    private Player player1, player2, player3;
    private boolean isFinished;
    private boolean isFirstTurn;
    private String winner;
    private Long timer;
    private TextView playerone;
    private TextView playertwo;
    private TextView turn;
    private Game activity;

    public Board(Game activity) {
        this.activity = activity;
        cups = activity.getCups();
        player1 = new Player((PlayerCup) cups[7]);
        player2 = new Player((PlayerCup) cups[15]);
        player3 = new Player((PlayerCup) cups[15]);
        enableAllButtons();
//        player1.setTurn(false);
//        player2.setTurn(false);
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
    }

    //this method is called only at the start of the game, when both players can make a move
    private void enableAllButtons() {
        for (int i = 0; i < cups.length; i++) {
            //ignore the player cups
            if (i == 7 || i == 15) {
                continue;
            } else {
                cups[i].setEnabled(true);
            }
        }
    }

    //This method edits the highscores after the game
    public void updateScores() {
        Integer score = checkWinner().playerCup.getMarbles();
        Integer one = Prefrences.fromPreferences(activity.getBaseContext(), -1, "first", "your_prefs");
        Integer two = Prefrences.fromPreferences(activity.getBaseContext(), -1, "second", "your_prefs");
        Integer three = Prefrences.fromPreferences(activity.getBaseContext(), -1, "third", "your_prefs");
        activity.setTime();
        if (score > one) {
            three = two;
            two = one;
            one = score;
        } else if (score > two) {
            three = two;
            two = score;
        } else if (score > three) {
            three = score;
        }
        Prefrences.toPreferences(activity.getBaseContext(), one, "first", "your_prefs");
        Prefrences.toPreferences(activity.getBaseContext(), two, "second", "your_prefs");
        Prefrences.toPreferences(activity.getBaseContext(), three, "third", "your_prefs");
    }

    public void addTimer(long time){
        timer = time;
    }
    public void pressCup(View view) {

        //get id of the pressed cup
        int id = ((PocketCup)view).getId();
        //empty cup
        PocketCup pressedPocketCup = (PocketCup) cups[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        //at this point please check if the cup in the array still has the marbles
        Log.i("Pressed Cup", "New move");
        putMarblesInNextCups(id, marblesFromEmptiedCup);
        String turnText = "";
        if(player1.getTurn()) {
            turnText = (String) playerone.getText();
        }else{
            turnText = (String) playertwo.getText();
        }
        turn.setText(turnText+"'s turn");
        turn.setTextColor(Color.GREEN);
        if(isGameFinished()) {
            updateScores();
            winner = checkWinner().getName();
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
        //checks if it is the first turn
        //if it is, it gives the player who made the turn first to be the first player
        if(isFirstTurn){
            doFirstTurn(id);
        }
        checkIfPlayerCanPlay();
        updateButtonText();
        if(player1.getTurn()){
            playerone.setTextColor(Color.BLACK);
            playertwo.setTextColor(Color.GREEN);
        }else{
            playertwo.setTextColor(Color.BLACK);
            playerone.setTextColor(Color.GREEN);
        }
    }

    private void updateButtonText() {
        int[] backgrounds = {R.drawable.pocketbackground, R.drawable.back1, R.drawable.back2, R.drawable.back3,
            R.drawable.back4, R.drawable.back5, R.drawable.back6, R.drawable.back7, R.drawable.back8};
        for (int i = 0; i < cups.length; i++) {
            Cup c = cups[i];
            int marbles = c.getMarbles();
            c.setText(Integer.toString(marbles));
            if(i == 7 || i == 15) {
                continue;
            }
            else{
                if(marbles <= 7) {
                    c.setBackgroundResource(backgrounds[marbles]);
                }
                else{
                    c.setBackgroundResource(backgrounds[8]);
                }
            }
        }
    }
    public void addNames(TextView player1, TextView player2){
        playerone = player1;
        playertwo = player2;
        this.player1.setName((String) player1.getText());
        this.player2.setName((String) player2.getText());
        turn = (TextView) activity.findViewById(R.id.turn);
        turn.setText("Turn 1");
        turn.setTextColor(Color.GREEN);
    }

    private void doFirstTurn(int pressedID){
        if(pressedID < 7){
            player1.setTurn(true);
            player2.setTurn(false);
        }
        else{
            player2.setTurn(true);
            player1.setTurn(false);
        }
        for (int i = 0; i < 15; i++) {
            if (i < 7 && player1.getTurn() && cups[i].getMarbles() != 0) {
                cups[i].setEnabled(true);
            } else if (i > 7 && player2.getTurn() && cups[i].getMarbles() != 0) {
                cups[i].setEnabled(true);
            } else {
                cups[i].setEnabled(false);
            }
        }
        isFirstTurn = false;
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
        } else if (player2.getTurn()) {
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
            switchTurns(player2);
        } else {
            switchTurns(player1);
        }
    }

    //decides which turn is next depending on the id of the final marblees that has been put
    private void decideTurn(int finalButtonID) {
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
    }

    //switches turn to the player who is given as a parameter
    private void switchTurns(Player player) {
        if (player.equals(player2)) {
            player2.setTurn(true);
            player1.setTurn(false);
        } else {
            player2.setTurn(false);
            player1.setTurn(true);
        }
        for (int i = 0; i < 15; i++) {
            if (i < 7 && player1.getTurn() && cups[i].getMarbles() != 0) {
                cups[i].setEnabled(true);
            } else if (i > 7 && player2.getTurn() && cups[i].getMarbles() != 0) {
                cups[i].setEnabled(true);
            } else {
                cups[i].setEnabled(false);
            }
        }
    }

    public void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
        int cupNumber = idCurrentCup + 1;
        for (int i = 0; i < marblesFromEmptiedCup; i++) {
            Log.i("Cup id:", Integer.toString(cupNumber));
//            Log.i("Player's 1 turn:", Boolean.toString(player1.getTurn()));
//            Log.i("Player's 2 turn:", Boolean.toString(player2.getTurn()));
            //condition for when the cup is the playerCup
            if (cupNumber == 7) {
                if (player1.getTurn()) {
                    //SHOULD I MODIFY THE player1.playerCup or the cup in the array??
                    player1.increaseScore(1);
                    //PlayerCup player1Cup = (PlayerCup)cups[i];
                    //player1Cup.addMarbles(1);
                    cupNumber++; //jumps PlayerCup and goes to next one
                    continue; //finish current iteration on this point and to go to next iteration
                } else {
                    cupNumber = 8;
                }
            } else if (cupNumber == 15) {
                if (player2.getTurn()) {
                    //SHOULD I MODIFY THE player2.playerCup or the cup in the array??
                    player2.increaseScore(1);
                    //PlayerCup player2Cup = (PlayerCup)cups[i];
                    //player2Cup.addMarbles(1);
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
                    //..or [(cupNumber -14)*-1]
                    int oppositeCupNumbers = oppositeCup.emptyCup();
                    //have to do it so it adds 1 more to the playerCup, sry :S
                    nextPocketCup.addMarbles(-1);
                    player1.increaseScore(oppositeCupNumbers + 1);
                }
                else if(player2.getTurn() && cupNumber > 7) {
                    oppositeCup = (PocketCup) cups[(14-cupNumber)];
                    int oppositeCupNumbers = oppositeCup.emptyCup();
                    //have to do it so it adds 1 more to the playerCup, sry :S
                    nextPocketCup.addMarbles(-1);
                    player2.increaseScore(oppositeCupNumbers + 1);
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
        if(player1.getScore() > player2.getScore()) {
            return player1;
        }else if(player1.getScore() < player2.getScore()) {
            return player2;
        } else{
            return player3;
        }
    }

}
