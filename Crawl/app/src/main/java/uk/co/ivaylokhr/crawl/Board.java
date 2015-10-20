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
        // everything %15 to stay in the range
        for(int i = id + 1; i <= id + marbleEmptiedCup; i++) {

            //condition for when the cup is the playerCup
            if(i == 7) {
                if(player1.getTurn()) {
                    //SHOULD I MODIFY THE player1.playerCup or the cup in the array??
                    player1.increaseScore(1);
                    //PlayerCup player1Cup = (PlayerCup)cups[i];
                    //player1Cup.addMarbles(1);
                    continue; //finish current iteration on this point and to go to next iteration
                }
                else {
                    i++; //jumps current cup and goes to next one
                }
            }
            else if(i == 15) {
                if(player2.getTurn()) {
                    //SHOULD I MODIFY THE player2.playerCup or the cup in the array??
                    player2.increaseScore(1);
                    //PlayerCup player2Cup = (PlayerCup)cups[i];
                    //player2Cup.addMarbles(1);
                    continue;
                }
                else {
                    i++;
                }
            }

            PocketCup nextPocketCup = (PocketCup) cups[i];

            //check the last one
            if(i == id+marbleEmptiedCup && nextPocketCup.isEmpty() ) {
                PocketCup oppositeCup ;

                //get the opposite cup hard-wired way
                switch (i) {
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
                    case 7: oppositeCup = (PocketCup)cups[7] ;
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





            //AT THE END OF THE FOR LOOP USE THE MODULUS 14 MAYBE
        }//END OF FOR LOOP



    }
}
