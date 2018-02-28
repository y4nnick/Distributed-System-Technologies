package dst.ass3.jms;

import dst.ass3.jms.scheduler.impl.Scheduler;
import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.streamingserver.IStreamingServer;
import dst.ass3.jms.streamingserver.impl.StreamingServer;
import dst.ass3.jms.uplink.IUplink;
import dst.ass3.jms.uplink.impl.Uplink;
import dst.ass3.model.EventType;

public class JMSFactory {

	public static IStreamingServer createStreamingServer(String name) {

		return new StreamingServer(name);
	}

	public static IUplink createUplink(String name, String streamingServer,
			EventType type) {

		return new Uplink(name, streamingServer, type);
	}

	public static IScheduler createScheduler() {
		return new Scheduler();
	}

}
