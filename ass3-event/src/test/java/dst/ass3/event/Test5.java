package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.assureMin;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
 * Checks Avg Query with a time window of 15sec.
 */
public class Test5 extends AbstractEventTest {

	private Semaphore semDuration;
	private Semaphore semAvg;

	private final int eventCount = 20;

	private ArrayList<IEventWrapper> running;

	private Map<Long, Long> idDurationMap;
	private Map<Long, Long> esperDuration;

	private int currentFailCount = 0;
	// We accept a fail-count of 10% due to timing issues. The calculation for
	// the allowed number of fail-counts is as follows:
	// allowedNumberOfFailCounts = eventCount / acceptedFailCountFactor =
	// = eventCount / 10 = eventCount * 0.1
	private final int acceptedFailCountFactor = 10;

	@Test
	public void test_AverageQueryWithWindow() {
		final long startTime = System.currentTimeMillis();
		
		semDuration = new Semaphore(0);
		semAvg = new Semaphore(0);

		/**
		 * store the pair <received time>:<received duration> value to check
		 * calculation of esper avg query
		 */
		esperDuration = new HashMap<Long, Long>();

		running = new ArrayList<IEventWrapper>();

		/**
		 * store the pair <eventId>:<starttime> to compare with the duration
		 * field of the EventDuration event
		 */
		idDurationMap = new HashMap<Long, Long>();

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public synchronized void update(EventBean[] newEvents,
					EventBean[] oldEvents, EPStatement s, EPServiceProvider p) {
				Long duration = null;
				long current = System.currentTimeMillis();
				synchronized (idDurationMap) {
					for (EventBean e : newEvents) {
						// System.out.println("LISTENER:"+
						// e.getEventType().getName() + " " +
						// e.getUnderlying());
						String name = e.getEventType().getName();
						if (name.equals(Constants.EVENT_EVENT_ASSIGNED)
								|| name.equals(Constants.EVENT_EVENT_STREAMED)) {
							return;
						}
						if (name.equals(Constants.EVENT_EVENT_DURATION)) {
							/*
							 * EventDuration Event received. Compare duration
							 * value with self calculated value in idDurationMap
							 * store esper duration value in esperDuration for
							 * avg calculation
							 */
							duration = EventingUtils.getLong(e, Constants.EVENT_PROP_DURATION);
							Long eventId = EventingUtils.getLong(e, Constants.EVENT_PROP_EVENT_ID);

							Long expected = idDurationMap.get(eventId);

							if (expected < 0) {
								fail("expected < 0 ! " + eventId);
								return;
							}

							/*
							 * ensureRange will not return control if range is
							 * violated failCount needs to be counted here. So
							 * failure is expected and in case of no failure
							 * failcount value is restored
							 */
							currentFailCount++;
							if (EventingUtils.ensureRange(eventId + " duration",
									expected, duration, allowedInaccuracy,
									false)) {
								// no failure has occurred so withdraw the
								// "borrowed" failcount
								currentFailCount--;
							}
							esperDuration.put(current, duration);
							System.out.println("Duration " + duration);

							semDuration.release();
						} else {
							/*
							 * Avg Event received. Calculate 15sec time window
							 * and sum up self recorded events from
							 * esperDuration
							 */
							Double avgDuration = EventingUtils.getDouble(e,
									Constants.EVENT_AVG_EVENT_DURATION);

							long fifteenSecsAgo = current - 15 * 1000 + 100;
							double sum = 0;
							long count = 0;
							for (Long time : esperDuration.keySet()) {
								if (time > fifteenSecsAgo) {
									count++;
									sum += esperDuration.get(time);
								}
							}
							Double expected = sum / count;
							System.out.println("avgDuration: "
									+ avgDuration.longValue() + " expected "
									+ expected.longValue());

							/*
							 * ensureRange will not return control if range is
							 * violated failCount needs to be counted here. So
							 * failure is expected and in case of no failure
							 * failcount value is restored
							 */
							currentFailCount++;
							if (EventingUtils.ensureRange(
									"avg 15 seconds ago does not match",
									expected.longValue(),
									avgDuration.longValue(), 10, false)) {
								// no failure has occurred so withdraw the
								// "borrowed" failcount
								currentFailCount--;
							}
							semAvg.release();
						}
					}
				}

			}
		}, false);

		/*
		 * start adding ASSIGNED events to esper with some randomness (delayed
		 * by factor 0.7). Then start picking random running events and streamed
		 * them.
		 */
		for (int i = 0; i < eventCount; i++) {
			IEventWrapper tmp = null;
			if (running.size() < eventCount) {
				tmp = EventingFactory.createEventWrapper(i + 1L, i + 1L,
						LifecycleState.ASSIGNED, CLASSIFIEDBY, EventType.PRESENTATION);
				test.addEvent(tmp);
				running.add(tmp);
				synchronized (idDurationMap) {
					idDurationMap.put(tmp.getEventId(),
							-1 * System.currentTimeMillis());
				}
				System.out.println("add " + tmp.getEventId());
			}
			EventingUtils.sleepRandom(50 * 1000 / eventCount); // total < 50sec
			int randomStop = (int) (Math.random() * (running.size() + eventCount * 0.7));
			if (running.size() - 1 >= randomStop) {
				process(randomStop);
				running.remove(randomStop); // remove from running
			}
		}

		logTimed("stopping running", startTime);
		for (int i = 0; i < running.size(); i++) {
			process(i);
			EventingUtils.sleepRandom(10 * 1000 / running.size()); // delay stop
																	// <= 10sec
		}

		sleep(SHORT_WAIT); // wait for all events
		logTimed("checking results", startTime);

		assure(semDuration, eventCount, eventCount
				+ " eventDuration event expected", ESPER_CHECK_TIMEOUT);

		// accept >= 10 events (esper may emit additional Avg Events even if no
		// new EventDuration events have been received
		// this happens because the time window moves and some values slide out
		assureMin(semAvg, eventCount, eventCount + " avgDuration event expected",
				0, ESPER_CHECK_TIMEOUT, false);

		// we accept a fail-count of 10% due to timing issues
		if (eventCount / acceptedFailCountFactor < currentFailCount) {
			fail("fail count too high! " + currentFailCount + "/" + eventCount);
		} else {
			logTimed("done, failcount OK: " + currentFailCount + "/"
					+ eventCount, startTime);
		}
	}

	/**
	 * switches a Event to streamed stores the time it was running (to compare
	 * later with esper events) and notify esper
	 * 
	 * @param index
	 */
	private void process(int index) {
		IEventWrapper tmp = running.get(index);
		tmp.setState(LifecycleState.STREAMED);
		synchronized (idDurationMap) {
			idDurationMap.put(
					tmp.getEventId(),
					idDurationMap.get(tmp.getEventId())
							+ System.currentTimeMillis());
			System.out.println("\nstop " + tmp.getEventId() + " Duration:"
					+ idDurationMap.get(tmp.getEventId()));
			test.addEvent(tmp);
			sleep(100); // short wait for esper events
		}
	}
}
