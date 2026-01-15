public class Threads {
    public static void main(String[] a) {
        for (int i = 1; i <= 10; i++) {
            MyThread mt = new MyThread(i);
            mt.run();
        }

        for (int i = 1; i <= 2; i++) {
            Task t = new Task();
            t.run();
        }

        try {
            threadLifeCycleDemo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void threadLifeCycleDemo () throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                System.out.println("Thread running");
                Thread.sleep(2000);
                System.out.println("Thread finishing");
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        System.out.println("State: " + t.getState()); // new

        t.start();
        System.out.println("State: " + t.getState()); // runnable/running sort of

        Thread.sleep(1000);
        System.out.println("State: " + t.getState()); // timed_wait

        t.join(); // waiting for thread completion
        System.out.println("State: " + t.getState()); // termination
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

//NOTE: Another way, preferred
//
// since there is no way to do multiple inheritance,
// so if a class already extends another class, it is
// not possible to extend class Thread, it is better to
// implement runnable interface alongside any interface or class

//NOTE: Thread life cycle
// new -> created but not started
// runnable -> thread is ready to run or is running
// blocked -> blocked either by locks or some way
// waiting -> waiting for another thread
// timed_waiting
// terminated

class Task implements Runnable {
    @Override
    public void run() {
         for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ", count = " + i);
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
