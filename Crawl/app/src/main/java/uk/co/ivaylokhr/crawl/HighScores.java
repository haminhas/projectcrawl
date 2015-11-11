package uk.co.ivaylokhr.crawl;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class HighScores extends Activity {
    private TextView first;
    private TextView second;
    private TextView third;
    private TextView games;
    private TextView fastest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        Log.i("tag", "test");

        //retrieves the list of high scores and shows them on screen
        first = (TextView) findViewById(R.id.textView6);
        second = (TextView) findViewById(R.id.textView10);
        third = (TextView) findViewById(R.id.textView11);
        games = (TextView) findViewById(R.id.textView12);
        fastest = (TextView) findViewById(R.id.fastgame);
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        first.setText("1st Place:  " + sp.getInt("first", -1));
        second.setText("2nd Place: "+sp.getInt("second", -1));
        third.setText("3rd Place:  " + sp.getInt("third", -1));
        games.setText("Total Games played: " + sp.getInt("games", -1));
        String temp = (String) sp.getString("times", "");
        Log.i("tag",temp + "d");
        if(temp.equals("")){
            temp = "00:00";
        }
        fastest.setText("Fastest Game played: " + temp);
    }
    //Closes the window and returns to the start menu
    public void back(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_scores, menu);
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
}
