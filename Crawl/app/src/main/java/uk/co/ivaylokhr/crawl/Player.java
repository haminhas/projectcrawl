package uk.co.ivaylokhr.crawl;

/**
 * Created by Hassan on 19/10/2015.
 */
public class Player {
    String name ;
    boolean turn ;

    public Player(){
        turn = true;
    }
    
    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }

    public String getName() { return name; }

    public void setName(String newName) { name = newName; }

}
