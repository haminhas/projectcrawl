package Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import Model.Cup;
import Controller.Game;
import Model.Preferences;
import uk.co.ivaylokhr.crawl.R;

public class GameActivity extends Activity {

    private Button[] buttons;
    private Game game ;
    private ImageButton imgButton;
    private TextView timer;
    private long startTime;
    private long timeCounter=0;
    private Handler handler = new Handler();
    private TextView turn;
    private TextView playerOneLabelName;
    private TextView playerTwoLabelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initialiseGame();
        initializeButtons();
        increaseGamesPlayed();
        enableAllButtons();
        addPlayerNames();
        updateView();
        settings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initialiseGame() {
        game = new Game();
        turn = (TextView) findViewById(R.id.turn);
        timer = (TextView) findViewById(R.id.Timer);
        playerOneLabelName = (TextView) findViewById(R.id.player1);
        playerTwoLabelName = (TextView) findViewById(R.id.player2);
        startTime = System.currentTimeMillis();
        handler.postDelayed(updateTimer, 0);
        buttons = fillButtonsArray();
    }


    private void addPlayerNames(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        //Displays Player 1 and Player 2
        String playerOneName = sp.getString("player1", "");
        String playerTwoName = sp.getString("player2", "");
        if(playerOneName.equals("")){
            playerOneName = "Player 1";
        }
        if(playerTwoName.equals("")){
            playerTwoName = "Player 2";
        }
        playerOneLabelName.setText(playerOneName);
        playerTwoLabelName.setText(playerTwoName);
        game.getPlayer1().setName(playerOneName);
        game.getPlayer2().setName(playerTwoName);
        turn.setText("Turn 1");
        turn.setTextColor(Color.GREEN);
    }

    //adds game to the number of games played
    private void increaseGamesPlayed(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Integer games = sp.getInt("games", -1);
        games++;
        editor.putInt("games", games);
        editor.commit();
    }

    //Create the Settings button on click listener
    private void settings(){
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
    private Runnable updateTimer = new Runnable() {
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
                "the progress of this game will be lost!").setCancelable(true)
                .setPositiveButton("Yes", new GoToMainMenu(mainMenu))
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
    private void setShortestPlayedTime(){
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
    private Button[] fillButtonsArray(){
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
    public void endGame(String[] finalScores){
        Intent intent = new Intent(this, End.class);
        intent.putExtra("name", finalScores[0]);
        intent.putExtra("score", finalScores[1]);
        intent.putExtra("name2", finalScores[2]);
        intent.putExtra("score2", finalScores[3]);
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


    private void enableAllButtons() {
        for (int i = 0; i < buttons.length; i++) {
            //ignore the player cups
            if (i == 7 || i == 15) {
                buttons[i].setEnabled(false);
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
                    int marbles = game.getBoardCups()[b.getId()].getMarbles();
                        activateAnimation(b.getId(), marbles);
                    game.pressCup(b.getId());
                    playClickSound();
                    swapEnabledButtonsOnTurnChange();
                    updateBoardView();
                    if(game.isGameFinished()){
                        updateScores();
                        endGame(game.checkWinner());
                    }
                }
            });

        }
    }

    private void activateAnimation(int idCurrentCup, int marbles) {
        int nextCup = idCurrentCup + 1;
        for (int i = 0; i < marbles; i++) {
            if (nextCup == 15 && game.isPlayerOneTurn()){
                nextCup = 0;
            }
            if (nextCup == 7 && !game.isPlayerOneTurn()){
                nextCup += 1;
            }
            playZoomAnimation(buttons[nextCup], i);
            nextCup += 1;
            if (nextCup > 15){
                nextCup = 0;
            }
        }
    }

    //view is the object the animation needs to be aplied to
    //index is the index in the queue if there needs to be chained with other animations
    private void playZoomAnimation(View view, int index){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomanim);
        animation.setStartOffset(200*index);
        view.startAnimation(animation);
    }


    private void playClickSound(){
        MediaPlayer media = MediaPlayer.create(this, R.raw.click);
        media.start();
    }

    private void swapEnabledButtonsOnTurnChange() {
        if(game.isPlayerOneTurn()){
            for (int i = 0; i < 7; i++) {
                buttons[i].setEnabled(true);
            }

            for (int i = 8; i < 15; i++) {
                buttons[i].setEnabled(false);
            }
        }else{
            for (int i = 0; i < 7; i++) {
                buttons[i].setEnabled(false);
            }

            for (int i = 8; i < 15; i++) {
                buttons[i].setEnabled(true);
            }
        }
    }

    public void updateView() {
        Cup[] cups = game.getBoardCups();
        int[] backgrounds = {R.drawable.pocketbackground, R.drawable.back1, R.drawable.back2, R.drawable.back3,
                R.drawable.back4, R.drawable.back5, R.drawable.back6, R.drawable.back7, R.drawable.back8};
        for (int i = 0; i < cups.length; i++) {
            buttons[i].setText(Integer.toString(cups[i].getMarbles()));

            int marbles = cups[i].getMarbles();
            if (marbles <= 7) {
                buttons[i].setBackgroundResource(backgrounds[marbles]);
            } else {
                buttons[i].setBackgroundResource(backgrounds[8]);
            }
        }
    }
    //updates the information on the board
    private void updateBoardView(){
        updateTurnText();
        updateView();
    }

    //update Ivaylo's textview on the top of the screen, it might be removed if you feel like it
    private void updateTurnText(){
        String turnText = "";
        if(game.isPlayerOneTurn()) {
            turnText = (String) playerOneLabelName.getText();
            playerOneLabelName.setTextColor(Color.GREEN);
            playerTwoLabelName.setTextColor(Color.BLACK);
        }else{
            turnText = (String) playerTwoLabelName.getText();
            playerTwoLabelName.setTextColor(Color.GREEN);
            playerOneLabelName.setTextColor(Color.BLACK);
        }
        turn.setText(turnText + "'s turn");
        turn.setTextColor(Color.GREEN);
    }

    //This method edits the highscores after the game
    public void updateScores() {
        Integer score = game.checkWinnerScore();
        Integer one = Preferences.fromPreferences(this, -1, "first", "your_prefs");
        Integer two = Preferences.fromPreferences(this.getBaseContext(), -1, "second", "your_prefs");
        Integer three = Preferences.fromPreferences(this.getBaseContext(), -1, "third", "your_prefs");
        this.setShortestPlayedTime();
        if (score > one) {
            three = two;
            two = one;
            one = score;
        } else if (score > two) {
            three = two;
            two = score;
        } else if (score > three) {
            three = score;
        }
        Preferences.toPreferences(this.getBaseContext(), one, "first", "your_prefs");
        Preferences.toPreferences(this.getBaseContext(), two, "second", "your_prefs");
        Preferences.toPreferences(this.getBaseContext(), three, "third", "your_prefs");
    }


}
