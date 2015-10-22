package uk.co.ivaylokhr.crawl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Game extends AppCompatActivity {

    private Cup[] cups;
    private Board b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        b = new Board(fillTheArray());
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
        cups[0].setId(0);
        cups[1] = (PocketCup) findViewById(R.id.b1);
        cups[1].setId(1);
        cups[2] = (PocketCup) findViewById(R.id.b2);
        cups[2].setId(2);
        cups[3] = (PocketCup) findViewById(R.id.b3);
        cups[3].setId(3);
        cups[4] = (PocketCup) findViewById(R.id.b4);
        cups[4].setId(4);
        cups[5] = (PocketCup) findViewById(R.id.b5);
        cups[5].setId(5);
        cups[6] = (PocketCup) findViewById(R.id.b6);
        cups[6].setId(6);
        cups[7] = (PlayerCup) findViewById(R.id.b7);
        cups[7].setId(7);
        cups[8] = (PocketCup) findViewById(R.id.b8);
        cups[8].setId(8);
        cups[9] = (PocketCup) findViewById(R.id.b9);
        cups[9].setId(9);
        cups[10] = (PocketCup) findViewById(R.id.b10);
        cups[10].setId(10);
        cups[11] = (PocketCup) findViewById(R.id.b11);
        cups[11].setId(11);
        cups[12] = (PocketCup) findViewById(R.id.b12);
        cups[12].setId(12);
        cups[13] = (PocketCup) findViewById(R.id.b13);
        cups[13].setId(13);
        cups[14] = (PocketCup) findViewById(R.id.b14);
        cups[14].setId(14);
        cups[15] = (PlayerCup) findViewById(R.id.b15);
        cups[15].setId(15);
        return cups;
    }
}
