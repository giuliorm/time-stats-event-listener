import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.StatsEventListener;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventProducer implements Runnable {

    public EventProducer() {
    }

    @Override
    public void run() {
        int l = new Random(10).nextInt();
        l = l < 0 ? 30 : l;
        for (int i = 0; i <l ; i++) {
            StatsEventListener.getInstance().accept(new Event(System.currentTimeMillis()));
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void runProducers() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++)
            service.execute(this);
        service.shutdown();
    }
}
