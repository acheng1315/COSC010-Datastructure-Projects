import java.util.Comparator;

/**
 * @author Andrew Cheng and Noel(Sangha) Jang
 * Class that compares 2 trees and returns specific ints based on the comparisons.
 */
public class TreeComparator implements Comparator<BinaryTree<TreeCreationData>> {
    /**
     * compares two trees and their frequencies.
     * @param T1
     * @param T2
     * @return an int
     */
    public int compare(BinaryTree<TreeCreationData> T1, BinaryTree<TreeCreationData> T2) {
        if (T1.getData().getFrequency() < T2.getData().getFrequency()) {
            return -1;
        }
        else if(T1.getData().getFrequency() == T2.getData().getFrequency()) {
            return 0;
        }
        return 1;
    }

}
