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
    import android.widget.EditText;
    import android.widget.RadioButton;
    import android.widget.TextView;

    public class MainActivity extends AppCompatActivity {
        int players;
        public final static String EXTRA_MESSAGE = "uk.co.ivaylokhr.MESSAGE";
        TextView textView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setScores();
            setContentView(R.layout.activity_main);
            textView = (TextView) findViewById(R.id.textView);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.textsize));
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        public void onePlayer(View view){
            Intent intent = new Intent(this, Game.class);
            players = 1;
            intent.putExtra(EXTRA_MESSAGE, players);
            startActivity(intent);
        }
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

        public void twoPlayer(View view){
            Intent intent = new Intent(this, Game.class);
            players = 2;
            intent.putExtra(EXTRA_MESSAGE, players);
            startActivity(intent);
        }

        public void highScore(View view){
            Intent intent = new Intent(this, HighScores.class);
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
