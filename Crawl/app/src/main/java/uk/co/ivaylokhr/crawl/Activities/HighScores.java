package uk.co.ivaylokhr.crawl.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import uk.co.ivaylokhr.crawl.R;

public class HighScores extends Activity {
    private TextView first;
    private TextView second;
    private TextView third;
    private TextView games;
    private TextView fastest;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        //retrieves the list of high scores and shows them on screen
        first = (TextView) findViewById(R.id.textView6);
        second = (TextView) findViewById(R.id.textView10);
        third = (TextView) findViewById(R.id.textView11);
        games = (TextView) findViewById(R.id.textView12);
        fastest = (TextView) findViewById(R.id.fastgame);
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        first.setText(getString(R.string.firstplace) + sp.getInt("first", -1));
        second.setText(getString(R.string.secondplace)+sp.getInt("second", -1));
        third.setText(getString(R.string.thirdplace) + sp.getInt("third", -1));
        games.setText(getString(R.string.totalgames) + sp.getInt("games", -1));
        String temp = sp.getString("times", "");
        if(temp.equals("")){
            temp = "00:00";
        }
        fastest.setText(getString(R.string.fastestgame) + temp);
    }

    /**
     * Closes the window and returns to the start menu
     * @param view
     */
    public void back(View view) {
        finish();
    }

    //returns to Main Menu on back pressed
    @Override
    public void onBackPressed(){
        finish();
    }
}
