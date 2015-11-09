package uk.co.ivaylokhr.crawl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private Button[] buttons;
    private Game game ;
    private ImageButton imgButton;
    private TextView timer;
    long startTime;
    long timeCounter=0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initialiseGame();
        increaseGamesPlayed();
        setPlayersNameTextFields();
        enableAllButtons();
        settings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void initialiseGame() {
        timer = (TextView) findViewById(R.id.Timer);
        startTime = System.currentTimeMillis();
        handler.postDelayed(updateTimer, 0);
        buttons = fillButtonsArray();
        game = new Game();
    }

    //adds game to the number of games played
    public void increaseGamesPlayed(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Integer games = sp.getInt("games", -1);
        games++;
        editor.putInt("games", games);
        editor.commit();
    }

    //Sets the text fields and retrieves player 1 and player 2's names
    public void setPlayersNameTextFields(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        //Displays Player 1 and Player 2
        String player1 = sp.getString("player1", "");
        String player2 = sp.getString("player2", "");
        if(player1.equals("")){
            player1 = "Player 1";
        }
        if(player2.equals("")){
            player2 = "Player 2";
        }
        final TextView text1 = (TextView) findViewById(R.id.player1);
        final TextView text2 = (TextView) findViewById(R.id.player2);
        text1.setText(player1);
        text2.setText(player2);
        board1.addNames(text1, text2);
    }

    //Create the Settings button on click listener
    public void settings(){
        imgButton =(ImageButton)findViewById(R.id.imageButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.settings, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT,
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT);
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
                        back();
                    }
                });
                //Creates onClickListener that closes the settings menu
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
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


    //warn the player that going back will end his game
    @Override
    public void onBackPressed() {
        AlertDialog.Builder optionpane = new AlertDialog.Builder(this);
        Intent mainMenu = new Intent(this, MainActivity.class);
        optionpane.setTitle("Go back?");
        optionpane.setMessage("Are you sure you want to go back? This will take you to the main menu and all" +
                "the progress of this game will be lost!").setCancelable(true).setPositiveButton("Go to main menu", new GoToMainMenu(mainMenu))
                .setNegativeButton("Continue with the game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog alertDialog = optionpane.create();
        alertDialog.show();
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

    //closes window and returns to main menu
    public void back() {
        finish();
    }

    //set the high score for the least time a game has taken to complete
    public void setShortedPlayedTime(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String temp = (String) sp.getString("times", "");
        if(temp.equals("")){
            temp = "00:00";
        }
        String time = ""+ timer.getText();
        int one = temp.charAt(0)*10+temp.charAt(1);
        int two = temp.charAt(3)*10+temp.charAt(4);
        int three =time.charAt(0)*10+time.charAt(1);
        int four = time.charAt(3)*10+time.charAt(4);
        if(three < one) {
            editor.putString("times", time);
            editor.commit();
        }else if(four < two && one == three){
            editor.putString("times", time);
            editor.commit();
        }else if(temp.equals("00:00")){
            editor.putString("times", time);
            editor.commit();
        }
        editor.commit();
    }

    //fills the array with Player Cups and Pocket Cups and sets there ids
    public Button[] fillButtonsArray(){
        buttons = new Button[16];

        int[] ids = {R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5,
                R.id.b6, R.id.b7, R.id.b8, R.id.b9, R.id.b10, R.id.b11,
                R.id.b12, R.id.b13, R.id.b14, R.id.b15};

        for(int i=0; i<ids.length; i++) {
                buttons[i] = (Button) findViewById(ids[i]);
                buttons[i].setId(i);
        }

        return buttons;
    }

    //returns cups
    public Button[] getButtons(){
        return buttons;
    }

    //ends the game and starts the end game screen
    public void endGame(Player winner, Player loser){
        Intent intent = new Intent(this, End.class);
        intent.putExtra("name", winner.getName());
        intent.putExtra("score", winner.getScore()+"");
        intent.putExtra("name2", loser.getName());
        intent.putExtra("score2", loser.getScore()+"");
        startActivity(intent);
    }

    //This is for the dialog. It goes to the main menu if you say you want to
    public class GoToMainMenu implements DialogInterface.OnClickListener{
        private Intent mainMenu;
        public GoToMainMenu(Intent intent){
            mainMenu = intent;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(mainMenu);
        }
    }



    ///

    private void enableAllButtons() {
        for (int i = 0; i < buttons.length; i++) {
            //ignore the player cups
            if (i == 7 || i == 15) {
                continue;
            } else {
                buttons[i].setEnabled(true);
            }
        }
    }

    //Initialize onClickListener and update the view of all the buttons on board
    private void initializeButtons(){
        for (final Button b : buttons) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    game.board.pressCup(b.getId());
                }
            });
        }
        updateButtonText();
    }
}
