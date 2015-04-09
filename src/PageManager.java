import java.util.Arrays;
import java.util.Random;

/**
 * Aaron Rice
 * rice2007
 * page-replacement
 */
public class PageManager {

    private static final int PAGE_UPPER_BOUND = 20; //100
    private static final int FREQUENT_PAGE_UPPER_BOUND = 4; //20
    private static final int NONFREQUENT_PAGE_OFFSET = 17; //79
    private static final int PAGE_LOWER_BOUND = 0;

    private static final int RES_SET_SIZE = 5;
    private static final int STREAM_SIZE = 20;
    private static final int LOOK_AHEAD_LENGTH = 5;

    private int[] resSet;
    private int[] lookAheadIndex;
    private int setCapacity;
    private int setSentinel;

    public PageManager(int setSize) {
        this.resSet = new int[setSize];
        this.lookAheadIndex = new int[setSize];
        for (int i = 0; i < setSize; i++) {
            this.resSet[i] = Integer.MIN_VALUE;
            this.lookAheadIndex[i] = Integer.MAX_VALUE;
        }
        this.setCapacity = 0;
        this.setSentinel = 0;
    }

    public void addPage() {

    }

    public boolean contains(int value) {
        boolean test = false;
        for (int i = 0; i < this.resSet.length; i++) {
            if (this.resSet[i] == value) {
                test = true;
                break;
            }
        }
        return test;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.resSet.length; i++) {
            sb.append("[" + this.resSet[i] + "]\n");
        }
        return sb.toString();
    }
    
    private int lookAhead(int[] pageStream, int streamSentinel) {
        int minIndex = Integer.MAX_VALUE;
        for (int i = 0; i < 5; i++) {
            for (int j = streamSentinel; j < streamSentinel + LOOK_AHEAD_LENGTH; j++) {
                try {
                    if (this.resSet[i] == pageStream[j]) {
                        this.lookAheadIndex[i] = j - streamSentinel;
                        break;
                    } else {
                        this.lookAheadIndex[i] = Integer.MAX_VALUE;
                    }
                } catch (Exception e) {
                    break;
                }
            }
            if (this.lookAheadIndex[i] < minIndex) {
                minIndex = i;
            }
            this.lookAheadIndex[i] = Integer.MAX_VALUE;
        }
        return minIndex;
    }

    /**
     * Generates a random page using the 80-20 rule.
     *
     * Generates a random number from 0 to 99 (inclusive). Generates a page from 0 to 19 (inclusive) if random number
     * is in the frequent range and from 20-99 (inclusive) if random number is out of the frequent range.
     *
     *  @return the page number generated
     */
    private static int generateStream() {
        Random rand = new Random();
        return rand.nextInt(PAGE_UPPER_BOUND) <= NONFREQUENT_PAGE_OFFSET ?
                rand.nextInt(FREQUENT_PAGE_UPPER_BOUND) :
                rand.nextInt(NONFREQUENT_PAGE_OFFSET) + FREQUENT_PAGE_UPPER_BOUND;
    }
    public static void main(String[] args) {
        PageManager manager = new PageManager(RES_SET_SIZE);
        int[] pageStream = new int[STREAM_SIZE];
        for (int i = 0; i < STREAM_SIZE; i++) {
            pageStream[i] = generateStream();
        }
        System.out.println(Arrays.toString(pageStream));

        for (int i = 0; i < STREAM_SIZE; i++) {
            if (manager.setCapacity < RES_SET_SIZE) {
                //
                if (manager.contains(pageStream[i])) {
                    System.out.println(manager.toString());
                    continue;
                } else {
                    manager.resSet[manager.setSentinel] = pageStream[i];
                    manager.setSentinel++;
                    manager.setCapacity++;
                }
            } else {
                if (manager.contains(pageStream[i])) {
                    System.out.println(manager.toString());
                    continue;
                } else {
                    System.out.println("Page fault!");
                    int replacementIndex = manager.lookAhead(pageStream, i + 1);
                    if (replacementIndex < Integer.MAX_VALUE) {
                        manager.resSet[replacementIndex] = pageStream[i];
                    } else {
                        manager.resSet[0] = pageStream[i];
                    }
                }
            }
            System.out.println(manager.toString());
        }
    }

}
