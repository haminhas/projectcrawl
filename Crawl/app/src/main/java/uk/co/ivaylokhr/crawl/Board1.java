package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

//TODO: This is a test.
public class Board1 extends AppCompatActivity {
    private Cup[] cups;
    private Player player1, player2, player3;
    private boolean isFirstTurn;
    private TextView playerone;
    private TextView playertwo;
    private TextView turn;
    private GameActivity activity;
    //first turn stuff
    private Player firstPlayer;
    private int firstID, secondID;
    Context context;
    private boolean firstHasPlayed, secondHasPlayed;

    public Board1(GameActivity activity) {
        this.activity = activity;
        cups = activity.getButtons();
        player1 = new Player((PlayerCup) cups[7]);
        player2 = new Player((PlayerCup) cups[15]);
        //player 3 is for draw
        player3 = new Player((PlayerCup) cups[15]);
        initializeFirstTurn();
        initializeButtons();
    }

    //a separate method for initializing values of the variables neeeded for first Turn
    private void initializeFirstTurn(){
        enableAllButtons();
        player1.setTurn(false);
        player2.setTurn(false);
        //The player who will have the first play
        firstPlayer = null;
        firstID = -1; secondID = -1;
        firstHasPlayed = false; secondHasPlayed = false;
        isFirstTurn = true;
    }



    //adds GameActivity.java content to the board class
    public void addContent(Context context){
        context = this.context;
    }

    //This method edits the highscores after the game
    public void updateScores() {
        Integer score = checkWinner().playerCup.getMarbles();
        Integer one = Preferences.fromPreferences(activity.getBaseContext(), -1, "first", "your_prefs");
        Integer two = Preferences.fromPreferences(activity.getBaseContext(), -1, "second", "your_prefs");
        Integer three = Preferences.fromPreferences(activity.getBaseContext(), -1, "third", "your_prefs");
        activity.setShortedPlayedTime();
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
                firstPlayer = player2;
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
            applyFirstTurnChanges(firstID, secondID);
        }
    }

    //This is called only in the first turn to update the board after both players made their moves
    private void applyFirstTurnChanges(int firstID, int secondID){
        ((PocketCup)cups[firstID]).emptyCup();
        ((PocketCup)cups[secondID]).emptyCup();
        for (int i = 1; i < 8; i++){
            int nextCupID = i + firstID;
            cups[nextCupID].addMarbles(1);
        }
        for(int i = 1; i < 8; i++){
            int nextCupID = i + secondID;
            if(nextCupID > 15){
                nextCupID -= 16;
            }
            cups[nextCupID].addMarbles(1);
        }
        updateBoardView();
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

    //update all the buttons (background and text)
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

    //This method loops through the current player's half and determine if you can make a move
    //If there is no valid move, it switches the player to make a move
    private void checkIfPlayerCanPlay() {
        if (player1.getTurn()) {
            //loops through the half ot player 1
            for (int i = 0; i < 7; i++) {
                // break if there is a valid move
                if (!((PocketCup) cups[i]).isEmpty()) {
                    return;
                }
            }
        } else if (player2.getTurn()) {
            //loops through the half ot player 2
            for (int i = 8; i < 15; i++) {
                //break if there is a valid move
                if (!((PocketCup) cups[i]).isEmpty()) {
                    return;
                }
            }
        }
        // if it didn't break, force the switch of turns
        forceSwitch();
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
        //checks if there is a valid method
        //If not, change the turn to the other player
        checkIfPlayerCanPlay();
    }


    //view is the object the animation needs to be aplied to
    //index is the index in the queue if there needs to be chained with other animations
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
        if(player1.getScore() > player2.getScore()) {
            return player1;
        }else if(player1.getScore() < player2.getScore()) {
            return player2;
        } else{
            return player3;
        }
    }
}