import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Callable<String> myCallable;
        final ExecutorService threadPool = Executors.newFixedThreadPool(25);
        List<Future> futures = new ArrayList<>();
        //этот код ниже менять нельзя!
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }
        long startTs = System.currentTimeMillis();
        for (String text : texts) {
            myCallable = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
                return text.substring(0, 100) + " -> " + maxSize;
            };
            futures.add(threadPool.submit(myCallable));
        }
        for (Future future : futures) {
            future.get();
        }
        long endTs = System.currentTimeMillis();
        System.out.println("Time: " + (endTs - startTs) + "ms");
        threadPool.shutdown();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}