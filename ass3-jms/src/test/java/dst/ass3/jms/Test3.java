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
 * This test performs the following tasks: ASSIGN, CLASSIFY as PRESENTATION and perform INFO
 * 2 times.
 * 
 * <pre>
 * Timing diagram
 * 
 *    0  1  2  3  4 [sec]                                                                 
 *    |--|--|--|--|-->                                                                    
 * E1 ###################...
 * I1       *                                                                             
 * I2       *                                                                             
 *    ^     ^     ^                                                                       
 *    CP1   CP2   CP3                                                                      
 *                                                                                        
 * ##: waiting for processing (no uplink listener = uplink discards message)
 * 
 * E1: event1
 * I1,I2: Info Request 1,2
 * CP1: Check-Point 1 - assign event1, StreamingServer accepts as PRESENTATION
 * CP2: Check-Point 2 - request info 2x [Scheduler = ASSIGN]                                               
 * CP3: Check-Point 3 - info finished  [Scheduler = INFO, INFO]
 * </pre>
 */
public class Test3 extends AbstractJMSTest {

	private AtomicInteger streamingServerEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);

	private long eventId1 = 30;
	private long eventWrapperId1 = -1;

	private String classifiedBy = null;
	private long startTime;

	private Semaphore sem;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_AssignTypeAndInfo() {
		sem = new Semaphore(0);

		ss1.start();
		ss2.start();
		scheduler.start();
		u2.start();
		u4.start();

		IStreamingServerListener streamingServerListener = new IStreamingServerListener() {
			@Override
			public EventWrapperDecideResponse decideEvent(
					ClassifyEventWrapperDTO eventWrapper, String streamingServerName) {
				logTimed("** streamingServer " + streamingServerName + " eventWrapper: " + eventWrapper,
						startTime);

				streamingServerEvent.incrementAndGet();

				assertNotNull("eventWrapper.eventId = null", eventWrapper.getEventId());
				assertNotNull("eventWrapper.id = null", eventWrapper.getId());

				assertEquals("eventId wrong", eventId1, eventWrapper.getEventId()
						.longValue());

				log("SETTING ID " + eventWrapper.getId());
				eventWrapperId1 = eventWrapper.getId();

				classifiedBy = STREAMINGSERVER1_NAME;
				assertEquals("reported streamingServer wrong", classifiedBy, streamingServerName);

				sem.release();
				return new EventWrapperDecideResponse(
						EventWrapperResponse.ACCEPT, EventType.PRESENTATION);
			}
		};

		IStreamingServerListener streamingServerListener2 = new IStreamingServerListener() {
			@Override
			public EventWrapperDecideResponse decideEvent(
					ClassifyEventWrapperDTO eventWrapper, String streamingServerName) {
				logTimed("** streamingServer " + streamingServerName + " eventWrapper: " + eventWrapper,
						startTime);

				streamingServerEvent.incrementAndGet();

				assertNotNull("eventWrapper.eventId = null", eventWrapper.getEventId());
				assertNotNull("eventWrapper.id = null", eventWrapper.getId());

				assertEquals("eventId wrong", eventId1, eventWrapper.getEventId()
						.longValue());

				log("SETTING ID " + eventWrapper.getId());
				eventWrapperId1 = eventWrapper.getId();

				classifiedBy = STREAMINGSERVER2_NAME;
				assertEquals("reported streamingServer wrong", classifiedBy, streamingServerName);

				sem.release();
				return new EventWrapperDecideResponse(
						EventWrapperResponse.ACCEPT, EventType.PRESENTATION);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, EventWrapperDTO eventWrapper) {
				logTimed("** scheduler: type=" + type + " eventWrapper: " + eventWrapper,
						startTime);

				sleep(SHORT_WAIT); // wait short time for updated eventId

				assertEquals("eventId in server response DTO wrong " + eventId1,
						eventId1, eventWrapper.getEventId().longValue());
				assertEquals("eventWrapperId in server response DTO wrong"
						+ schedulerEvent, eventWrapperId1, eventWrapper.getId().longValue());

				switch (schedulerEvent.get()) {
				case 0:
					// ASSIGN for eventId1
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event != ASSIGNED",
							LifecycleState.ASSIGNED, eventWrapper.getState());
					assertEquals("1st event Event type != UNCLASSIFIED",
							EventType.UNCLASSIFIED, eventWrapper.getType());
					break;
				case 1:
					// INFO 1
					assertEquals("2nd event of wrong type", InfoType.INFO, type);
					assertEquals("2nd event != ASSIGNED",
							LifecycleState.READY_FOR_STREAMING,
							eventWrapper.getState());
					assertEquals("2nd event Event type != PRESENTATION",
							EventType.PRESENTATION, eventWrapper.getType());
					assertNotNull("2nd classified by == null" + classifiedBy,
							eventWrapper.getClassifiedBy());
					assertEquals("2nd classified by != " + classifiedBy, classifiedBy,
							eventWrapper.getClassifiedBy());
					break;
				case 2:
					// INFO 2
					assertEquals("3rd event of wrong type", InfoType.INFO, type);
					assertEquals("3rd event != ASSIGNED",
							LifecycleState.READY_FOR_STREAMING,
							eventWrapper.getState());
					assertEquals("3rd event Event type != PRESENTATION",
							EventType.PRESENTATION, eventWrapper.getType());
					assertNotNull("3rd classified by == null" + classifiedBy,
							eventWrapper.getClassifiedBy());
					assertEquals("3rd classified by != " + classifiedBy, classifiedBy,
							eventWrapper.getClassifiedBy());
					break;
				default:
					fail("only 3 events expected");
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
		ss2.setStreamingServerListener(streamingServerListener2);
		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + eventId1 + "...");
		scheduler.assign(eventId1);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);
		assure(sem,
				2,
				"did not get 2 events (Scheduler: assign, StreamingServer: classify) in time",
				DEFAULT_CHECK_TIMEOUT);

		assertEquals("wrong count of streamingServer events ", 1, streamingServerEvent.get());
		assertEquals("wrong count of scheduler events ", 1,
				schedulerEvent.get());

		log("Executing info 2x " + eventWrapperId1 + "...");
		scheduler.info(eventWrapperId1);
		scheduler.info(eventWrapperId1);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);
		assure(sem, 2, "did not get 2 events (Scheduler: info, info) in time",
				DEFAULT_CHECK_TIMEOUT);

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of streamingServer events ", 1, streamingServerEvent.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		ss1.setStreamingServerListener(null);
		ss2.setStreamingServerListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down " + AbstractJMSTest.STREAMINGSERVER1_NAME + "...");
		ss1.stop();
		log("shutting down " + AbstractJMSTest.STREAMINGSERVER2_NAME + "...");
		ss2.stop();
		log("shutting down " + AbstractJMSTest.UPLINK2_NAME + "...");
		u2.stop();
		log("shutting down " + AbstractJMSTest.UPLINK4_NAME + "...");
		u4.stop();
		log("shutting down Scheduler...");
		scheduler.stop();
		
		super.shutdown();
	}

}
