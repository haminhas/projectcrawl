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

    //create new window that lets you change the player names
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

    //return to start screen and save the new player names
    public void save(View view){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        TextView one = (TextView) findViewById(R.id.editText);
        TextView two = (TextView) findViewById(R.id.editText2);
        String player = ""+ one.getText();
        String playerr = ""+ two.getText();
        editor.putString("player1", player);
        editor.putString("player2", playerr);
        editor.commit();
        finish();
    }

    //return to start screen without saving
    public void cancel(View view){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }
}
