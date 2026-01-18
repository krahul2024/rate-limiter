public class Threads {
    public static void main(String[] a) {
        // runInitialThreadExamples();
        raceConditionDemo();
        fixedRaceConditionDemo();
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

    static void runInitialThreadExamples () {
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

    static void raceConditionDemo() {
        Counter counter = new Counter();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);
        Thread t4 = new Thread(task);

        try {
            t1.start();
            t2.start();
            t3.start();
            t4.start();

            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Final Count = " + counter.getCount());
    }

    static void fixedRaceConditionDemo() {
        Counter c = new Counter();

        Runnable task = () -> {
            for (int i = 1; i <= 1000; i++) {
                c.incrementSync();
            }
        };

        int nThreads = 4;
        Thread[] threads = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        try {
            for (int i = 0; i < nThreads; i++) {
                threads[i].join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Final Count(Expected = 4000) with sync = " + c.getCountSync());
    }
}

class Counter {
    private int count = 0;

    public void increment() {
        count++; // this is three step op -> read count, add 1 to count, write back to count
    }

    // NOTE: couple more ways
    // AtomicInteger, LongAdder
    // Locks, Semaphores
    // ReadWriteLock
    //
    // synchronized -> built-in mutexes
    // ReentrantLock -> cpp std::mutex

    public synchronized void incrementSync() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public synchronized int getCountSync() {
        return count;
    }
}

//----------------------------- Basic Tutorial on usage of thread/runnable ------------------------------

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
