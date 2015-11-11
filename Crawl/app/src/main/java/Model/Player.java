package Model;


public class Player {
    private String name ;
    private boolean turn ;

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
