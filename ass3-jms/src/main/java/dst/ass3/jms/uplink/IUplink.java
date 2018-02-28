package dst.ass3.jms.uplink;

import dst.ass3.dto.StreamEventWrapperDTO;
import dst.ass3.model.EventType;

public interface IUplink {
	/**
	 * Starts an Uplink
	 */
	void start();

	/**
	 * Stops the Uplink and cleans all resources (e.g.: close session,
	 * connection, etc.).
	 */
	void stop();

	/**
	 * Sets the Listener. This listener simulates the execution of the
	 * given event wrapper. Only one Listener should be in use at any time. Be sure to
	 * handle cases where messages are received but no listener is yet set
	 * (discard the message).
	 * 
	 * @param listener
	 */
	void setUplinkListener(IUplinkListener listener);

	interface IUplinkListener {
		/**
		 * Waits until the given Event wrapper has been streamed. You should call this
		 * method ASYNC (in a new Thread) because it may return after a long
		 * time.
		 * 
		 * @param eventWrapper
		 *            the event to simulate execution
		 * @param uplinkName
		 *            the name of the Uplink calling this listener
		 * @param acceptedType
		 *            the type this Uplink accepts
		 * @param streamingServerName
		 *            the name of the StreamingServer this Uplink belongs too
		 */
		void waitTillStreamed(StreamEventWrapperDTO eventWrapper, String uplinkName,
				EventType acceptedType, String streamingServerName);
	}
}
