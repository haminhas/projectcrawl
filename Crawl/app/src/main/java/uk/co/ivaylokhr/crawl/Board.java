package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by Hassan on 19/10/2015.
 */
public class Board {
    private Cup[] cups;
    private Player player1, player2;
    private boolean isFinished;

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
        //get id of the pressed cup
        int id = ((PocketCup)view).getPocketCupId();

        //empty cup
        PocketCup pressedPocketCup = (PocketCup)cups[id];
        int marbleEmptiedCup = pressedPocketCup.emptyCup();
        //at this point please check if the cup in the array still has the marbles


        //put marbles in subsequent cups
        for(int i = id + 1; i <= id + marbleEmptiedCup; i++) {
            if(i == 7) {
                if(player1.getTurn()) {
                    PlayerCup player1Cup = (PlayerCup)cups[i];
                    player1Cup.addMarbles(1);
                    continue; //finish current iteration on this point and to go to next iteration
                }
                else {
                    i++; //jumps current cup and goes to next one
                }
            }
            else if(i == 15) {
                if(player2.getTurn()) {
                    PlayerCup player2Cup = (PlayerCup)cups[i];
                    player2Cup.addMarbles(1);
                    continue;
                }
                else {
                    i++;
                }
            }

            PocketCup nextPocketCup = (PocketCup) cups[i];
            nextPocketCup.addMarbles(1);
        }


    }
}
