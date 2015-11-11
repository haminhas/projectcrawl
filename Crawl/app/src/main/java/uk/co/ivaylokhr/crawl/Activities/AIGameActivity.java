package uk.co.ivaylokhr.crawl.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import uk.co.ivaylokhr.crawl.Controller.AIGame;
import uk.co.ivaylokhr.crawl.Model.Cup;
import uk.co.ivaylokhr.crawl.Model.Preferences;
import uk.co.ivaylokhr.crawl.R;

public class AIGameActivity extends Activity {

    private Button[] buttons;
    private AIGame aiGame;
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

    public void initialiseGame() {
        aiGame = new AIGame();
        turn = (TextView) findViewById(R.id.turn);
        timer = (TextView) findViewById(R.id.Timer);
        playerOneLabelName = (TextView) findViewById(R.id.player1);
        playerTwoLabelName = (TextView) findViewById(R.id.player2);
        startTime = System.currentTimeMillis();
        handler.postDelayed(updateTimer, 0);
        buttons = fillButtonsArray();
    }


    public void addPlayerNames(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        //Displays Player 1 and Player 2
        String playerOneName = sp.getString("player1", "");
        if(playerOneName.equals("")){
            playerOneName = "Player 1";
        }
        playerOneLabelName.setText(playerOneName);
        playerTwoLabelName.setText("Computer");
        aiGame.getHumanPlayer().setName(playerOneName);
        aiGame.getAIPlayer().setName("Computer");
        turn.setText("Turn 1");
        turn.setTextColor(Color.GREEN);
    }

    //adds aiGame to the number of games played
    public void increaseGamesPlayed(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Integer games = sp.getInt("games", -1);
        games++;
        editor.putInt("games", games);
        editor.commit();
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
                //Creates onClickListener that closes the aiGame and returns to the main menu
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


    //warn the player that going back will end his aiGame
    @Override
    public void onBackPressed() {
        AlertDialog.Builder optionpane = new AlertDialog.Builder(this);
        Intent mainMenu = new Intent(this, MainActivity.class);
        optionpane.setTitle("Go back?");
        optionpane.setMessage("Are you sure you want to go back? This will take you to the main menu and all " +
                "the progress of this game will be lost!").setCancelable(true)
                .setPositiveButton("Yes", new GoToActivityListener(mainMenu))
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

    //set the high score for the least time a aiGame has taken to complete
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

    public class GoToActivityListener implements DialogInterface.OnClickListener{
        private Intent activity;
        public GoToActivityListener(Intent intent){
            activity = intent;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(activity);
        }
    }


    private void enableAllButtons() {
        for (int i = 0; i < 7; i++) {
                buttons[i].setEnabled(true);
        }
        for (int i = 7; i < 16; i++) {
            buttons[i].setEnabled(false);
        }
    }

    //Initialize onClickListener and update the view of all the buttons on board
    private void initializeButtons(){
        for (final Button b : buttons) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int marbles = aiGame.getBoardCups()[b.getId()].getMarbles();
                    activateAnimation(b.getId(), marbles);
                    aiGame.pressCup(b.getId());
                    playClickSound();
                    swapEnabledButtonsOnTurnChange();
                    updateBoardView();
                    if(aiGame.isGameFinished()){
                        updateScores();
                        popUpGameFinished();
                    }
                    if(!aiGame.isPlayerOneTurn()){
                        aiMove(marbles);
                    }
                }
            });

        }
    }

    private void popUpGameFinished() {
        String[] finalResults = aiGame.getFinalResults();
        AlertDialog.Builder optionpane = new AlertDialog.Builder(this);
        Intent mainMenu = new Intent(this, MainActivity.class);
        Intent newAIGame = new Intent(this, AIGameActivity.class);
        optionpane.setTitle("Game Finished");
        optionpane.setMessage("Winner: " + finalResults[0] + ": " + finalResults[1] + "\nLoser: " + finalResults[2] + ": " + finalResults[3] ).setCancelable(false)
                .setPositiveButton("Main Menu", new GoToActivityListener(mainMenu))
                .setNegativeButton("Play Again", new GoToActivityListener(newAIGame));
        AlertDialog alertDialog = optionpane.create();
        alertDialog.show();
    }

    private void aiMove(int marbles) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("tag", "test1");
                    aiGame.doMove();
                    // Do something after 2s = 2000ms
                    swapEnabledButtonsOnTurnChange();
                    updateBoardView();
                    activateAnimation(aiGame.returnFirstButton(), aiGame.returnMarbles());
                    if(aiGame.isGameFinished()){
                        updateScores();
                        popUpGameFinished();
                    }
                    if(!aiGame.isPlayerOneTurn()){
                        aiMove(aiGame.returnMarbles());
                    }
                }
            }, (marbles * 250)+1000);

        }


    public void activateAnimation(int idCurrentCup, int marbles) {
        int nextCup = idCurrentCup + 1;
        for (int i = 0; i < marbles; i++) {
            if (nextCup == 15 && aiGame.isPlayerOneTurn()){
                nextCup = 0;
            }
            if (nextCup == 7 && !aiGame.isPlayerOneTurn()){
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

    public void swapEnabledButtonsOnTurnChange() {
        if(aiGame.isPlayerOneTurn()){
            for (int i = 0; i < 7; i++) {
                if (aiGame.getBoardCups()[i].getMarbles() > 0){
                    buttons[i].setEnabled(true);
                }
                else{
                    buttons[i].setEnabled(false);
                }
            }
        }else{
            for (int i = 0; i < 7; i++) {
                buttons[i].setEnabled(false);
            }
        }
    }

    public void updateView() {
        Cup[] cups = aiGame.getBoardCups();
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
        if(aiGame.isPlayerOneTurn()) {
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

    //This method edits the highscores after the aiGame
    public void updateScores() {
        Integer score = aiGame.checkWinnerScore();
        Integer one = Preferences.fromPreferences(this.getBaseContext(), -1, "first", "your_prefs");
        Integer two = Preferences.fromPreferences(this.getBaseContext(), -1, "second", "your_prefs");
        Integer three = Preferences.fromPreferences(this.getBaseContext(), -1, "third", "your_prefs");
        this.setShortedPlayedTime();
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