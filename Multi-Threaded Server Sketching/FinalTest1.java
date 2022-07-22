public class FinalTest1 extends Thread {
    private static int total = 0;
    private static final int times = 1000000;

    public void run() {
        for (int i = 0; i < times; i++) {
            total--;
        }
    }

    public static void main(String [] args) throws Exception {
        FinalTest1 t1 = new FinalTest1();
        FinalTest1 t2 = new FinalTest1();

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("total at end = " + total);
    }
}