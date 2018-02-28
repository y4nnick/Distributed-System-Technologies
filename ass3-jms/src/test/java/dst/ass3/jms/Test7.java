package dst.ass3.jms;

import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.log;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
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
 * This test performs the following tasks: Assign 3 Events, accept and stream
 * all of them.
 * 
 * <pre>
 * Timing diagram
 * 
 *      0  1  2  3  4  5  6  7  8  9  10 [sec]                                     
 *      |--|--|--|--|--|--|--|--|--|--|-->                                         
 * E1/2 ******************
 * E3         ******************
 *      ^     ^     ^        ^        ^                                            
 *      CP1   CP2   CP3      CP4      CP5                                           
 *                                                                                 
 * Each Event needs ****************** (=6sec) for streaming
 * CP1: Check-Point 1 - Assign event1, event2
 * CP2: Check-Point 2 - Assign event3
 * CP3: Check-Point 3 - 1-3 running                                                                 
 * CP4: Check-Point 4 - 1-2 STREAMED, 3 running                                                    
 * CP5: Check-Point 5 - 3 STREAMED
 * </pre>
 */
public class Test7 extends AbstractJMSTest {

	private AtomicInteger streamingServerEventEvent1 = new AtomicInteger(0);
	private AtomicInteger streamingServerEventEvent2 = new AtomicInteger(0);
	private AtomicInteger streamingServerEventEvent3 = new AtomicInteger(0);

	private AtomicInteger schedulerEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEventEvent1 = new AtomicInteger(0);
	private AtomicInteger schedulerEventEvent2 = new AtomicInteger(0);
	private AtomicInteger schedulerEventEvent3 = new AtomicInteger(0);

	private AtomicInteger uplinkEvent = new AtomicInteger(0);
	private AtomicInteger uplinkEventEvent1 = new AtomicInteger(0);
	private AtomicInteger uplinkEventEvent2 = new AtomicInteger(0);
	private AtomicInteger uplinkEventEvent3 = new AtomicInteger(0);

	private long eventId1 = 70;
	private long eventId2 = 71;
	private long eventId3 = 72;
	private long eventWrapperId1 = -1;
	private long eventWrapperId2 = -1;
	private long eventWrapperId3 = -1;

	private String event1ClassifiedBy;
	private String event2ClassifiedBy;
	private String event3ClassifiedBy;
	private long startTime;

	private final int STREAMING_TIME = 6000;

	private Semaphore sem;
	private Semaphore semUplink;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_TypeAssignClassifyAndStream() {
		sem = new Semaphore(0);
		semUplink = new Semaphore(0);

		ss1.start();
		ss2.start();
		scheduler.start();
		u1.start();
		u2.start();
		u3.start();
		u4.start();

		IStreamingServerListener streamingServerListener = new IStreamingServerListener() {
			@Override
			public EventWrapperDecideResponse decideEvent(ClassifyEventWrapperDTO eventWrapper,
														  String streamingServerName) {
				logTimed("** streamingServer " + streamingServerName + " event: " + eventWrapper,
						startTime);
				if (eventWrapper.getEventId() == eventId1) {
					streamingServerEventEvent1.incrementAndGet();
					eventWrapperId1 = eventWrapper.getId();
					event1ClassifiedBy = streamingServerName;
					return new EventWrapperDecideResponse(EventWrapperResponse.ACCEPT,
							EventType.PRESENTATION);
				}
				if (eventWrapper.getEventId() == eventId2) {
					streamingServerEventEvent2.incrementAndGet();
					eventWrapperId2 = eventWrapper.getId();
					event2ClassifiedBy = streamingServerName;
					return new EventWrapperDecideResponse(EventWrapperResponse.ACCEPT,
							EventType.INTERACTIVE);
				}
				if (eventWrapper.getEventId() == eventId3) {
					streamingServerEventEvent3.incrementAndGet();
					eventWrapperId3 = eventWrapper.getId();
					event3ClassifiedBy = streamingServerName;
					return new EventWrapperDecideResponse(EventWrapperResponse.ACCEPT,
							EventType.INTERACTIVE);
				}

				fail("streamingServer Events - unknown type");
				return new EventWrapperDecideResponse(EventWrapperResponse.DENY, null);
			}
		};

		IUplinkListener uplinkListener = new IUplinkListener() {
			@Override
			public void waitTillStreamed(StreamEventWrapperDTO eventWrapper,
					String uplinkName, EventType acceptedType,
					String streamingServerName) {
				logTimed("** uplink " + uplinkName + " event: " + eventWrapper,
						startTime);

				if (eventWrapper.getId() == eventWrapperId1) {
					uplinkEventEvent1.incrementAndGet();
					assertEquals("uplinkListener 1 eventWrapperId", eventWrapperId1, eventWrapper
							.getId().longValue());
					assertEquals("uplinkListener 1 eventId", eventId1, eventWrapper
							.getEventId().longValue());
					assertEquals("uplinkListener 1 type",
							EventType.PRESENTATION, eventWrapper.getType());
					assertEquals("uplinkListener 1 classifiedby", event1ClassifiedBy,
							eventWrapper.getClassifiedBy());
				}
				if (eventWrapper.getId() == eventWrapperId2) {
					uplinkEventEvent2.incrementAndGet();
					assertEquals("uplinkListener 2 eventWrapperId", eventWrapperId2, eventWrapper
							.getId().longValue());
					assertEquals("uplinkListener 2 eventId", eventId2, eventWrapper
							.getEventId().longValue());
					assertEquals("uplinkListener 2 type",
							EventType.INTERACTIVE, eventWrapper.getType());
					assertEquals("uplinkListener 2 classifiedby", event2ClassifiedBy,
							eventWrapper.getClassifiedBy());
				}
				if (eventWrapper.getId() == eventWrapperId3) {
					uplinkEventEvent3.incrementAndGet();
					assertEquals("uplinkListener 3 eventWrapperId", eventWrapperId3, eventWrapper
							.getId().longValue());
					assertEquals("uplinkListener 3 eventId", eventId3, eventWrapper
							.getEventId().longValue());
					assertEquals("uplinkListener 3 type",
							EventType.INTERACTIVE, eventWrapper.getType());
					assertEquals("uplinkListener 3 classifiedby", event3ClassifiedBy,
							eventWrapper.getClassifiedBy());
				}

				uplinkEvent.incrementAndGet();
				sem.release();

				sleep(STREAMING_TIME);

				semUplink.release();
				logTimed("finish " + uplinkName, startTime);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, EventWrapperDTO eventWrapper) {
				logTimed("** scheduler: type=" + type + " event: " + eventWrapper,
						startTime);
				sleep(SHORT_WAIT); // wait short time for updated eventWrapperId
				if (eventWrapper.getEventId() == eventId1) {
					assertEquals("eventWrapperId in server response DTO wrong"
							+ schedulerEvent, eventWrapperId1, eventWrapper.getId().longValue());
					schedulerEventEvent1.incrementAndGet();
				}
				if (eventWrapper.getEventId() == eventId2) {
					assertEquals("eventWrapperId in server response DTO wrong Event2 "
							+ schedulerEvent, eventWrapperId2, eventWrapper.getId().longValue());
					schedulerEventEvent2.incrementAndGet();
				}
				if (eventWrapper.getEventId() == eventId3) {
					assertEquals("eventWrapperId in server response DTO wrong Event3 "
							+ schedulerEvent, eventWrapperId3, eventWrapper.getId().longValue());
					schedulerEventEvent3.incrementAndGet();
				}

				switch (schedulerEvent.get()) {
				case 0:
					// ASSIGN for eventId1 / eventId2
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event wrong", LifecycleState.ASSIGNED,
							eventWrapper.getState());
					assertEquals("1st event Event type wrong",
							EventType.UNCLASSIFIED, eventWrapper.getType());
					break;
				case 1:
					// ASSIGN for eventId1 / eventId2
					assertEquals("2nd event of wrong type", InfoType.CREATED,
							type);
					assertEquals("2nd event wrong", LifecycleState.ASSIGNED,
							eventWrapper.getState());
					assertEquals("2nd event Event type wrong",
							EventType.UNCLASSIFIED, eventWrapper.getType());
					break;
				case 2:
					// ASSIGN for eventId3
					assertEquals("3rd event of wrong type", InfoType.CREATED,
							type);
					assertEquals("3rd event wrong", LifecycleState.ASSIGNED,
							eventWrapper.getState());
					assertEquals("3rd event Event type wrong",
							EventType.UNCLASSIFIED, eventWrapper.getType());
					break;
				case 3:
					// STREAMED 1-2
					assertEquals("4th event of wrong type", InfoType.STREAMED,
							type);
					assertEquals("4th event wrong", LifecycleState.STREAMED,
							eventWrapper.getState());
					assertNotSame("4th event Event type wrong",
							EventType.UNCLASSIFIED, eventWrapper.getType());
					break;
				case 4:
					// STREAMED 1-2
					assertEquals("5th event of wrong type", InfoType.STREAMED,
							type);
					assertEquals("5th event wrong", LifecycleState.STREAMED,
							eventWrapper.getState());
					assertNotSame("5th event Event type wrong",
							EventType.UNCLASSIFIED, eventWrapper.getType());
					break;
				case 5:
					// STREAMED 3
					assertEquals("6th event of wrong type", InfoType.STREAMED,
							type);
					assertEquals("6th wrong Event", eventId3, eventWrapper.getEventId()
							.longValue());
					assertEquals("6th event wrong", LifecycleState.STREAMED,
							eventWrapper.getState());
					assertEquals("6th event Event type ",
							EventType.INTERACTIVE, eventWrapper.getType());
					assertEquals("6th event classified by", event3ClassifiedBy,
							eventWrapper.getClassifiedBy());
					break;
				default:
					fail("only 6 events expected");
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

		u1.setUplinkListener(uplinkListener);
		u2.setUplinkListener(uplinkListener);
		u3.setUplinkListener(uplinkListener);
		u4.setUplinkListener(uplinkListener);

		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + eventId1 + "...");
		scheduler.assign(eventId1);
		log("Assigning " + eventId2 + "...");
		scheduler.assign(eventId2);

		assure(sem,
				4,
				"did not receive 4 events (Scheduler: create, create; Uplink: pre-processing, pre-processing) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);

		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 1,
				schedulerEventEvent1.get());
		assertEquals("wrong count of scheduler 2 events ", 1,
				schedulerEventEvent2.get());
		assertEquals("wrong count of uplink 1 events ", 1,
				uplinkEventEvent1.get());
		assertEquals("wrong count of uplink 2 events ", 1,
				uplinkEventEvent2.get());
		assertEquals("wrong count of uplink 3 events ", 0,
				uplinkEventEvent3.get());

		sleep(2000); // 2 sec delayed startup
		scheduler.assign(eventId3);

		assure(sem, 2, "did not receive 2 event (Scheduler: create; Uplink: pre-processing) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);

		assertEquals("wrong count of uplink events ", 3, uplinkEvent.get());

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 1,
				schedulerEventEvent1.get());
		assertEquals("wrong count of scheduler 2 events ", 1,
				schedulerEventEvent2.get());
		assertEquals("wrong count of scheduler 3 events ", 1,
				schedulerEventEvent3.get());
		assertEquals("wrong count of uplink 1 events ", 1,
				uplinkEventEvent1.get());
		assertEquals("wrong count of uplink 2 events ", 1,
				uplinkEventEvent2.get());
		assertEquals("wrong count of uplink 3 events ", 1,
				uplinkEventEvent3.get());

		// Event 1 and 2 need some time to finish
		assure(semUplink,
				2,
				"did not receive 2 events (Uplink: finished streaming, finished streaming) in time",
				DEFAULT_CHECK_TIMEOUT + STREAMING_TIME / 1000);
		assure(sem,
				2,
				"did not receive 2 events (Scheduler: streaming, streamed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP4 ------------------------
		logCheckpoint(4, startTime);

		assertEquals("wrong count of scheduler events ", 5,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 2,
				schedulerEventEvent1.get());
		assertEquals("wrong count of scheduler 2 events ", 2,
				schedulerEventEvent2.get());
		assertEquals("wrong count of scheduler 3 events ", 1,
				schedulerEventEvent3.get());

		assertEquals("wrong count of streamingServer Event1 events ", 1,
				streamingServerEventEvent1.get());
		assertEquals("wrong count of streamingServer Event2 events ", 1,
				streamingServerEventEvent2.get());
		assertEquals("wrong count of streamingServer Event3 events ", 1,
				streamingServerEventEvent3.get());

		assertEquals("wrong count of uplink events ", 3, uplinkEvent.get());
		assertEquals("wrong count of uplink 1 events ", 1,
				uplinkEventEvent1.get());
		assertEquals("wrong count of uplink 2 events ", 1,
				uplinkEventEvent2.get());
		assertEquals("wrong count of uplink 3 events ", 1,
				uplinkEventEvent3.get());

		assure(semUplink,
				1,
				"did not receive 1 event (uplink: finished streaming) in time",
				DEFAULT_CHECK_TIMEOUT + STREAMING_TIME / 1000);

		assure(sem, 1,
				"did not receive 1 event (Scheduler: streamed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP5 ------------------------
		logCheckpoint(5, startTime);
		assertEquals("wrong count of scheduler events ", 6,
				schedulerEvent.intValue());
		assertEquals("wrong count of scheduler 1 events ", 2,
				schedulerEventEvent1.get());
		assertEquals("wrong count of scheduler 2 events ", 2,
				schedulerEventEvent2.get());
		assertEquals("wrong count of scheduler 3 events ", 2,
				schedulerEventEvent3.get());

		assertEquals("wrong count of streamingServer Event1 events ", 1,
				streamingServerEventEvent1.get());
		assertEquals("wrong count of streamingServer Event2 events ", 1,
				streamingServerEventEvent2.get());
		assertEquals("wrong count of streamingServer Event3 events ", 1,
				streamingServerEventEvent3.get());

		assertEquals("wrong count of uplink events ", 3, uplinkEvent.get());
		assertEquals("wrong count of uplink 1 events ", 1,
				uplinkEventEvent1.get());
		assertEquals("wrong count of uplink 2 events ", 1,
				uplinkEventEvent2.get());
		assertEquals("wrong count of uplink 3 events ", 1,
				uplinkEventEvent3.get());

	}

	@After
	public void shutdown() {
		// disable all listeners
		ss1.setStreamingServerListener(null);
		ss2.setStreamingServerListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down " + AbstractJMSTest.UPLINK1_NAME + "...");
		u1.stop();
		log("shutting down " + AbstractJMSTest.UPLINK2_NAME + "...");
		u2.stop();
		log("shutting down " + AbstractJMSTest.UPLINK3_NAME + "...");
		u3.stop();
		log("shutting down " + AbstractJMSTest.UPLINK4_NAME + "...");
		u4.stop();
		log("shutting down " + AbstractJMSTest.STREAMINGSERVER1_NAME + "...");
		ss1.stop();
		log("shutting down " + AbstractJMSTest.STREAMINGSERVER2_NAME + "...");
		ss2.stop();
		log("shutting down Scheduler...");
		scheduler.stop();

		super.shutdown();
	}

}
