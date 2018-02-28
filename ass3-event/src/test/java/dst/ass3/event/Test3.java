package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.fail;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

import dst.ass3.AbstractEventTest;
import dst.ass3.EventingUtils;
import dst.ass3.model.EventType;
import dst.ass3.model.IEventWrapper;
import dst.ass3.model.LifecycleState;

/**
 * Basic Test: Adds an ASSIGNED event, changes the Event to Streamed and waits
 * for a EventDuration Event.
 * 
 */
public class Test3 extends AbstractEventTest {

	private Semaphore semDuration;
	private Semaphore semAss;
	private Semaphore semStreamed;

	private long eventWrapperId1 = 3L;
	private long eventWrapperId2 = 6L;

	private long eventId1 = 5L;
	private long eventId2 = 7L;

	@Test
	public void test_EventDurationEvent1() {
		final long startTime = System.currentTimeMillis();
		semDuration = new Semaphore(0);
		semAss = new Semaphore(0);
		semStreamed = new Semaphore(0);

		IEventWrapper l1 = EventingFactory.createEventWrapper(eventWrapperId1,
				eventId1, LifecycleState.ASSIGNED, CLASSIFIEDBY, EventType.PRESENTATION);
		IEventWrapper l2 = EventingFactory.createEventWrapper(eventWrapperId2,
				eventId2, LifecycleState.ASSIGNED, CLASSIFIEDBY, EventType.PRESENTATION);

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents,
					EPStatement s, EPServiceProvider p) {
				Long timestamp = null;
				for (EventBean e : newEvents) {
					System.out.println("LISTENER:" + e.getEventType().getName()
							+ " " + e.getUnderlying());
					String name = e.getEventType().getName();

					if (name.equals(Constants.EVENT_EVENT_DURATION)) {
						EventingUtils.ensureEventId("Duration", e, eventId1);
						semDuration.release();
					} else if (name.equals(Constants.EVENT_EVENT_ASSIGNED)) {
						timestamp = EventingUtils.getTimeStamp(e);
						EventingUtils.ensureEventId("Assigned", e, eventId1,
								eventId2);
						if (timestamp < startTime) {
							fail("starttime > timestamp (start time:"
									+ startTime + ", timestamp:" + timestamp
									+ ")");
						}
						semAss.release();
					} else if (name.equals(Constants.EVENT_EVENT_STREAMED)) {
						timestamp = EventingUtils.getTimeStamp(e);
						EventingUtils.ensureEventId(Constants.EVENT_EVENT_STREAMED, e, eventId1);
						if (timestamp < startTime) {
							fail("starttime > timestamp (start time:"
									+ startTime + ", timestamp:" + timestamp
									+ ")");
						}
						semStreamed.release();
					}

				}

			}
		}, false);

		sleep(SHORT_WAIT); // wait for setup
		logCheckpoint(0, startTime);

		test.addEvent(l1);
		test.addEvent(l2);

		logCheckpoint(1, startTime);
		l1.setState(LifecycleState.STREAMED);
		test.addEvent(l1);

		logTimed("checking results", startTime);
		assure(semDuration, 1, "1 eventDuration event expected!",
				ESPER_CHECK_TIMEOUT);
		assure(semAss, 2, "2 eventAssigned events expected!",
				ESPER_CHECK_TIMEOUT);
		assure(semStreamed, 1, "1 eventStreamed event expected!",
				ESPER_CHECK_TIMEOUT);

	}
}
