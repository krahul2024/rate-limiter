import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

    public static void main(String[] a) throws InterruptedException {
        RateLimiter rl = new RateLimiter();
        String key = "user";

        System.out.println("Sending Req...");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Request " + i + " -> " + rl.allow(key));
        }

        System.out.println("Sleeping...");
        Thread.sleep(5000);

        System.out.println("After time window:");
        for (int i = 1; i <= 3; i++) {
            System.out.println("Request " + i + " -> " + rl.allow(key));
        }
    }

    final int maxReq = 3;
    final long windowTimeMillis = 5_000;

    Map<String, Window> data = new HashMap<>();

    static class Window {
        int count;
        long startTime;
    }

    boolean allow(String key) {
        long now = System.currentTimeMillis();
        Window w = data.get(key);

        // if it doesn't exist
        if (w == null) {
            w = new Window();
            w.count = 1;
            w.startTime = now;

            data.put(key, w);
            return true;
        }

        if (now - w.startTime >= windowTimeMillis) {
            w.count = 1;
            w.startTime = now;
            return true;
        }

        if (w.count < maxReq) {
            w.count++;
            return true;
        }

        return false;
    }
}
