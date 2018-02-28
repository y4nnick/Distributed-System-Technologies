package dst.ass3.jms;

import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.LONG_WAIT;
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
import dst.ass3.dto.StreamEventWrapperDTO;
import dst.ass3.dto.ClassifyEventWrapperDTO;
import dst.ass3.dto.EventWrapperDTO;
import dst.ass3.jms.scheduler.IScheduler.ISchedulerListener;
import dst.ass3.jms.streamingserver.IStreamingServer.IStreamingServerListener;
import dst.ass3.jms.uplink.IUplink.IUplinkListener;
import dst.ass3.model.EventType;
import dst.ass3.model.LifecycleState;

/**
 * This test performs the following tasks: Start one uplink, stop it, ASSIGN
 * 1 event and CLASSIFY it as INTERACTIVE. Start uplink again and STREAM it.
 * Test checks if uplinks that are currently not listening also receive
 * messages, when they come back online.
 * 
 * <pre>
 * Timing diagram
 * 
 *    0  1  2  3  4                                                                       
 *    |--|--|--|--|-->                                                                    
 * E1 *******
 * I1          *                                                                           
 *    ^     ^  ^  ^                                                                       
 *    CP1   CP2+3 CP4                                                                      
 *                                                                                        
 * **: Running                                                                            
 *                                                                                        
 * E1: takes 2sec to finish
 * I1: InfoRequest for EventWrapper1
 *                                                                                        
 * CP1: Check-Point 1 - Assign event1; Shutdown tw1
 * CP2: Check-Point 2 - Start tw1 again; wait till event1 has finished
 * CP3: Check-Point 3 - event1 should have finished so send INFO Request
 * CP4: Check-Point 4 - Info Request completed [Scheduler = ASSIGN, STREAMED, INFO events]
 * </pre>
 */
public class Test8 extends AbstractJMSTest {

	private AtomicInteger streamingServer = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);
	private AtomicInteger streamed = new AtomicInteger(0);

	private long eventId1 = 40;
	private long eventWrapperId1 = -1;

	private String classifiedBy = null;
	private long startTime;

	private Semaphore sem;
	private Semaphore semUplink;

	private final int STREAMING_TIME = 2000;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_UplinkRestart() {
		sem = new Semaphore(0);
		semUplink = new Semaphore(0);
		ss1.start();
		scheduler.start();
		u1.start();

		IStreamingServerListener streamingServerListener = new IStreamingServerListener() {
			@Override
			public EventWrapperDecideResponse decideEvent(
					ClassifyEventWrapperDTO eventWrapper, String streamingServerName) {
				logTimed("** streamingServer " + streamingServerName + " eventWrapper: "
						+ eventWrapper, startTime);

				assertNotNull("eventWrapper.eventId = null",
						eventWrapper.getEventId());
				assertNotNull("eventWrapper.id = null", eventWrapper.getId());

				assertEquals("eventId wrong", eventId1, eventWrapper.getEventId()
						.longValue());

				log("SETTING ID " + eventWrapper.getId());
				eventWrapperId1 = eventWrapper.getId();
				classifiedBy = streamingServerName;

				streamingServer.incrementAndGet();
				sem.release();

				return new EventWrapperDecideResponse(
						EventWrapperResponse.ACCEPT, EventType.INTERACTIVE);
			}
		};

		IUplinkListener uplinkListener = new IUplinkListener() {
			@Override
			public void waitTillStreamed(StreamEventWrapperDTO eventWrapper,
					String uplinkName, EventType acceptedType,
					String streamingServerName) {
				logTimed("** uplink " + uplinkName + " event: "
						+ eventWrapper, startTime);

				assertEquals("streamingServerName", classifiedBy, streamingServerName);
				assertEquals("uplinkName", UPLINK1_NAME, uplinkName);
				assertEquals("eventtype", EventType.INTERACTIVE,
						acceptedType);

				sleep(STREAMING_TIME); // simulate streaming

				streamed.incrementAndGet();

				semUplink.release();
				assertEquals("uplink listener - too many events", 1,
						streamed.get());
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, EventWrapperDTO eventWrapper) {
				logTimed("** scheduler: type=" + type + " eventWrapper: "
						+ eventWrapper, startTime);
				sleep(SHORT_WAIT); // wait short time for updated eventId

				assertEquals("eventId in server response DTO wrong " + eventId1,
						eventId1, eventWrapper.getEventId().longValue());

				assertEquals("eventId in server response DTO wrong"
						+ schedulerEvent, eventWrapperId1, eventWrapper.getId()
						.longValue());

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
					// STREAMED
					assertEquals("2nd event of wrong type", InfoType.STREAMED,
							type);
					assertEquals("2nd event != ASSIGNED",
							LifecycleState.STREAMED, eventWrapper.getState());
					assertEquals("2nd event Event type ",
							EventType.INTERACTIVE, eventWrapper.getType());
					assertNotNull("2nd classified by == null " + classifiedBy,
							eventWrapper.getClassifiedBy());
					assertEquals("2nd classified by != " + classifiedBy, classifiedBy,
							eventWrapper.getClassifiedBy());
					break;
				case 2:
					// INFO
					assertEquals("3rd event of wrong type", InfoType.INFO, type);
					assertEquals("3rd event != ASSIGNED",
							LifecycleState.STREAMED, eventWrapper.getState());
					assertEquals("3rd event Event type ",
							EventType.INTERACTIVE, eventWrapper.getType());
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

		log("Shutdown " + AbstractJMSTest.UPLINK1_NAME + " so it isn't listening anymore!");
		u1.stop(); // uplink is offline now => should not listen anymore

		// ---------------- CP1 ------------------------
		logCheckpoint(1, startTime);

		ss1.setStreamingServerListener(streamingServerListener);
		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + eventId1 + "...");
		scheduler.assign(eventId1);

		assure(sem,
				2,
				"did not get 2 events (Scheduler: create; streamingServer: classify) in time",
				DEFAULT_CHECK_TIMEOUT);
		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);

		assertEquals("wrong count of streamingServer events ", 1,
				streamingServer.get());
		assertEquals("wrong count of scheduler events ", 1,
				schedulerEvent.get());

		// start uplink again => should receive a message and process it
		log("Start " + AbstractJMSTest.UPLINK1_NAME + " again!");
		u1.setUplinkListener(uplinkListener);
		u1.start();

		sleep(LONG_WAIT); // give uplink some time to start again.

		assure(semUplink,
				1,
				"did not get 1 event (uplink: finished streaming) in time",
				DEFAULT_CHECK_TIMEOUT + STREAMING_TIME / 1000);
		assure(sem, 1, "did not get 1 event (Scheduler: streamed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);
		assertEquals("wrong count of uplink events", 1, streamed.get());
		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of streaminServer events ", 1,
				streamingServer.get());

		log("Executing info " + eventWrapperId1 + "...");
		scheduler.info(eventWrapperId1);

		assure(sem, 1, "did not get 1 event (Scheduler: info) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP4 ------------------------
		logCheckpoint(4, startTime);

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of streamingServer events ", 1,
				streamingServer.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		ss1.setStreamingServerListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down " + AbstractJMSTest.UPLINK1_NAME + "...");
		u1.stop();
		log("shutting down " + AbstractJMSTest.STREAMINGSERVER1_NAME + "...");
		ss1.stop();
		log("shutting down Scheduler...");
		scheduler.stop();

		super.shutdown();
	}

}
