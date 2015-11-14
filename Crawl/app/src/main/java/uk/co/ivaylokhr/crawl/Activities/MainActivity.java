package uk.co.ivaylokhr.crawl.Activities;

    import android.app.Activity;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.TypedValue;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;

    import uk.co.ivaylokhr.crawl.R;

public class MainActivity extends Activity {
        private int players;
        public final static String EXTRA_MESSAGE = "uk.co.ivaylokhr.MESSAGE";
        private TextView textView;
        private Button buttonOne;
        private Button buttonTwo;
        private Button buttonThree;
        private Button buttonFour;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setScores();
            setContentView(R.layout.activity_main);
            initialiseScreen();
        }
        //TODO: Either use MenuItems or remove this.
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        //initialise text messages and home sreen buttons
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
        public void onClickOnePlayer(View view){
                Intent intent = new Intent(this, AIGameActivity.class);
                players = 1;
                intent.putExtra(EXTRA_MESSAGE, players);
                startActivity(intent);
        }

        //starts a two player game

        public void onClickTwoPlayer(View view){
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
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

        //opens the High Score Menu
        public void onClickHighScore(View view){
                Intent intent = new Intent(this, HighScores.class);
                startActivity(intent);
        }

        //opens the Settings Menu
        public void onClickSettings(View view){
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
        }

        @Override
        public void onBackPressed(){
            Intent goHome = new Intent(Intent.ACTION_MAIN);
            goHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(goHome);
        }
    }
