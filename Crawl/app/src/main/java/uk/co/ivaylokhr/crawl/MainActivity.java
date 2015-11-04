package uk.co.ivaylokhr.crawl;

    import android.app.Activity;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
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
        public Button buttonOne;
        public Button buttonTwo;
        public Button buttonThree;
        public Button buttonFour;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setScores();
            setContentView(R.layout.activity_main);
            initialiseScreen();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        //initalise text mesages and home sreen buttons
        public void initialiseScreen() {
            textView = (TextView) findViewById(R.id.textView);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.textsize));
            buttonOne = (Button) findViewById(R.id.button);
            buttonTwo = (Button) findViewById(R.id.button9);
            buttonThree = (Button) findViewById(R.id.button3);
            buttonFour = (Button) findViewById(R.id.button2);
        }

        //starts a one player game
        public void onePlayer(View view){
            if (buttonOne.getText().equals("1 player")) {
                Intent intent = new Intent(this, AIGame.class);
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

        //starts a two player game
        public void twoPlayer(View view){
            if (buttonTwo.getText().equals("2 player")) {
                buttonOne.setText("Host");
                buttonTwo.setText("Connect");
                buttonThree.setText("Back");
                buttonFour.setText("Share");
            }
            else {
                Integer hosting = 0;
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
                editor.putString("times","00:00");
                editor.putInt("third", 0);
                editor.putInt("games", 0);
                editor.commit();
            }
        }

        public void highScore(View view){
            if(buttonFour.getText().equals("Statistics")) {
                Intent intent = new Intent(this, HighScores.class);
                startActivity(intent);
            }else{
                    Intent intent = new Intent(this, Game.class);
                    startActivity(intent);
            }
        }

        public void Settings(View view){
            if(buttonThree.getText().equals("Settings")) {
                Intent intent = new Intent(this, Set.class);
                startActivity(intent);
            }else{
                buttonOne.setText("1 player");
                buttonTwo.setText("2 player");
                buttonThree.setText("Settings");
                buttonFour.setText("Statistics");
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

        @Override
        public void onBackPressed(){
            finish();
        }
    }
