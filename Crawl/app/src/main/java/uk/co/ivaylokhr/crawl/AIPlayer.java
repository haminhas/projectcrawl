package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends AppCompatActivity {
//    private AIGame activity;
//    private Cup[] cups;
//    private Player player1, ai, player3;
//    Context context;
//    private TextView playerone;
//    private TextView playertwo;
//    private TextView turn;
//    private boolean isFirstTurn;
//
//    public AIPlayer(AIGame activity) {
//        this.activity = activity;
//        cups = activity.getCups();
//        player1 = new Player((PlayerCup) cups[7]);
//        player3 = new Player((PlayerCup) cups[7]);
//        ai = new Player((PlayerCup) cups[15]);
//        initializeFirstTurn();
//        initializeButtons();
//    }
//
//    private void initializeFirstTurn(){
//        player1.setTurn(true);
//        ai.setTurn(false);
//        isFirstTurn = true;
//    }
//
//    //Initialize onClickListener and update the view of all the buttons on board
//    private void initializeButtons(){
//        for (Cup c : cups) {
//            c.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    pressCup(v);
//                }
//            });
//        }
//        for (int i = 8; i < 15; i++) {
//            cups[i].setEnabled(false);
//        }
//        updateButtonText();
//    }
//
//    //The human player has a go
//    //The id of the button that was pressed is remembered
//    //Then the computer makes its move and then the changes are applied
//    private void doFirstTurn(int playerID){
//        int aiID = doFirstAIMove();
//        applyFirstMove(playerID, aiID);
//        isFirstTurn = false;
//        switchTurns(player1);
//        updateBoardView();
//    }
//
//    //Pure random move for the first turn
//    private int doFirstAIMove(){
//        switchTurns(ai);
//        Random rand = new Random();
//        return rand.nextInt(6) + 8;
//    }
//
//    //update the board with the first turn moves of the human and AI players
//    private void applyFirstMove(int playerMove, int AIMove){
//        ((PocketCup)cups[playerMove]).emptyCup();
//        ((PocketCup)cups[AIMove]).emptyCup();
//        for (int i = 1; i < 8; i++){
//            int nextCupID = i + playerMove;
//            cups[nextCupID].addMarbles(1);
//            playZoomAnimation(cups[nextCupID], i-1);
//        }
//        for(int i = 1; i < 8; i++){
//            int nextCupID = i + AIMove;
//            if(nextCupID > 15){
//                nextCupID -= 16;
//            }
//            cups[nextCupID].addMarbles(1);
//            playZoomAnimation(cups[nextCupID], i-1);
//        }
//    }
//
//    private PocketCup opposite() {
//        for(int i = 8; i < 15;i++){
//            int op = (cups[i].getMarbles() +cups[i].getId())%16;
//            if((op != 7 && op != 15) && cups[i].getMarbles() != 0){
//                PocketCup nextPocketCup = (PocketCup) cups[op];
//                if(nextPocketCup.isEmpty()){
//                    return (PocketCup) cups[i];
//                }
//            }
//        }
//        return (PocketCup) cups[1];
//    }
//
//    public PocketCup extraTurn(){
//        for(int i = 14; i > 7;i--){
//            if((cups[i].getMarbles() +cups[i].getId())%15 == 0){
//                return (PocketCup) cups[i];
//            }
//        }
//        return (PocketCup) cups[1];
//    }
//
//
//    public void doMove(){
//        int finalButtonID =0;
//        boolean again = false;
//        //get id of the pressed cup
//        ArrayList<PocketCup> temp = new ArrayList<>();
//        //Assuming ai player is at the top
//        for (int i = 8; i < 15; i++) {
//            if (!((PocketCup) cups[i]).isEmpty()) {
//                temp.add((PocketCup)cups[i]);
//            }
//        }
//        if(temp.isEmpty()){
//            forceSwitch();
//            return;
//        }
//        int id;
//        if(!extraTurn().equals(cups[1])) {
//            id = extraTurn().getId();
//            again = true;
//        }else if(!opposite().equals(cups[1])){
//                id = opposite().getId();
//        }else{
//            Random rand = new Random();
//            int randCup = rand.nextInt(temp.size());
//            PocketCup chosenCup = temp.get(randCup);
//            id = chosenCup.getId();
//        }
//        //empty cup
//        PocketCup pressedPocketCup = (PocketCup) cups[id];
//        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
//        //at this point please check if the cup in the array still has the marbles
//        putMarblesInNextCups(id, marblesFromEmptiedCup);
//
//        finalButtonID = id + marblesFromEmptiedCup;
//        if (finalButtonID > 15) {
//            finalButtonID -= 15;
//        }
//        checkForGameFinish();
//        decideTurn(finalButtonID);
//        updateBoardView();
//        if(again){
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Do something after 2s = 2000ms
//                    doMove();
//                }
//            }, (marblesFromEmptiedCup * 250)+1000);
//        }
//    }
//
//    //adds AIGame.java content to the board class
//    public void addContent(Context context){
//        context = this.context;
//    }
//
//    public void pressCup(View view) {
//        playClickSound();
//        if(isFirstTurn){
//            doFirstTurn(view.getId());
//            return;
//        }
//        //get id of the pressed cup
//        int id = ((PocketCup) view).getId();
//        //empty cup
//        PocketCup pressedPocketCup = (PocketCup) cups[id];
//        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
//        int finalButtonID =0;
//        if (player1.getTurn()) {
//            //at this point please check if the cup in the array still has the marbles
//            putMarblesInNextCups(id, marblesFromEmptiedCup);
//            finalButtonID = id + marblesFromEmptiedCup;
//            if (finalButtonID > 15) {
//                finalButtonID -= 15;
//            }
//            checkForGameFinish();
//            decideTurn(finalButtonID);
//            updateBoardView();
//        }
//
//        if (ai.getTurn()) {
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Do something after 2s = 2000ms
//                    doMove();
//                }
//            }, (marblesFromEmptiedCup * 250)+1000);
//        }
//    }
//
//    //updates the information on the board
//    private void updateBoardView(){
//        updateTurnText();
//        updatePlayerTurnIndicators();
//        updateButtonText();
//    }
//
//    private void playClickSound(){
//        MediaPlayer media = MediaPlayer.create(activity, R.raw.click);
//        media.start();
//    }
//
//    //update Ivaylo's textview on the top of the screen, it might be removed if you feel like it
//    private void updateTurnText(){
//        String turnText = "";
//        if(player1.getTurn()) {
//            turnText = (String) playerone.getText();
//        }else{
//            turnText = (String) playertwo.getText();
//        }
//        turn.setText(turnText + "'s turn");
//        turn.setTextColor(Color.GREEN);
//    }
//
//    //update the turn indicators Sola made
//    private void updatePlayerTurnIndicators() {
//        if (player1.getTurn()) {
//            playerone.setTextColor(Color.GREEN);
//            playertwo.setTextColor(Color.BLACK);
//        } else {
//            playertwo.setTextColor(Color.GREEN);
//            playerone.setTextColor(Color.BLACK);
//        }
//    }
//
//    //This method edits the highscores after the game
//    public void updateScores() {
//        Integer score = player1.getScore();
//        Integer one = Preferences.fromPreferences(activity.getBaseContext(), -1, "first", "your_prefs");
//        Integer two = Preferences.fromPreferences(activity.getBaseContext(), -1, "second", "your_prefs");
//        Integer three = Preferences.fromPreferences(activity.getBaseContext(), -1, "third", "your_prefs");
//        activity.setTime();
//        if (score > one) {
//            three = two;
//            two = one;
//            one = score;
//        } else if (score > two) {
//            three = two;
//            two = score;
//        } else if (score > three) {
//            three = score;
//        }
//        Preferences.toPreferences(activity.getBaseContext(), one, "first", "your_prefs");
//        Preferences.toPreferences(activity.getBaseContext(), two, "second", "your_prefs");
//        Preferences.toPreferences(activity.getBaseContext(), three, "third", "your_prefs");
//    }
//
//    private void updateButtonText() {
//        int[] backgrounds = {R.drawable.pocketbackground, R.drawable.back1, R.drawable.back2, R.drawable.back3,
//                R.drawable.back4, R.drawable.back5, R.drawable.back6, R.drawable.back7, R.drawable.back8};
//        for (int i = 0; i < cups.length; i++) {
//            Cup c = cups[i];
//            int marbles = c.getMarbles();
//            c.setText(Integer.toString(marbles));
//            if(i == 7 || i == 15) {
//                continue;
//            }
//            else{
//                if(marbles <= 7) {
//                    c.setBackgroundResource(backgrounds[marbles]);
//                }
//                else{
//                    c.setBackgroundResource(backgrounds[8]);
//                }
//            }
//        }
//    }
//
//    //This method loops through the current player's half and determine if you can make a move
//    //If there is no valid move, it switches the player to make a move
//    private void checkIfPlayerCanPlay() {
//        if (player1.getTurn()) {
//            //loops through the half ot player 1
//            for (int i = 0; i < 7; i++) {
//                // break if there is a valid move
//                if (!((PocketCup) cups[i]).isEmpty()) {
//                    break;
//                }
//                // at the end of the loop force the switch
//                if (i == 6) {
//                    forceSwitch();
//                    doMove();
//                }
//            }
//        } else if (ai.getTurn()) {
//            //loops through the half ot player 2
//            for (int i = 8; i < 15; i++) {
//                //break if there is a valid move
//                if (!((PocketCup) cups[i]).isEmpty()) {
//                    break;
//                }
//                // at the end of the loop force the switch
//                if (i == 14) {
//                    forceSwitch();
//                }
//            }
//        }
//    }
//
//    //This forces the switch of turns.It is called when one player doesn't have valid moves
//    private void forceSwitch(){
//        if(player1.getTurn()){
//            switchTurns(ai);
//        } else {
//            switchTurns(player1);
//        }
//    }
//
//    public void addNames(TextView player1, TextView player2){
//        playerone = player1;
//        playertwo = player2;
//        this.player1.setName((String) player1.getText());
//        ai.setName((String) player2.getText());
//        turn = (TextView) activity.findViewById(R.id.turn);
//        turn.setText("Turn 1");
//        turn.setTextColor(Color.GREEN);
//    }
//
//    //decides which turn is next depending on the id of the final marblees that has been put
//    private void decideTurn(int finalButtonID) {
//        if (player1.getTurn() && finalButtonID != 7) {
//            switchTurns(ai);
//        }
//        //if it landed on the player1 cup and is his turn
//        //we need this to make the button disabled
//        else if (player1.getTurn()) {
//            switchTurns(player1);
//        } else if (ai.getTurn() && finalButtonID != 15) {
//            switchTurns(player1);
//        }
//        //if it landed on the player2 cup and is his turn
//        //we need this to make the button disabled
//        else if (ai.getTurn()) {
//            switchTurns(ai);
//        }
//    }
//
//    //switches turn to the player who is given as a parameter
//    private void switchTurns(Player player) {
//        int t = 0;
//        if (player.equals(ai)) {
//            ai.setTurn(true);
//            player1.setTurn(false);
//        } else {
//            ai.setTurn(false);
//            player1.setTurn(true);
//        }
//        for (int i = 0; i < 7; i++) {
//            if (player1.getTurn() && cups[i].getMarbles() != 0) {
//                cups[i].setEnabled(true);
//            }else{
//                cups[i].setEnabled(false);
//            }
//        }
//        for (int i = 0; i < 15; i++) {
//            if (cups[i].getMarbles() == 0) {
//                cups[i].setEnabled(false);
//            }
//        }
//        checkIfPlayerCanPlay();
//    }
//
//    public void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
//        int cupNumber = idCurrentCup + 1;
//        for (int i = 0; i < marblesFromEmptiedCup; i++) {
//            //condition for when the cup is the playerCup
//            if (cupNumber == 7) {
//                if (player1.getTurn()) {
//                    player1.increaseScore(1);
//                    playZoomAnimation(cups[cupNumber], i);
//                    cupNumber++; //jumps PlayerCup and goes to next one
//                    continue; //finish current iteration on this point and to go to next iteration
//                } else {
//                    cupNumber = 8;
//                }
//            } else if (cupNumber == 15) {
//                if (ai.getTurn()) {
//                    ai.increaseScore(1);
//                    playZoomAnimation(cups[cupNumber], i);
//                    cupNumber = 0;
//                    continue; //finish current iteration on this point and to go to next iteration
//                } else {
//                    cupNumber = 0;
//                }
//            }
//
//            PocketCup nextPocketCup = (PocketCup) cups[cupNumber];
//            playZoomAnimation(nextPocketCup, i);
//
//            //check at the last iteration if cup is empty
//            if (i == marblesFromEmptiedCup - 1 && nextPocketCup.isEmpty()) {
//                PocketCup oppositeCup;
//                if (player1.getTurn() && cupNumber < 7) {
//                    oppositeCup = (PocketCup) cups[cupNumber + ((7 - cupNumber) * 2)];
//                    int oppositeCupNumbers = oppositeCup.emptyCup();
//                    nextPocketCup.addMarbles(-1);
//                    player1.increaseScore(oppositeCupNumbers + 1);
//                }
//                if(ai.getTurn() && 8 < cupNumber && cupNumber < 15){
//                    oppositeCup = (PocketCup) cups[14-cupNumber];
//                    int oppositeCupNumbers = oppositeCup.emptyCup();
//                    nextPocketCup.addMarbles(-1);
//                    ai.increaseScore(oppositeCupNumbers + 1);
//                }
//
//            }
//
//            nextPocketCup.addMarbles(1);
//
//            //update id for the next cup, and stay in the range of 15.
//            if (cupNumber >= 15) {
//                cupNumber = 0;
//            } else {
//                cupNumber++;
//            }
//
//
//        }//END OF FOR LOOP
//    }
//
//    //view is the object the animation needs to be aplied to
//    //index is the index in the queue if there needs to be chained with other animations
//    private void playZoomAnimation(View view, int index){
//        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.zoomanim);
//        animation.setStartOffset(200*index);
//        view.startAnimation(animation);
//    }
//
//    public boolean isGameFinished() {
//        //check if game is finished
//        return ai.getScore() + player1.getScore() == 98;
//    }
//
//    private void checkForGameFinish(){
//        if (isGameFinished()) {
//            updateScores();
//            if(checkWinner().equals(player1)) {
//                activity.endGame(checkWinner(), ai);
//            }else if(checkWinner().equals(ai)){
//                activity.endGame(checkWinner(), player1);
//            }else{
//                activity.endGame(checkWinner(), player1);
//            }
//        }
//    }
//
//
//    public Player checkWinner() {
//        //checks who won
//        if(player1.getScore() > ai.getScore()) {
//            return player1;
//        }else if(player1.getScore() < ai.getScore()) {
//            return ai;
//        } else{
//            return player3;
//        }
//    }

}

