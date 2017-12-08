import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.TimeStatsEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This class emulates an event flow in the system. It generates
 * random number of events in infinite loop , which then can be consumed by
 * accept.
 */
public class EventProducer implements Runnable {

    private static final int MINIMUM_SLEEP_TIME = 50;
    private static final int MAXIMUM_SLEEP_TIME = 5000;

    public EventProducer() {
    }

    @Override
    public void run() {
        while(true) {
            Event e = new Event(System.currentTimeMillis());
            TimeStatsEventListener.getInstance().accept(e);
            try {

                int sleepTime =  MINIMUM_SLEEP_TIME + (int)(Math.random() * MAXIMUM_SLEEP_TIME);
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
