package uk.co.ivaylokhr.crawl.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import uk.co.ivaylokhr.crawl.R;

public class Settings extends Activity {
    private String player1;
    private String player2;

    /**
     * create new window that lets you change the player names
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        //Displays Player 1 and Player 2
        player1 = sp.getString("player1", "");
        player2 = sp.getString("player2", "");
        if (player1.equals("")){
            player1 = getString(R.string.player1);
        }
        if (player2.equals("")){
            player2 = getString(R.string.player2);
        }
        TextView one = (TextView) findViewById(R.id.editText);
        TextView two = (TextView) findViewById(R.id.editText2);
        one.setText(player1);
        two.setText(player2);
    }

    /**
     * return to start screen and save the new player names
     * @param view
     */
    public void save(View view){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        TextView one = (TextView) findViewById(R.id.editText);
        TextView two = (TextView) findViewById(R.id.editText2);
        String playerone = ""+ one.getText();
        String playertwo = ""+ two.getText();
        editor.putString("player1", playerone);
        editor.putString("player2", playertwo);
        editor.commit();
        finish();
    }

    /**
     * return to start screen without saving
     * @param view
     */
    public void cancel(View view){
        finish();
    }

    //returns to Maain Menu on back pressed
    @Override
    public void onBackPressed(){
        finish();
    }

    /**
     * @return Player1
     */
    public String getPlayer1() {
        return player1;
    }

    /**
     * @return Player2
     */
    public String getPlayer2() {
        return player2;
    }
}
