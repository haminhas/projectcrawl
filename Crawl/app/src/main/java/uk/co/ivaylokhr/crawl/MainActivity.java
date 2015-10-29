package uk.co.ivaylokhr.crawl;

    import android.app.Activity;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.util.TypedValue;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.RadioButton;
    import android.widget.TextView;

    public class MainActivity extends AppCompatActivity {
        int players;
        public final static String EXTRA_MESSAGE = "uk.co.ivaylokhr.MESSAGE";
        TextView textView;
        public Button one;
        public Button two;
        public Button three;
        public Button four;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setScores();
            setContentView(R.layout.activity_main);
            textView = (TextView) findViewById(R.id.textView);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.textsize));
            one = (Button) findViewById(R.id.button);
            two = (Button) findViewById(R.id.button9);
            three = (Button) findViewById(R.id.button3);
            four = (Button) findViewById(R.id.button2);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        //starts a one player game
        public void onePlayer(View view){
            if (one.getText().equals("1 player")) {
                Intent intent = new Intent(this, Game.class);
                players = 1;
                intent.putExtra(EXTRA_MESSAGE, players);
                startActivity(intent);
            }else {
                Integer hosting = 1;
                Intent intent = new Intent(this, Bluetooth.class);
                intent.putExtra(EXTRA_MESSAGE, hosting);
                startActivity(intent);
            }
        }
        //Initialise the high scores.
        public  void setScores(){
            SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
                Integer games = sp.getInt("games", -1);
            if (games == -1) {
                editor.putInt("first", 0);
                editor.putInt("second", 0);
                editor.putInt("third", 0);
                editor.putInt("games", 0);
                editor.commit();
            }
        }
        //starts a two player game
        public void twoPlayer(View view){
            Log.i("tag", (String) two.getText());
            if (two.getText().equals("2 player")) {
                one.setText("Host");
                two.setText("Connect");
                three.setText("Back");
                four.setText("Share");
            }
            else {
                Integer hosting = 0;
                Intent intent = new Intent(this, Bluetooth.class);
                intent.putExtra(EXTRA_MESSAGE, hosting);
                startActivity(intent);
            }
        }




        public void highScore(View view){
            if(four.getText().equals("Statistics")) {
                Intent intent = new Intent(this, HighScores.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, Game.class);
                players = 2;
                intent.putExtra(EXTRA_MESSAGE, players);
                startActivity(intent);
            }
        }

        public void Settings(View view){
            if(four.getText().equals("Settings")) {

            }else{
                one.setText("1 player");
                two.setText("2 player");
                three.setText("Settings");
                four.setText("Statistics");
            }
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
