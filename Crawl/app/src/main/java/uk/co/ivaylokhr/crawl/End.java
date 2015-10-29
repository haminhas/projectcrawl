package uk.co.ivaylokhr.crawl;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class End extends AppCompatActivity {
    private TextView winner1;
    private TextView winner2;
    private TextView loser1;
    private TextView loser2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        winner1 = (TextView) findViewById(R.id.winner1);
        winner2 = (TextView) findViewById(R.id.winner2);
        winner1.setTextColor(Color.rgb(245,245,12));
        winner2.setTextColor(Color.rgb(245,245,12));
        loser1 = (TextView) findViewById(R.id.loser1);
        loser2 = (TextView) findViewById(R.id.loser2);
        loser1.setTextColor(Color.rgb(92,81,15));
        loser2.setTextColor(Color.rgb(92,81,15));
        Intent intent = getIntent();
        String draw = intent.getStringExtra("score");
        if (draw.equals("49")){
            winner1.setText("Draw");
            winner2.setText(intent.getStringExtra("name") + "-" + intent.getStringExtra("name2"));
            loser1.setText("49 - 49");
            loser2.setText(intent.getStringExtra(""));
        }else {
            Log.i("tag",intent.getStringExtra("name"));
            winner1.setText(intent.getStringExtra("name"));
            winner2.setText(intent.getStringExtra("score"));
            loser1.setText(intent.getStringExtra("name2"));
            loser2.setText(intent.getStringExtra("score2"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end, menu);
        return true;
    }

    public void mainMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
        startActivity(intent);
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
