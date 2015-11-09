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



    private void playClickSound(){
        MediaPlayer media = MediaPlayer.create(activity, R.raw.click);
        media.start();
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