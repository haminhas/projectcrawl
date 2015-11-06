
import org.junit.Test;
import uk.co.ivaylokhr.crawl.MyMath;
import static junit.framework.Assert.*;

/**
 * Created by Federico on 06/11/15.
 */
public class MyMathTest {

    @Test
    public void primeTest3() {
        assertTrue(MyMath.isPrime(3));
    }

    @Test
    public void primeTest4() {
        assertTrue(MyMath.isPrime(5));
    }

    @Test
    public void primeTest7() {
        assertTrue(MyMath.isPrime(7));
    }
}
