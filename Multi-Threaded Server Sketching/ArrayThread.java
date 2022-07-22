/**
 * @author Andrew Cheng
 * array thread sum class that has left thread calc sum of first half of array and right thread calc second half
 * of the array to find the total sum.
 */
public class ArrayThread extends Thread {
    // instance variables
    private static int totalSum;
    private static int[] list = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private int start;
    private int end;

    // constructor for the start/end of the list
    public ArrayThread(int start, int end) {
        this.start = start;
        this.end = end;
    }
    // run method for the threads
    public void run() {
        for (int i = start; i <= end; i++) {
            totalSum += list[i];
        }
    }
    // main method that calls the two threads
    public static void main(String[] args) throws Exception {

        int middleIndex = list.length / 2;
        Thread t1 = new Thread(new ArrayThread(0, middleIndex));
        Thread t2 = new Thread(new ArrayThread(middleIndex + 1, list.length - 1));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.print(totalSum);

    }
}






