package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.util.AttributeSet;
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
    }

    public boolean isFinished(){
        return isFinished;
    }


    public void pressCup(View view) {
        //1. get id of the pressed cup
        int id = ((PocketCup)view).getPocketCupId();

        //empty cup
        PocketCup pressedPocketCup = (PocketCup)cups[id];
        int marblesFromEmptiedCup = pressedPocketCup.emptyCup();
        //at this point please check if the cup in the array still has the marbles

        putMarblesInNextCups(id, marblesFromEmptiedCup);

        if(isGameFinished()) {
            winner = checkWinner();
        }
    }

    
    public void putMarblesInNextCups(int idCurrentCup, int marblesFromEmptiedCup) {
        int cupNumber = idCurrentCup + 1;
        for(int i = 0; i < marblesFromEmptiedCup; i++) {

            //condition for when the cup is the playerCup
            if(cupNumber == 7) {
                if(player1.getTurn()) {
                    //SHOULD I MODIFY THE player1.playerCup or the cup in the array??
                    player1.increaseScore(1);
                    //PlayerCup player1Cup = (PlayerCup)cups[i];
                    //player1Cup.addMarbles(1);
                    continue; //finish current iteration on this point and to go to next iteration
                }
                else {
                    cupNumber++; //jumps PlayerCup and goes to next one
                }
            }
            else if(cupNumber == 15) {
                if(player2.getTurn()) {
                    //SHOULD I MODIFY THE player2.playerCup or the cup in the array??
                    player2.increaseScore(1);
                    //PlayerCup player2Cup = (PlayerCup)cups[i];
                    //player2Cup.addMarbles(1);
                    continue;
                }
                else {
                    cupNumber++;
                }
            }

            PocketCup nextPocketCup = (PocketCup) cups[cupNumber];

            //check at the last iteration if cup is empty
            if(i == marblesFromEmptiedCup && nextPocketCup.isEmpty() ) {
                PocketCup oppositeCup ;

                //get the opposite cup hard-wired way
                switch (cupNumber) {
                    case 0: oppositeCup = (PocketCup)cups[14] ;
                        break;
                    case 1: oppositeCup = (PocketCup)cups[13] ;
                        break;
                    case 2: oppositeCup = (PocketCup)cups[12] ;
                        break;
                    case 3: oppositeCup = (PocketCup)cups[11] ;
                        break;
                    case 4: oppositeCup = (PocketCup)cups[10] ;
                        break;
                    case 5: oppositeCup = (PocketCup)cups[9] ;
                        break;
                    case 6: oppositeCup = (PocketCup)cups[8] ;
                        break;
                    case 8: oppositeCup = (PocketCup)cups[6] ;
                        break;
                    case 9: oppositeCup = (PocketCup)cups[5] ;
                        break;
                    case 10: oppositeCup = (PocketCup)cups[4] ;
                        break;
                    case 11: oppositeCup = (PocketCup)cups[3] ;
                        break;
                    case 12: oppositeCup = (PocketCup)cups[2] ;
                        break;
                    case 13: oppositeCup = (PocketCup)cups[1] ;
                        break;
                    case 14: oppositeCup = (PocketCup)cups[0] ;
                        break;
                    default: oppositeCup = (PocketCup)cups[i];
                        break;
                }

                int marblesFromOppositeCup = oppositeCup.emptyCup();

                if(player1.getTurn()) {
                    player1.increaseScore(marblesFromOppositeCup);
                }
                else {
                    player2.increaseScore(marblesFromOppositeCup);
                }

            }

            nextPocketCup.addMarbles(1);

            //update id for the next cup, and stay in the range of 15.
            cupNumber++;
            if(cupNumber>=15) {
                cupNumber = 0;
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