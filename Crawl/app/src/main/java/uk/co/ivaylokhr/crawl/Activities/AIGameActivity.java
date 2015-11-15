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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import uk.co.ivaylokhr.crawl.Controller.AIGame;
import uk.co.ivaylokhr.crawl.Controller.AnimationRunnable;
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

    /**
     *
     * @param savedInstanceState
     */
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

    //initialises the game and creates the AIGame class
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


    //retrieves player names from the Shared Preferences and sets the player names
    public void addPlayerNames(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        //Displays Player 1 and Player 2
        String playerOneName = sp.getString("player1", "");
        if(playerOneName.equals("")){
            playerOneName = getString(R.string.player1);
        }
        playerOneLabelName.setText(playerOneName);
        playerTwoLabelName.setText(R.string.computer);
        aiGame.getHumanPlayer().setName(playerOneName);
        aiGame.getAIPlayer().setName( (String) playerTwoLabelName.getText());
        turn.setText(R.string.turn1);
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
        final Intent mainMenu = new Intent(this, MainActivity.class);
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
                //Creates 4 buttons objects linked to the buttons on the settings menu
                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                Button btnMain = (Button) popupView.findViewById(R.id.main);
                Button btnConnect = (Button) popupView.findViewById(R.id.connect);
                Button btnHost = (Button) popupView.findViewById(R.id.host);
                //Hides the Connect and Host button so the can't be pressed
                btnConnect.setVisibility(View.GONE);
                btnHost.setVisibility(View.GONE);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                //Creates onClickListener that closes the aiGame and returns to the main menu
                btnMain.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(mainMenu);
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
        optionpane.setTitle(R.string.goback);
        optionpane.setMessage(R.string.gobackmessage).setCancelable(true);
        optionpane.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        optionpane.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = optionpane.create();
        alertDialog.show();
    }


    public void back() {
        finish();
    }

    //set the high score for the least time a aiGame has taken to complete
    public void setShortestPlayedTime(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String temp = sp.getString("times", "");
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

    /**
     *fills the array with Player Cups and Pocket Cups and sets there ids
     * @return Button Array
     */
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

    /**
     * returns Buttons
     * @return Button Array
     */
    public Button[] getButtons(){
        return buttons;
    }

    //enables all buttons on game start
    private void enableAllButtons() {
        for (int i = 0; i < 7; i++) {
            buttons[i].setEnabled(true);
        }
        for (int i = 7; i < 16; i++) {
            buttons[i].setEnabled(false);
            buttons[i].setTextColor(Color.DKGRAY);
        }
    }

    //Initialize onClickListener and update the view of all the buttons on board
    private void initializeButtons(){
        for (final Button b : buttons) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playClickSound();
                    if(aiGame.isFirstTurn()){
                        aiGame.setFirstHumanMove(b.getId());
                        swapEnabledButtonsOnTurnChange();
                        firstAIMove();
                    }
                    else{
                        int marbles = aiGame.getBoardCups()[b.getId()].getMarbles();
                        activateAnimation(b.getId(), marbles);
                        aiGame.pressCup(b.getId());
                        swapEnabledButtonsOnTurnChange();
                        updateBoardView();
                        if(aiGame.isGameFinished()){
                            updateScores();
                            popUpGameFinished();
                        }
                        //if AIs turn it will call the aiMove method
                        if(aiGame.getAIPlayer().getTurn()){
                            aiMove(marbles);
                        }
                    }
                }
            });
        }
    }

    //games end screen
    private void popUpGameFinished() {
        String[] finalResults = aiGame.getFinalResults();
        String message;
        if(aiGame.isDraw()){
            message = "Draw!\n" + finalResults[0] + ": " + finalResults[1] + "\n" + finalResults[2] + ": " + finalResults[3];
        }
        else{
            message = "Winner: " + finalResults[0] + ": " + finalResults[1] + "\nLoser: " + finalResults[2] + ": " + finalResults[3];
        }
        AlertDialog.Builder optionpane = new AlertDialog.Builder(this);
        Intent mainMenu = new Intent(this, MainActivity.class);
        Intent newAIGame = new Intent(this, AIGameActivity.class);
        optionpane.setTitle("Game Finished");
        optionpane.setMessage("Winner: " + finalResults[0] + ": " + finalResults[1] + "\nLoser: " + finalResults[2] + ": " + finalResults[3] ).setCancelable(false);
        optionpane.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        optionpane.setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                recreate();
            }
        });

        AlertDialog alertDialog = optionpane.create();
        alertDialog.show();
    }

    //decides the AI's first move
    private void firstAIMove(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int humanMove = aiGame.getFirstMoveID();
                int aiMove = aiGame.generateFirstAIMove();
                aiGame.applyFirstTurnChanges(humanMove, aiMove);
                activateAnimation(humanMove, 7);
                activateAnimation(aiMove, 7);
                swapEnabledButtonsOnTurnChange();
                updateBoardView();
            }
        }, 1500);
    }

    /**
     * AI logic which decides which move he should make
     * @param marbles
     */
    private void aiMove(int marbles) {
        //forces the AI to wait until the previous animation is completed
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    aiGame.doMove();
                    activateAnimation(aiGame.returnFirstButton(), aiGame.returnMarbles());
                    aiGame.switchTurnsAfterAITurn(aiGame.returnFirstButton(), aiGame.returnMarbles());
                    // Do something after 2s = 2000ms
                    swapEnabledButtonsOnTurnChange();
                    updateBoardView();
                    if(aiGame.isGameFinished()){
                        updateScores();
                        popUpGameFinished();
                    }
                    if(aiGame.getAIPlayer().getTurn()){
                        aiMove(aiGame.returnMarbles());
                    }
                }
            }, (marbles * 250)+1000);

        }

    /**
     *activate the animation so there is a visual feedback in the game
     * @param idCurrentCup
     * @param marbles
     */
    public void activateAnimation(int idCurrentCup, int marbles) {
        int nextCup = idCurrentCup + 1;
        for (int i = 0; i < marbles; i++) {
            if (nextCup == 15 && aiGame.getHumanPlayer().getTurn()){
                nextCup = 0;
            }
            if (nextCup == 7 && aiGame.getAIPlayer().getTurn()){
                nextCup += 1;
            }
            if(!aiGame.isFirstTurn()) {
                if (i == marbles - 1 && aiGame.getBoardCups()[nextCup].isEmpty()) {
                    if (aiGame.getAIPlayer().getTurn() && nextCup > 7) {
                        handler.postDelayed(new AnimationRunnable(this, buttons[14 - nextCup]), (i + 1) * 200);
                        handler.postDelayed(new AnimationRunnable(this, buttons[15]), (i + 1) * 200);
                    } else if (aiGame.getHumanPlayer().getTurn() && nextCup < 7) {
                        handler.postDelayed(new AnimationRunnable(this, buttons[nextCup + ((7 - nextCup) * 2)]), (i + 1) * 200);
                        handler.postDelayed(new AnimationRunnable(this, buttons[7]), (i + 1) * 200);
                    }
                }
                handler.postDelayed(new AnimationRunnable(this, buttons[nextCup]), i * 200);
                nextCup += 1;
                if (nextCup > 15) {
                    nextCup = 0;
                }
            }
        }
    }

    //plays sound when ever a button is pressed
    private void playClickSound(){
        MediaPlayer media = MediaPlayer.create(this, R.raw.click);
        media.start();
    }

    //enable or disable buttons on the board depending on whose turn it is
    public void swapEnabledButtonsOnTurnChange() {
        if(aiGame.getHumanPlayer().getTurn()){
            for (int i = 0; i < 7; i++) {
                if (aiGame.getBoardCups()[i].getMarbles() > 0){
                    buttons[i].setEnabled(true);
                    buttons[i].setTextColor(Color.BLACK);
                }
                else{
                    buttons[i].setEnabled(false);
                    buttons[i].setTextColor(Color.DKGRAY);
                }
            }
        }else{
            for (int i = 0; i < 7; i++) {
                buttons[i].setEnabled(false);
                buttons[i].setTextColor(Color.DKGRAY);
            }
        }
    }

    //updates the board view
    public void updateView() {
        Cup[] cups = aiGame.getBoardCups();
        int[] backgrounds = {R.drawable.pocketbackground, R.drawable.back1, R.drawable.back2, R.drawable.back3,
                R.drawable.back4, R.drawable.back5, R.drawable.back6, R.drawable.back7, R.drawable.back8};
        for (int i = 0; i < cups.length; i++) {
            int marbles = cups[i].getMarbles();
            buttons[i].setText(String.valueOf(marbles));
            if(i == 7 || i == 15){
                continue;
            }
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
        if(aiGame.getHumanPlayer().getTurn()) {
            turnText = (String) playerOneLabelName.getText();
            playerOneLabelName.setTextColor(Color.GREEN);
            playerTwoLabelName.setTextColor(Color.BLACK);
        }else{
            turnText = (String) playerTwoLabelName.getText();
            playerTwoLabelName.setTextColor(Color.GREEN);
            playerOneLabelName.setTextColor(Color.BLACK);
        }
        turn.setText(String.format("%s's turn", turnText));
        turn.setTextColor(Color.GREEN);
    }

    //This method edits the highscores after the aiGame
    public void updateScores() {
        Integer score;
        if(aiGame.checkWinner().equals(aiGame.getPlayer1())) {
            score = aiGame.getBoard().getPlayerCup1Marbles();
        } else {
            score = aiGame.getBoard().getPlayerCup2Marbles();
        }
        Integer one = Preferences.fromPreferences(this.getBaseContext(), -1, "first", "your_prefs");
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