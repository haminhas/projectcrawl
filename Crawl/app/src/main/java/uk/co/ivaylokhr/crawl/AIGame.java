package uk.co.ivaylokhr.crawl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.RelativeLayout;

public class AIGame extends AppCompatActivity {

    private Cup[] cups;
    private AIPlayer b;
    private AIPlayer ai;
    private ImageButton imgButton;
    private TextView timer;
    long startTime;
    long timeCounter=0;
    Handler handler = new Handler();
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        timer= (TextView) findViewById(R.id.Timer);
        startTime = System.currentTimeMillis();
        handler.postDelayed(updateTimer, 0);
        b = new AIPlayer(fillTheArray());
        b.addContent(getBaseContext());
        addgame();
        //ai= new AIPlayer(fillTheArray());
        //Create the Settings button on click listener
        imgButton =(ImageButton)findViewById(R.id.imageButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.settings, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT,
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT);
                //Displays Player 1 and Player 2
                String player1 = sp.getString("player1", "");
                String player2 = sp.getString("player2", "");
                final EditText text1 = (EditText) popupView.findViewById(R.id.editText);
                final EditText text2 = (EditText) popupView.findViewById(R.id.editText2);
                text1.setText(player1);
                text2.setText(player2);
                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                Button btnMain = (Button) popupView.findViewById(R.id.main);

                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                //Creates onClickListener that closes the game and returns to the main menu
                btnMain.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setNames(text1.getText(), text2.getText());
                        back();
                    }
                });
//              //Creates onClickListener that closes the settings menu
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setNames(text1.getText(), text2.getText());
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(imgButton, 50, -30);
            }
        });
    }
    //Creates timer
    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeCounter = System.currentTimeMillis()-startTime;
            long totalTime = timeCounter;
            long hours = totalTime/3600000;
            long minutes = (totalTime-hours*3600000)/60000;
            long seconds = (totalTime-hours*3600000-minutes*60000)/1000;
            String time = String.format("%02d:%02d",minutes, seconds);
            timer.setText(time);
            handler.postDelayed(this, 0);
        }};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addgame(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Integer games = sp.getInt("games", -1);
        games++;
        editor.putInt("games", games);
        editor.commit();
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

    public void back() {
        finish();
    }

    public void setNames(Editable player1, Editable player2){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(!player1.toString().equals("")){
            editor.putString("player1", player1.toString());
            editor.commit();}
        else{
            editor.putString("player1", "Player 1");
            editor.commit();
        }
        if(!player2.toString().equals("")) {
            editor.putString("player2", player2.toString());
            editor.commit();
        }
        else{
            editor.putString("player2", "Player 2");
            editor.commit();
        }
    }
    public Cup[] fillTheArray(){
        cups = new Cup[16];

        int[] ids = {R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5,
                R.id.b6, R.id.b7, R.id.b8, R.id.b9, R.id.b10, R.id.b11,
                R.id.b12, R.id.b13, R.id.b14, R.id.b15};

        for(int i=0; i<ids.length; i++) {
            if (i==7 || i==15) {
                cups[i] = (PlayerCup) findViewById(ids[i]);
                cups[i].setId(i);
            } else {
                cups[i] = (PocketCup) findViewById(ids[i]);
                cups[i].setId(i);
            }
        }

        return cups;
    }
}