import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PageManager {

    private static final int PAGE_UPPER_BOUND = 100;
    private static final int FREQUENT_PAGE_UPPER_BOUND = 11;
    private static final int NONFREQUENT_PAGE_OFFSET = 89;

    private static final int RES_SET_SIZE = 12;
    private static final int STREAM_SIZE = 20000;

    private ArrayList<Integer> resSet;
    private ArrayList<Integer> lookAheadIndex;
    private int setCapacity;
    private int setSentinel;

    public PageManager(int setSize) {
        this.resSet = new ArrayList<>(setSize);
        this.lookAheadIndex = new ArrayList<>(setSize);
        for (int i = 0; i < setSize; i++) {
            this.resSet.add(Integer.MIN_VALUE);
            this.lookAheadIndex.add(Integer.MAX_VALUE);
        }
        this.setCapacity = 0;
        this.setSentinel = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.resSet.size(); i++) {
            sb.append("[" + this.resSet.get(i) + "]");
        }
        sb.append("\n");
        return sb.toString();
    }

    private int lookAhead(List<Integer> pageStream) {
        int maxIndex = 0;
        for (int i = 0; i < this.resSet.size(); i++) {
            lookAheadIndex.set(i, pageStream.indexOf(this.resSet.get(i)));
            if (lookAheadIndex.get(i) == -1) {
                return i;
            } else if (lookAheadIndex.get(i) > lookAheadIndex.get(maxIndex)) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static int generateStream() {
        Random rand = new Random();
        return rand.nextInt(PAGE_UPPER_BOUND) <= NONFREQUENT_PAGE_OFFSET ?
                rand.nextInt(FREQUENT_PAGE_UPPER_BOUND) :
                rand.nextInt(NONFREQUENT_PAGE_OFFSET) + FREQUENT_PAGE_UPPER_BOUND;
    }

    public static void main(String[] args) {
        PageManager manager = new PageManager(RES_SET_SIZE);
        ArrayList<Integer> pageStream = new ArrayList<>(STREAM_SIZE);
        int faultCounter = 0;
        for (int i = 0; i < STREAM_SIZE; i++) {
            pageStream.add(generateStream());
        }
        System.out.println(pageStream.toString());

        for (int i = 0; i < STREAM_SIZE; i++) {
            if (manager.setCapacity < RES_SET_SIZE) {
                //
                if (manager.resSet.contains(pageStream.get(i))) {
                    System.out.println(manager.toString());
                    continue;
                } else {
                    System.out.println("Page fault!");
                    manager.resSet.set(manager.setSentinel, pageStream.get(i));
                    manager.setSentinel++;
                    manager.setCapacity++;
                    faultCounter++;
                }
            } else {
                if (manager.resSet.contains(pageStream.get(i))) {
                    System.out.println(manager.toString());
                    continue;
                } else {
                    System.out.println("Page fault!");
                    faultCounter++;
                    int replacementIndex = manager.lookAhead(pageStream.subList(i, STREAM_SIZE));
                    manager.resSet.set(replacementIndex, pageStream.get(i));
                }
            }
            System.out.println(manager.toString());
        }
        System.out.println("Page faults: " + faultCounter);
        Double percent = (double) faultCounter/(double) STREAM_SIZE;
        System.out.println("Fault probability: " + percent);
    }
}
