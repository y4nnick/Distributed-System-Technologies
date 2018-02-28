package dst.ass3.jms.streamingserver;

import dst.ass3.dto.ClassifyEventWrapperDTO;
import dst.ass3.model.EventType;

public interface IStreamingServer {
	/**
	 * Starts a StreamingServer
	 */
	void start();

	/**
	 * Stops the StreamingServer and cleans all resources (e.g.: close session,
	 * connection, etc.). Keep in mind that Listeners may be sleeping when stop
	 * is requested. Be sure to interrupt them and discard the results they
	 * might return, because the system is stopping already.
	 */
	void stop();

	/**
	 * Sets the Listener. Only one Listener should be in use at any
	 * time. Be sure to handle cases where messages are received but no
	 * listener is yet set (discard the message). The listeners may block
	 * forever, so be sure to interrupt them in stop().
	 * 
	 * @param listener
	 */
	void setStreamingServerListener(IStreamingServerListener listener);

	interface IStreamingServerListener {
		enum EventWrapperResponse {
			ACCEPT, DENY
		}

		class EventWrapperDecideResponse {
			public EventWrapperResponse resp;
			public EventType type;

			public EventWrapperDecideResponse(EventWrapperResponse resp,
											  EventType type) {
				this.resp = resp;
				this.type = type;
			}
		}

		/**
		 * Decide on the given event wrapper.
		 * 
		 * @param eventWrapper
		 *            the event to decide
		 * @param streamingServerName
		 *            name of the StreamingServer executing this listener
		 * @return ACCEPT + Type | DENY
		 */
		EventWrapperDecideResponse decideEvent(ClassifyEventWrapperDTO eventWrapper, String streamingServerName);
	}
}
