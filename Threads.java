public class Threads {
    public static void main(String[] a) {
        for (int i = 1; i <= 10; i++) {
            MyThread mt = new MyThread(i);
            mt.run();
        }
    }
}

// one way to use
class MyThread extends Thread {
    int threadNum;

    public MyThread(int i) {
        this.threadNum = i;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            System.out.println("running the thread no. : " + this.threadNum);
        } catch (Exception e) { }
    }
}
