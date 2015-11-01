package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AIPlayer extends AppCompatActivity {
    private Cup[] cups;
    private boolean isFirstTurn;
    private String winner;
    Context context;
    private boolean opp;
    private AIGame activity;
    private Player player1, ai, player3;
    private TextView playerone;
    private TextView playertwo;
    private TextView turn;
    //first turn stuff
    private Player firstPlayer;
    private int firstID, secondID;
    private boolean firstHasPlayed, secondHasPlayed;

    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    public AIPlayer(AIGame activity) {
        this.cups = cups;
        this.activity = activity;
        cups = activity.getCups();
        player1 = new Player((PlayerCup) cups[7]);
        ai = new Player((PlayerCup) cups[15]);
        player3 = new Player((PlayerCup) cups[15]);
        enableAllButtons();
        initializeFirstTurn();
        initializeButtons();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                doMove();
                updateButtonText();

            }
        }, 5000);
    }


    //a separate method for initializing values of the variables neeeded for first Turn
    private void initializeFirstTurn(){
        player1.setTurn(false);
        ai.setTurn(false);
        firstPlayer = null;
        firstID = -1; secondID = -1;
        firstHasPlayed = false; secondHasPlayed = false;
        isFirstTurn = true;
    }

    //Initialize onClickListener and update the view of all the buttons on board
    private void initializeButtons(){
        for (Cup c : cups) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressCup(v);
                }
            });
        }
        updateButtonText();
    }

    //this method is called only at the start of the game, when both players can make a move
    private void enableAllButtons() {
        for (int i = 0; i < 7; i++) {
            cups[i].setEnabled(true);
        }
        for (int i = 8; i < 15; i++){
            cups[i].setEnabled(false);
        }
    }

    public void opposite(int cup,int marbles){
        updateButtonText();
        PocketCup nextPocketCup;
        int temp = cup;
        for (int j = 0; j < marbles; j++) {
            cup = temp + j;
            if (cup == 7) {
                cup = 8;
            }
            if (cup == 15) {
                int t = 15 - temp;
                marbles = marbles - t + 1;
                temp = 0;
                j = 0;
            } else {
                if (cup+1!=15 && cup+1!=7){
                    nextPocketCup = (PocketCup) cups[cup+1];
                    if (j == marbles - 1 && nextPocketCup.isEmpty() && cup>7) {
                        putMarblesInNextCups(cup,marbles);
                        opp = true;
                        PocketCup oppositeCup = (PocketCup) cups[(14 - (cup+1))];
                        int oppositeCupNumbers = oppositeCup.emptyCup();
                        nextPocketCup.addMarbles(-1);
                        cups[15].addMarbles(oppositeCupNumbers + 1);
                    }
                }
                //check at the last iteration if cup is empty
            }
        }
    }

    public void doMove(){
        playClickSound();
        opp = false;
        ArrayList<PocketCup> temp = new ArrayList<>();
        Boolean eturn =false;
        //Assuming ai player is at the top
        for (int i = 8; i < 15; i++) {
            if (((PocketCup) cups[i]).getMarbles() != 0) {
                temp.add((PocketCup)cups[i]);
            }
        }
        //If the AI has all empty moves then skip the turn

        if(isFirstTurn){
            Random rand = new Random();
            int randCup = rand.nextInt(temp.size());
            PocketCup chosenCup = temp.get(randCup);
            int id = chosenCup.getId();
            if (firstHasPlayed){
                int marbles = chosenCup.emptyCup() - 1;
                putMarblesInNextCups(id, marbles);
                ai.playerCup.addMarbles(1);
            }
            firstTurn(id);
            return;
        }

        if(temp.isEmpty()){
            return;
        }

        for(int i = 8; i < 15;i++) {
            if (cups[i].getMarbles() + cups[i].getId() == 15) {
                putMarblesInNextCups(cups[i].getId(), ((PocketCup) cups[i]).emptyCup());
                i = 8;
                eturn = true;
            }
        }

        if (eturn){
            doMove();
            //Return statement to prevent the rest of the method and giving AI another turn
            return;
        }

        for(int i = 8; i < 14;i++){
            opposite(i,cups[i].getMarbles());
        }

        if(!opp){
            Random rand = new Random();
            int randCup = rand.nextInt(temp.size());
            PocketCup chosenCup = temp.get(randCup);
            int id = chosenCup.getId();
            int marblesFromEmptiedCup = chosenCup.emptyCup();
            putMarblesInNextCups(id, marblesFromEmptiedCup);
        }

        forceSwitch();

    }

    //adds Game.java content to the board class
    public void addContent(Context context){
        context = this.context;
    }

    public void pressCup(View view) {
        playClickSound();
        //get id of the pressed cup
        int id = ((PocketCup)view).getId();
        //empty cup
        if(isFirstTurn){
            firstTurn(id);
            return;
        }
        if (player1.getTurn()) {
            PocketCup pressedPocketCup = (PocketCup) cups[id];
            int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
            //at this point please check if the cup in the array still has the marbles
            Log.i("Pressed Cup", "New move");
            putMarblesInNextCups(id, marblesFromEmptiedCup);
            int finalButtonID = id + marblesFromEmptiedCup;
            if (finalButtonID > 15) {
                finalButtonID -= 15;
            }
            decideTurn(finalButtonID);

        }
        updateButtonText();

        if (ai.getTurn()) {
            updateButtonText();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    updateButtonText();
                    doMove();
                    updateButtonText();

                }
            }, 5000);
            updateButtonText();

        }

        if(isGameFinished()) {
            updateScores();
            winner = checkWinner().getName();
            if(checkWinner().equals(player1)) {
                activity.endGame(checkWinner(), ai);
            }else if(checkWinner().equals(ai)){
                activity.endGame(checkWinner(), player1);
            }else{
                activity.endGame(checkWinner(), player1);
            }
        }
    //if the player cant do any moves then make the ai do a move
    while (checkIfPlayerCanPlay()){
        doMove();
    }

    updateButtonText();
    }

    //This method loops through the current player's half and determine if you can make a move
    //If there is no valid move, it switches the player to make a move
    private Boolean checkIfPlayerCanPlay() {
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
                    return true;
                }
            }
        }
        return false;
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
        for (int i = 0; i < 7; i++) {
            if (player1.getTurn() && cups[i].getMarbles() != 0) {
                cups[i].setEnabled(true);
            }
            if (cups[i].getMarbles() == 0 || ai.getTurn()) {
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
                    playZoomAnimation(cups[cupNumber], i);

                    continue; //finish current iteration on this point and to go to next iteration
                } else {
                    cupNumber = 8;
                }
            } else if (cupNumber == 15) {
                if (ai.getTurn()) {
                    ai.increaseScore(1);
                    cupNumber = 0;
                    playZoomAnimation(cups[cupNumber], i);

                    continue; //finish current iteration on this point and to go to next iteration
                } else {
                    cupNumber = 0;
                }
            }

            PocketCup nextPocketCup = (PocketCup) cups[cupNumber];
            playZoomAnimation(nextPocketCup, i);

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

    private void playZoomAnimation(View view, int index){
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.zoomanim);
        animation.setStartOffset(200*index);
        view.startAnimation(animation);
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
        else if(player1.getScore() < ai.getScore()) {
            return ai;
        } else{
            return player3;
        }
    }


    public void addNames(TextView player1, TextView player2){
        playerone = player1;
        playertwo = player2;
        this.player1.setName((String) player1.getText());
        this.ai.setName((String) player2.getText());
        turn = (TextView) activity.findViewById(R.id.turn);
        turn.setText("Turn 1");
        turn.setTextColor(Color.GREEN);
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

    //update Ivaylo's textview on the top of the screen, it might be removed if you feel like it
    private void updateTurnText(){
        String turnText = "";
        if(player1.getTurn()) {
            turnText = (String) playerone.getText();
        }else{
            turnText = (String) playertwo.getText();
        }
        turn.setText(turnText + "'s turn");
        turn.setTextColor(Color.GREEN);
    }

    //update the turn indicators Sola made
    private void updatePlayerTurnIndicators(){
        if(player1.getTurn()){
            playerone.setTextColor(Color.GREEN);
            playertwo.setTextColor(Color.BLACK);
        }else{
            playertwo.setTextColor(Color.GREEN);
            playerone.setTextColor(Color.BLACK);
        }
    }

    private void playClickSound(){
        MediaPlayer media = MediaPlayer.create(activity, R.raw.click);
        media.start();
    }

    //updates the information on the board
    private void updateBoardView(){
        updateTurnText();
        updatePlayerTurnIndicators();
        updateButtonText();
    }
    //This is called only in the first turn to update the board after both players made their moves
    private void applyFirstTurnChanges(int firstID){
        ((PocketCup)cups[firstID]).emptyCup();
        ((PocketCup)cups[secondID]).emptyCup();
        for (int i = 1; i < 8; i++){
            int nextCupID = (i + firstID) %16;
            cups[nextCupID].addMarbles(1);
        }
        updateBoardView();
    }
    //the logic behind the first turn, where the players do the turn together
    //after both players make their moves, the one that clicked first is first to go
    private void firstTurn(int id){
        if(id < 7){
            if(!secondHasPlayed){
                firstPlayer = player1;
            }
            firstID = id;
            firstHasPlayed = true;
            for (int i = 0; i < 7; i++){
                cups[i].setEnabled(false);
            }
        }
        else{
            if(!firstHasPlayed){
                firstPlayer = ai;
            }
            secondID = id;
            secondHasPlayed = true;
            for(int i = 8; i < cups.length; i++){
                cups[i].setEnabled(false);
            }
        }
        if(firstHasPlayed && secondHasPlayed){
            isFirstTurn = false;
            switchTurns(firstPlayer);
            if(firstPlayer.equals(player1)){
                applyFirstTurnChanges(firstID);
                if(secondID - firstID >= 9){
                    cups[firstID].addMarbles(1);
                    updateBoardView();
                }
            }else {
                putMarblesInNextCups(firstID, 6);
                ai.playerCup.addMarbles(1);
                applyFirstTurnChanges(secondID);
                if(firstID - secondID >= 9){
                    cups[secondID].addMarbles(1);
                    updateBoardView();
                }
            }

            if(firstPlayer.equals(ai)){

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        doMove();
                        updateButtonText();

                    }
                }, 5000);
            }
        }
    }
    //This method edits the highscores after the game
    public void updateScores() {
        Integer score = checkWinner().playerCup.getMarbles();
        Integer one = Preferences.fromPreferences(activity.getBaseContext(), -1, "first", "your_prefs");
        Integer two = Preferences.fromPreferences(activity.getBaseContext(), -1, "second", "your_prefs");
        Integer three = Preferences.fromPreferences(activity.getBaseContext(), -1, "third", "your_prefs");
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
        Preferences.toPreferences(activity.getBaseContext(), one, "first", "your_prefs");
        Preferences.toPreferences(activity.getBaseContext(), two, "second", "your_prefs");
        Preferences.toPreferences(activity.getBaseContext(), three, "third", "your_prefs");
    }
}


