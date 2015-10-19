package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.util.AttributeSet;

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

}
