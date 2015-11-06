package uk.co.ivaylokhr.crawl;

/**
 * Created by Federico on 06/11/15.
 */
public class MyMath {

    public static boolean isPrime (long number) {
        if(number < 2) {
            return false;
        } else if (number == 2) {
            return true;
        }

        for(long i=3; i<Math.sqrt(number)+1; i=i+2) {
            if((number % i) == 0) {
                return false;
            }
        }
        return true;
    }
}
