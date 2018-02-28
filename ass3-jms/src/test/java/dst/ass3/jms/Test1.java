package dst.ass3.jms;

import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.log;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass3.AbstractJMSTest;
import dst.ass3.dto.ClassifyEventWrapperDTO;
import dst.ass3.dto.EventWrapperDTO;
import dst.ass3.jms.scheduler.IScheduler.ISchedulerListener;
import dst.ass3.jms.streamingserver.IStreamingServer.IStreamingServerListener;
import dst.ass3.model.EventType;
import dst.ass3.model.LifecycleState;

/**
 * This test performs the following tasks: ASSIGN and DENY.
 * 
 * <pre>
 * Timing diagram                                                                         
 *                                                                                        
 *    0  1  2 [sec]                                                                       
 *    |--|--|-->                                                                          
 * E1 D
 *    ^     ^                                                                             
 *    CP1   CP2                                                                            
 *                                                                                        
 * D: Event denied by StreamingServer
 *                                                                                        
 * E1: event1
 *                                                                                        
 * CP1: Check-Point 1 - Assign event1
 * CP2: Check-Point 2 - [Scheduler = ASSIGNED, DENIED] [StreamingServer = DENIED]
 * </pre>
 */
public class Test1 extends AbstractJMSTest {

	private AtomicInteger streamingServerEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);

	private long eventId = 10;

	private long eventWrapperId = -1;

	private long startTime;

	private Semaphore sem;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_AssignAndDeny() {
		sem = new Semaphore(0);
		ss1.start();
		ss2.start();
		scheduler.start();
		u1.start();

		IStreamingServerListener streamingServerListener = new IStreamingServerListener() {
			@Override
			public EventWrapperDecideResponse decideEvent(ClassifyEventWrapperDTO eventWrapper,
														  String streamingServerName) {
				logTimed("** streamingServer " + streamingServerName + " eventWrapper: " + eventWrapper,
						startTime);
				streamingServerEvent.incrementAndGet();

				assertEquals("only 1 raised event expected", 1,
						streamingServerEvent.get());

				assertNotNull("eventWrapper.eventId = null", eventWrapper.getEventId());
				assertEquals("eventId wrong", eventId, eventWrapper.getEventId().longValue());

				assertNotNull("eventWrapper.id = null", eventWrapper.getId());

				log("SETTING ID " + eventWrapper.getId());
				eventWrapperId = eventWrapper.getId();
				sem.release();
				return new EventWrapperDecideResponse(EventWrapperResponse.DENY, null);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, EventWrapperDTO eventWrapper) {
				logTimed("** scheduler: type=" + type + " eventWrapper: " + eventWrapper,
						startTime);

				sleep(SHORT_WAIT); // wait short time for updated eventId

				assertEquals("eventId in server response DTO wrong "
						+ schedulerEvent, eventId, eventWrapper.getEventId().longValue());
				assertEquals("eventWrapperId in server response DTO wrong"
						+ schedulerEvent, eventWrapperId, eventWrapper.getId().longValue());

				switch (schedulerEvent.get()) {
				case 0:
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event != ASSIGNED",
							LifecycleState.ASSIGNED, eventWrapper.getState());
					assertEquals("1st event event type != UNCLASSIFIED",
							EventType.UNCLASSIFIED, eventWrapper.getType());
					break;
				case 1:
					assertEquals("2nd event of wrong type", InfoType.DENIED,
							type);
					assertEquals("2nd event != STREAMING_NOT_POSSIBLE",
							LifecycleState.STREAMING_NOT_POSSIBLE,
							eventWrapper.getState());
					assertEquals("2nd event event type != UNCLASSIFIED",
							EventType.UNCLASSIFIED, eventWrapper.getType());
					break;
				default:
					fail("only 2 events expected");
					break;
				}
				schedulerEvent.incrementAndGet();
				sem.release();
			}
		};

		sleep(SHORT_WAIT); // Wait for old messages being discarded.
		startTime = new Date().getTime();

		// ---------------- CP1 ------------------------
		logCheckpoint(1, startTime);

		ss1.setStreamingServerListener(streamingServerListener);
		ss2.setStreamingServerListener(streamingServerListener);

		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + eventId + "...");
		scheduler.assign(eventId);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);
		assure(sem,
				3,
				"did not get 3 events (Scheduler: create, denied; StreamingServer: classify) in time",
				DEFAULT_CHECK_TIMEOUT);
		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of streamingServer events ", 1, streamingServerEvent.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		ss1.setStreamingServerListener(null);
		ss2.setStreamingServerListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down " + AbstractJMSTest.UPLINK1_NAME + "...");
		u1.stop();
		log("shutting down " + AbstractJMSTest.STREAMINGSERVER1_NAME + "...");
		ss1.stop();
		log("shutting down " + AbstractJMSTest.STREAMINGSERVER2_NAME + "...");
		ss2.stop();
		log("shutting down Scheduler...");
		scheduler.stop();
		
		super.shutdown();
	}
}
