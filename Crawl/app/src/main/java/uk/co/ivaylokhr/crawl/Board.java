package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.jar.Attributes;


public class Board {
    private Cup[] cups;
    private Player player1, player2;
    private boolean isFinished;
    private String winner;


    public Board(Cup[] cups){
        this.cups = cups;
        player1 = new Player((PlayerCup) cups[7]);
        player2 = new Player((PlayerCup) cups[15]);
        isFinished = false;
        player1.setTurn(true);
        player2.setTurn(false);

        for(Cup c :cups){
            c.setText(Integer.toString(c.getMarbles()));
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressCup(v);
                }
            });
        }
    }

    public boolean isFinished(){
        return isFinished;
    }


    public void pressCup(View view) {
        //1. get id of the pressed cup
        int id = ((PocketCup)view).getId();

        //empty cup
        PocketCup pressedPocketCup = (PocketCup)cups[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        //at this point please check if the cup in the array still has the marbles
        Log.i("Pressed Cup", "New move");
        putMarblesInNextCups(id, marblesFromEmptiedCup);

        if(player1.getTurn()){
            player2.setTurn(true);
            player1.setTurn(false);
        } else {
            player2.setTurn(false);
            player1.setTurn(true);
        }

        if(isGameFinished()) {
            winner = checkWinner();
        }

        updateButtonText();
    }

    public void updateButtonText(){
        for(Cup c :cups){
            c.setText(Integer.toString(c.getMarbles()));
        }
    }

    
    public void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
        int cupNumber = idCurrentCup + 1;
        for(int i = 0; i < marblesFromEmptiedCup; i++) {
            Log.i("Cup id:", Integer.toString(cupNumber));
            Log.i("Player's 1 turn:", Boolean.toString(player1.getTurn()));
            Log.i("Player's 2 turn:", Boolean.toString(player2.getTurn()));
            //condition for when the cup is the playerCup
            if(cupNumber == 7) {
                if(player1.getTurn()) {
                    //SHOULD I MODIFY THE player1.playerCup or the cup in the array??
                    player1.increaseScore(1);
                    //PlayerCup player1Cup = (PlayerCup)cups[i];
                    //player1Cup.addMarbles(1);
                    cupNumber++; //jumps PlayerCup and goes to next one
                }
                continue; //finish current iteration on this point and to go to next iteration
            }
            else if(cupNumber == 15) {
                if(player2.getTurn()) {
                    //SHOULD I MODIFY THE player2.playerCup or the cup in the array??
                    player2.increaseScore(1);
                    Log.i("player 2 score:", Integer.toString(player2.getScore()));
                    //PlayerCup player2Cup = (PlayerCup)cups[i];
                    //player2Cup.addMarbles(1);
                    cupNumber =0;
                }
                continue; //finish current iteration on this point and to go to next iteration
            }

            PocketCup nextPocketCup = (PocketCup) cups[cupNumber];

            //check at the last iteration if cup is empty
            if(i == marblesFromEmptiedCup - 1 && nextPocketCup.isEmpty() ) {
                PocketCup oppositeCup ;
                int marblesFromOppositeCup=0; //Had to initialize this :/
                if(player1.getTurn()) {
                    oppositeCup = (PocketCup) cups[cupNumber + ((7 - cupNumber) * 2)];
                    //..or [(cupNumber -14)*-1]
                    marblesFromOppositeCup = oppositeCup.emptyCup();
                }
                else if(player2.getTurn()) {
                    oppositeCup = (PocketCup) cups[(14-cupNumber)];
                    marblesFromOppositeCup = oppositeCup.emptyCup();
                }


                if(player1.getTurn()) {
                    player1.increaseScore(marblesFromOppositeCup);
                }
                else {
                    player2.increaseScore(marblesFromOppositeCup);
                }

            }

            nextPocketCup.addMarbles(1);

            //update id for the next cup, and stay in the range of 15.
            if(cupNumber>=15) {
                cupNumber = 0;
            }
            else{
                cupNumber++;
            }


        }//END OF FOR LOOP
    }


    public boolean isGameFinished() {
        //check if game is finished
        for(int i=0; i<cups.length; i++) {
            if(i != 7 && i != 15) {
                PocketCup pocketCup = (PocketCup) cups[i];
                if (!pocketCup.isEmpty()) {
                    break;
                }
            }

            if(i==14) {
                return true;
            }

        }

        if(player1.getScore() > 49 || player2.getScore() > 49) {
            return true;
        }

        return false;
    }


    public String checkWinner() {
        if(player1.getScore() > player2.getScore()) {
            return player1.getName();
        }
        return player2.getName();
    }
}
