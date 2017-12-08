import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.TimeStatsEventListener;
import ru.juriasan.timestats.util.Logger;
import sun.rmi.runtime.Log;

/**
 * This class emulates an event flow in the system. It generates
 * random number of events in infinite loop , which then can be consumed by
 * accept.
 */
public class EventProducer implements Runnable {

    private int sleepMillis;

    /**
     * Constructs EventProducer.
     *
     * @param sleepMillis amount of milliseconds to this thread to sleep.
     */
    public EventProducer(int sleepMillis) {
        this.sleepMillis = sleepMillis != -1 ? sleepMillis : Event.SEC;
    }
    public EventProducer() {
        this.sleepMillis = Event.SEC;
    }

    @Override
    public void run() {
        while(true) {
            if (Thread.currentThread().isInterrupted()) {
                Logger.getInstance().info("EventProducer finishes its work.");
                break;
            }

            Event e = new Event(System.currentTimeMillis());
            TimeStatsEventListener.getInstance().accept(e);
            try {
                Thread.sleep(sleepMillis);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
