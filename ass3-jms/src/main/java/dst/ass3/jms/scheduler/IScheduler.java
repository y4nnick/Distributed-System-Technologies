package dst.ass3.jms.scheduler;

import dst.ass3.dto.EventWrapperDTO;

public interface IScheduler {
	/**
	 * Starts a scheduler
	 */
	void start();

	/**
	 * Stops the scheduler and cleans all resources (e.g.: close session,
	 * connection, etc.).
	 */
	void stop();

	/**
	 * Assigns a new Event with the given ID.
	 * 
	 * @param eventId
	 */
	void assign(long eventId);

	/**
	 * Requests information for the given EventWrapper ID.
	 * 
	 * @param eventWrapperId
	 */
	void info(long eventWrapperId);

	/**
	 * Sets the listener to report incoming messages. Only one Listener should
	 * be in use at any time. Be sure to handle cases where messages are
	 * received but no listener is yet set (discard the message).
	 * 
	 * @param listener
	 */
	void setSchedulerListener(ISchedulerListener listener);

	interface ISchedulerListener {
		enum InfoType {
			CREATED, INFO, STREAMED, DENIED
		}

		/**
		 * Notifies the listener about an incoming message.
		 * 
		 * @param type
		 *            the type of the incoming message
		 * @param eventWrapper
		 *            the event of the incoming message
		 */
		void notify(InfoType type, EventWrapperDTO eventWrapper);
	}
}
