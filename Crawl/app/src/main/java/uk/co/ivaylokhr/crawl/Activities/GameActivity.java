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
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.Toast;

import uk.co.ivaylokhr.crawl.Bluetooth.BluetoothChatFragment;
import uk.co.ivaylokhr.crawl.Controller.AnimationRunnable;
import uk.co.ivaylokhr.crawl.Controller.Game;
import uk.co.ivaylokhr.crawl.Controller.GoToActivityListener;
import uk.co.ivaylokhr.crawl.Model.Cup;
import uk.co.ivaylokhr.crawl.Model.Preferences;
import uk.co.ivaylokhr.crawl.R;

public class GameActivity extends AppCompatActivity {

    private Button[] buttons;
    private Game game ;
    private ImageButton imgButton;
    private TextView timer;
    private long startTime;
    private long timeCounter=0;
    private Handler handler = new Handler();
    private BluetoothChatFragment fragment;
    private android.os.Handler bluetoothHandler;
    private TextView turn;
    private TextView playerOneLabelName;
    private TextView bluetoothPressed;
    private TextView playerTwoLabelName;
    private boolean arePlayerOne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        bluetoothPressed = (TextView) findViewById(R.id.textView18);
        initBluetooth();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new BluetoothChatFragment();
            bluetoothHandler = fragment.returnHandler();
            transaction.replace(R.id.sample_content_fragment, fragment);
            fragment.addText(bluetoothPressed);
            arePlayerOne = false;
            transaction.commit();
        }
        initialiseGame();
        arePlayerOne = false;
        buttons = fillButtonsArray();
        initializeButtons();
        increaseGamesPlayed();
        enableAllButtons();
        addPlayerNames(true);
        updateView();
        settings();

    }
    public void sendMessage(String cup){

        if(fragment.getState()) {
            fragment.sendMessage(cup);
        }
    }

    private void initialiseGame() {
        game = new Game();
        turn = (TextView) findViewById(R.id.turn);
        timer = (TextView) findViewById(R.id.Timer);
        bluetoothPressed.setText("");
        playerOneLabelName = (TextView) findViewById(R.id.player1);
        playerTwoLabelName = (TextView) findViewById(R.id.player2);
        startTime = System.currentTimeMillis();
        handler.postDelayed(updateTimer, 0);
    }


    private void addPlayerNames(boolean firstGame){
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
        if(firstGame = true) {
            playerOneLabelName.setText(playerOneName);
            playerTwoLabelName.setText(playerTwoName);
        }
        game.getPlayer1().setName(playerOneName);
        game.getPlayer2().setName(playerTwoName);
        turn.setText(R.string.turn1);
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
                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                Button btnMain = (Button) popupView.findViewById(R.id.main);
                Button btnConnect = (Button) popupView.findViewById(R.id.connect);
                Button btnHost = (Button) popupView.findViewById(R.id.host);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                //Creates onClickListener that closes the game and returns to the main menu
                btnMain.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(mainMenu);
                    }
                });
                btnConnect.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        fragment.connectBluetooth();
                        popupWindow.dismiss();
                    }
                });
                btnHost.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        fragment.discoverable();
                        popupWindow.dismiss();
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
            String time = String.format("%02d:%02d", minutes, seconds);
            timer.setText(time);
            handler.postDelayed(this, 0);
        }};

    //warn the player that going back will end his game
    @Override
    public void onBackPressed() {
        AlertDialog.Builder optionpane = new AlertDialog.Builder(this);
        Intent mainMenu = new Intent(this, MainActivity.class);
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

    //set the high score for the least time a game has taken to complete
    private void setShortestPlayedTime(){
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


    public void initBluetooth() {
        bluetoothPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothPressed.getText().equals("")){
                    return;
                }
                if(bluetoothPressed.getText().equals("yes")){
                    arePlayerOne = true;
                    startNewGame();
                    return;
                }
                if(bluetoothPressed.getText().equals("no")){
                    arePlayerOne = false;
                    startNewGame();
                    return;
                }
                if(bluetoothPressed.getText().equals("start")){
                    startBluetooth();
                    startName();
                    sendMessage("go");
                    return;
                }
                if(bluetoothPressed.getText().equals("go")){
                    startBluetooth();
                    startName();
                    return;
                }
        Button b = buttons[Integer.parseInt(String.valueOf(bluetoothPressed.getText()))];
        int marbles = game.getBoardCups()[b.getId()].getMarbles();
        if(!game.isFirstTurn()){
            activateAnimation(b.getId(), marbles);
        }
        game.pressCup(b.getId());
        playClickSound();
        swapEnabledButtonsOnTurnChange();
        updateBoardView();
        if(game.isGameFinished()){
            updateScores();
            popUpGameFinished();
        }
            }


        });
    }

    private void startName() {
        if(arePlayerOne) {
            game.getPlayer2().setName("Opponent");
            playerTwoLabelName.setText("Opponent");
        }else{
            game.getPlayer1().setName("Opponent");
            playerOneLabelName.setText("Opponent");
        }
    }


    private void startBluetooth() {
        for (int i = 0; i < 7; i++) {
            if(!arePlayerOne){
                buttons[i].setEnabled(false);
                buttons[i].setTextColor(Color.BLACK);
            }
        }
        for (int i = 8; i < 15; i++) {
            if(arePlayerOne){
                buttons[i].setEnabled(false);
                buttons[i].setTextColor(Color.BLACK);
            }
        }
        startTime = System.currentTimeMillis();
    }

    //This is for the dialog. It goes to the main menu if you say you want to
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
        for (int i = 0; i < buttons.length; i++) {
            //ignore the player cups
            if (i == 7 || i == 15) {
                buttons[i].setEnabled(false);
                buttons[i].setTextColor(Color.DKGRAY);
            } else {
                buttons[i].setEnabled(true);
                buttons[i].setTextColor(Color.BLACK);
            }

        }

    }

    //Initialize onClickListener and update the view of all the buttons on board
    private void initializeButtons(){
        for (final Button b : buttons) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (game.isFirstTurn()){
                        game.firstTurnPlay(b.getId());
                        swapEnabledButtonsOnTurnChange();
                        updateBoardView();
                    }
                    game.pressCup(b.getId());
                    sendMessage(String.valueOf(b.getId()));
                    playClickSound();
                    swapEnabledButtonsOnTurnChange();
                    updateBoardView();
                    if(game.isGameFinished()){
                        popUpGameFinished();
                        updateScores();
                    }
                }
            });

        }
    }

    private void popUpGameFinished() {
        String[] finalResults = game.getFinalResults();
        String message;
        if(game.isDraw()){
            message = "Draw!\n" + finalResults[0] + ": " + finalResults[1] + "\n" + finalResults[2] + ": " + finalResults[3];
        }
        else{
            message = "Winner: " + finalResults[0] + ": " + finalResults[1] + "\nLoser: " + finalResults[2] + ": " + finalResults[3];
        }
        AlertDialog.Builder optionpane = new AlertDialog.Builder(this);
        optionpane.setTitle("Game Finished");

        optionpane.setMessage("Winner: " + finalResults[0] + ": " + finalResults[1] + "\nLoser: " + finalResults[2] + ": " + finalResults[3] ).setCancelable(false)
                .setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        optionpane.setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startNewGame();
            }
        });

        AlertDialog alertDialog = optionpane.create();
        alertDialog.show();
    }

    private void startNewGame() {
        //starts a new game that will stay bluetooth connected
        initialiseGame();
        increaseGamesPlayed();
        addPlayerNames(false);
        enableAllButtons();
        updateView();
        if(fragment.getState()) {
            startName();
        }
        startBluetooth();
        playerTwoLabelName.setTextColor(Color.BLACK);
        playerOneLabelName.setTextColor(Color.BLACK);
    }

    private void activateAnimation(int idCurrentCup, int marbles) {
        int nextCup = idCurrentCup + 1;
        for (int i = 0; i < marbles; i++) {
            if (nextCup == 15 && game.getPlayer1().getTurn()){
                nextCup = 0;
            }
            if (nextCup == 7 && game.getPlayer2().getTurn()){
                nextCup += 1;
            }
            Button toAnimate = buttons[nextCup];
            handler.postDelayed(new AnimationRunnable(this, toAnimate), i*200);
            if(!game.isFirstTurn()){
                if(i == marbles - 1 && game.getBoardCups()[nextCup].isEmpty()){
                    if(game.getPlayer1().getTurn() && nextCup < 7){
                        handler.postDelayed(new AnimationRunnable(this, buttons[nextCup+((7-nextCup)*2)]), (i+1)*200);
                        handler.postDelayed(new AnimationRunnable(this, buttons[7]), (i+1)*200);
                    }
                    else if(game.getPlayer2().getTurn() && nextCup > 7){
                        handler.postDelayed(new AnimationRunnable(this, buttons[14-nextCup]), (i+1)*200);
                        handler.postDelayed(new AnimationRunnable(this, buttons[15]), (i+1)*200);
                    }
                }
            }
            nextCup += 1;
            if (nextCup > 15){
                nextCup = 0;
            }
        }
    }

    private void playClickSound(){
        MediaPlayer media = MediaPlayer.create(this, R.raw.click);
        media.start();
    }

    private void swapEnabledButtonsOnTurnChange() {
        Log.i("tag","che4");
        if(fragment.getState()){
            Log.i("tag","che3");
            if (game.getPlayer1().getTurn()) {
                for (int i = 0; i < 7; i++) {
                    Log.i("tag","che2");
                    if (game.getBoardCups()[i].getMarbles() == 0) {
                        buttons[i].setEnabled(false);
                        buttons[i].setTextColor(Color.DKGRAY);
                    } else {
                        if(arePlayerOne) {
                            buttons[i].setEnabled(true);
                        }
                        buttons[i].setTextColor(Color.BLACK);
                    }
                }
                for (int i = 8; i < 15; i++) {
                    buttons[i].setEnabled(false);
                    buttons[i].setTextColor(Color.DKGRAY);
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    Log.i("tag","che1");
                    buttons[i].setEnabled(false);
                    buttons[i].setTextColor(Color.DKGRAY);
                }

                for (int i = 8; i < 15; i++) {
                    if (game.getBoardCups()[i].getMarbles() == 0) {
                        buttons[i].setEnabled(false);
                        buttons[i].setTextColor(Color.DKGRAY);
                    } else {
                        if(!arePlayerOne) {
                            buttons[i].setEnabled(true);
                        }
                        buttons[i].setTextColor(Color.BLACK);
                    }
                }
            }
        }else {
            if (game.getPlayer1().getTurn()) {
                for (int i = 0; i < 7; i++) {
                    if (game.getBoardCups()[i].getMarbles() == 0) {
                        buttons[i].setEnabled(false);
                        buttons[i].setTextColor(Color.DKGRAY);
                    } else {
                            buttons[i].setEnabled(true);
                            buttons[i].setTextColor(Color.BLACK);
                    }
                }
                for (int i = 8; i < 15; i++) {
                    buttons[i].setEnabled(false);
                    buttons[i].setTextColor(Color.DKGRAY);
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    buttons[i].setEnabled(false);
                    buttons[i].setTextColor(Color.DKGRAY);
                }

                for (int i = 8; i < 15; i++) {
                    if (game.getBoardCups()[i].getMarbles() == 0) {
                        buttons[i].setEnabled(false);
                        buttons[i].setTextColor(Color.DKGRAY);
                    } else {
                        buttons[i].setEnabled(true);
                        buttons[i].setTextColor(Color.BLACK);
                    }
                }
            }
        }
    }

    public void updateView() {
        Cup[] cups = game.getBoardCups();
        int[] backgrounds = {R.drawable.pocketbackground, R.drawable.back1, R.drawable.back2, R.drawable.back3,
                R.drawable.back4, R.drawable.back5, R.drawable.back6, R.drawable.back7, R.drawable.back8};
        for (int i = 0; i < cups.length; i++) {
            int marbles = cups[i].getMarbles();
            buttons[i].setText(String.valueOf(marbles));
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
        if(game.getPlayer1().getTurn()) {
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

    //This method edits the highscores after the game
    public void updateScores() {
        Integer score ;
        if(game.checkWinner().equals(game.getPlayer1())) {
            score = game.getBoard().getPlayerCup1Marbles();
        } else {
            score = game.getBoard().getPlayerCup2Marbles();
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
