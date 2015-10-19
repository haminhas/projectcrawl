package uk.co.ivaylokhr.crawl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Cup[] cups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Board b = new Board(fillTheArray());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private Cup[] fillTheArray(){
        cups = new Cup[16];
        cups[0] = (PocketCup) findViewById(R.id.b0);
        cups[1] = (PocketCup) findViewById(R.id.b1);
        cups[2] = (PocketCup) findViewById(R.id.b2);
        cups[3] = (PocketCup) findViewById(R.id.b3);
        cups[4] = (PocketCup) findViewById(R.id.b4);
        cups[5] = (PocketCup) findViewById(R.id.b5);
        cups[6] = (PocketCup) findViewById(R.id.b6);
        cups[7] = (PlayerCup) findViewById(R.id.b7);
        cups[8] = (PocketCup) findViewById(R.id.b8);
        cups[9] = (PocketCup) findViewById(R.id.b9);
        cups[10] = (PocketCup) findViewById(R.id.b10);
        cups[11] = (PocketCup) findViewById(R.id.b11);
        cups[12] = (PocketCup) findViewById(R.id.b12);
        cups[13] = (PocketCup) findViewById(R.id.b13);
        cups[14] = (PocketCup) findViewById(R.id.b14);
        cups[15] = (PlayerCup) findViewById(R.id.b15);
        return cups;
    }
}
