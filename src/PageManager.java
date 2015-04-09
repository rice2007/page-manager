import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Aaron Rice
 * rice2007
 * page-replacement
 */
public class PageManager {

    private static final int PAGE_UPPER_BOUND = 100;
    private static final int FREQUENT_PAGE_UPPER_BOUND = 20;
    private static final int PAGE_LOWER_BOUND = 0;


    /**
     * Generates a random page using the 80-20 rule.
     *
     * Generates a random number from 0 to 99 (inclusive). Generates a page from 0 to 19 (inclusive) if random number
     * is in the frequent range and from 20-99 (inclusive) if random number is out of the frequent range.
     *
     *  @return the page number generated
     */
    private static int generatePage() {
        Random rand = new Random();
        return rand.nextInt(PAGE_UPPER_BOUND) <= 79
                ? rand.nextInt(FREQUENT_PAGE_UPPER_BOUND)
                : rand.nextInt(79) + FREQUENT_PAGE_UPPER_BOUND;
    }
    public static void main(String[] args) {
        int[] resSet = new int[5];
        int[] pageStream = new int[20];
        for (int i = 0; i < 20; i++) {
            pageStream[i] = generatePage();
        }
        System.out.print(Arrays.toString(pageStream));
    }

}
