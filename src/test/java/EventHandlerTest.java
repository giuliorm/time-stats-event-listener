import org.testng.Assert;
import org.testng.annotations.Test;
import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.event.EventHandler;

public class EventHandlerTest {


    private void gapTest(EventHandler handler, long gap, long eventTime, boolean result) {
        Event event = new Event(eventTime);
        Assert.assertEquals(handler.handleEvent(event), result);
    }
    @Test
    public void MinuteGapEventPositiveTest() {
       EventHandler handler = new EventHandler(Event.MIN, "gap");
       gapTest(handler, Event.MIN, System.currentTimeMillis(), true);
    }

    @Test
    public void MinuteGapEventNegativeTest() {
        EventHandler handler = new EventHandler(Event.MIN, "gap");
        gapTest(handler, Event.MIN,  10, false);
    }

    @Test
    public void HourGapEventPositiveTest() {
        EventHandler handler = new EventHandler(Event.HOUR, "gap");
        gapTest(handler, Event.HOUR, System.currentTimeMillis(), true);
    }

    @Test
    public void HourGapEventNegativeTest() {
        EventHandler handler = new EventHandler(Event.HOUR, "gap");
        gapTest(handler, Event.HOUR,  10, false);
    }

    @Test
    public void MinuteGapEventEqualTest() {
        EventHandler handler = new EventHandler(Event.HOUR, "gap");
        gapTest(handler, Event.HOUR, System.currentTimeMillis() - Event.HOUR, true);
        gapTest(handler, Event.HOUR, System.currentTimeMillis() - Event.HOUR, true);
    }

    @Test
    public void MinuteGapEventNegativeNumberTest() {
        try {
            EventHandler handler = new EventHandler(Event.MIN, "gap");
            gapTest(handler, Event.MIN, -100, true);
            Assert.fail();
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

}
