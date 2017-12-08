import org.testng.annotations.Test;
import ru.juriasan.timestats.TimeStatsEventListener;

public class EventListenerTest {

    @Test()
    public void initial() {
        TimeStatsEventListener listener = TimeStatsEventListener.getInstance();
        EventProducer producer = new EventProducer();
        producer.runProducers();
        listener.run();
    }
}
