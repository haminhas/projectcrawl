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
        int id = ((PocketCup)view).getPocketCupId();

        PocketCup cup = (PocketCup)cups[id];
        int marbleEmptiedCup = cup.emptyCup();

        //at this point please check if the cup in the array still has the marbles

        for(int i=id+1; i<=marbleEmptiedCup+id; i++) {
            if(i<15) {
                cups[i].addMarbles(1);
            }
            else {
                //restart
            }
        }


    }
}
