package uk.co.ivaylokhr.crawl;


public class Board {
    private PlayerCup playerCup1, playerCup2;
    private Cup[] cups;

    public Board() {
        playerCup1 = new PlayerCup();
        playerCup2 = new PlayerCup();
        cups = new Cup[16];
        fillCupsArray();
    }

    public Cup[] getCups(){
        return cups;
    }

    public void fillCupsArray(){
        for (int i = 0; i < cups.length; i++) {
            //ignore the player cups
            if (i == 7) {
                cups[i] = playerCup1;
            }else if(i == 15){
                cups[i] = playerCup2;
            }
            else {
                cups[i] = new PocketCup() ;
            }
        }
    }

}
