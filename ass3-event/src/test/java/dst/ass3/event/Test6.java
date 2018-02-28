package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.junit.Test;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

import dst.ass3.AbstractEventTest;
import dst.ass3.EventingUtils;
import dst.ass3.dto.EventWrapperDTO;
import dst.ass3.model.EventType;
import dst.ass3.model.IEventWrapper;
import dst.ass3.model.LifecycleState;

/**
 * Use pattern matching facilities of EPL to detect events which have 3 times
 * attempted and failed to execute (i.e., switched 3 times between the status
 * READY FOR STREAMING and the status STREAMING NOT POSSIBLE).
 */
public class Test6 extends AbstractEventTest {

	private Semaphore semSwitch;

	@Test
	public void test_PatternMatchingQuery() {
		final long startTime = System.currentTimeMillis();

		semSwitch = new Semaphore(0);
		test = EventingFactory.getInstance();

		IEventWrapper t1 = EventingFactory.createEventWrapper(601L, 611L,
				LifecycleState.ASSIGNED, CLASSIFIEDBY, EventType.PRESENTATION);

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents,
					EPStatement s, EPServiceProvider p) {
				try {
					for (EventBean e : newEvents) {
						String name = e.getEventType().getName();
						if (name.equals(Constants.EVENT_EVENT_ASSIGNED)
								|| name.equals(Constants.EVENT_EVENT_STREAMED)
								|| name.equals(Constants.EVENT_EVENT_DURATION)) {
							return;
						}

						List<Object> eventBeans = new LinkedList<Object>();
						eventBeans.addAll(EventingUtils.getAliasedEventBeans(e));
						if(eventBeans.isEmpty()) {
							eventBeans.addAll(EventingUtils.getAliasedEventObjects(e, Object.class));
						}

						for (Object o : eventBeans) {
							if (o != null) {
								LifecycleState state = null;
								EventType type = null;
								String classifiedBy = null;
								Long eventID = null;

								if(o instanceof EventBean) {
									EventBean event = (EventBean)o;
									state = getEventStatus(event);
									if (state == null
											|| !state
													.equals(LifecycleState.READY_FOR_STREAMING))
										continue;
									type = getEventType(event);
									classifiedBy = event.get("classifiedBy").toString();
									eventID = EventingUtils.getLong(event, Constants.EVENT_PROP_EVENT_ID);
									System.out.println("event id "
											+ event.getUnderlying());
								} else {
									EventWrapperDTO[] dtos = (EventWrapperDTO[])o;
									EventWrapperDTO dto = dtos[0];
									state = dto.getState();
									type = dto.getType();
									classifiedBy = dto.getClassifiedBy();
									eventID = dto.getEventId();
								}

								assertEquals("type wrong",
										EventType.PRESENTATION, type);
								assertEquals("classifiedBy wrong", CLASSIFIEDBY, classifiedBy);
								assertEquals("eventId wrong", 611L, (long)eventID);
								semSwitch.release();

								break;
							}
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, false);

		sleep(SHORT_WAIT); // wait for setup
		logCheckpoint(0, startTime);

		t1.setState(LifecycleState.READY_FOR_STREAMING);
		test.addEvent(t1);

		for (int i = 0; i < 3; i++) {
			t1.setState(LifecycleState.STREAMING_NOT_POSSIBLE);
			test.addEvent(t1);
			sleep(SHORT_WAIT);

			t1.setState(LifecycleState.READY_FOR_STREAMING);
			test.addEvent(t1);
		}

		sleep(SHORT_WAIT); // wait for all events
		logTimed("checking results", startTime);

		assure(semSwitch, 1, "1 switch event expected!", ESPER_CHECK_TIMEOUT);
	}
}
