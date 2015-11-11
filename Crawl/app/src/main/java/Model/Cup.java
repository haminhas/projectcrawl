package Model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;


public class Cup {

    protected int marbles;

    public Cup() {
        marbles = 0;
        //marbles = 7;
    }

    public int getMarbles(){
        return marbles;
    }

    public void addMarbles(int x){
        marbles += x;
    }
}
