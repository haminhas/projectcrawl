package uk.co.ivaylokhr.crawl.Model;


public class Cup {

    protected int marbles;

    public Cup() {
        marbles = 0;
    }

    public int getMarbles(){
        return marbles;
    }

    public void addMarbles(int x){
        marbles += x;
    }

    public boolean isEmpty(){
        return marbles == 0;
    }


}
