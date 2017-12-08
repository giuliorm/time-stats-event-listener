import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.TimeStatsEventListener;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventProducer implements Runnable {

    public EventProducer() {
    }

    @Override
    public void run() {
        while(true) {
            int l = new Random(100).nextInt();
            l = l < 0 ? 100 : l;
            for (int i = 0; i <l ; i++) {
                Event e = new Event(System.currentTimeMillis());
                TimeStatsEventListener.getInstance().accept(e);
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void runProducers() {
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++)
            service.execute(this);
        service.shutdown();
    }
}
