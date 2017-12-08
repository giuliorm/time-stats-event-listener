import org.testng.annotations.Test;
import ru.juriasan.timestats.TimeStatsEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventListenerTest {

    private static final int NUM_THREADS = 15;
    private static final int LOOP_COUNT = 10000;

    @Test(enabled = false)
    public void initial() {
        TimeStatsEventListener listener = TimeStatsEventListener.getInstance();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++)
            executorService.execute(new EventProducer());
        listener.run();
    }
}
