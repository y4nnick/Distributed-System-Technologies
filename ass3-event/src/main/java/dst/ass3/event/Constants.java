package dst.ass3.event;

/**
 * Constants used for eventing tests.
 */
public class Constants {

	/** Event name for the event-wrapper. */
	public static final String EVENT_EVENT = "EventWrapper";
	
	/** Event name for event assigned events. */
	public static final String EVENT_EVENT_ASSIGNED = "EventAssigned";

	/** Event name for event streamed events. */
	public static final String EVENT_EVENT_STREAMED = "EventStreamed";

	/** Event name for event duration events. */
	public static final String EVENT_EVENT_DURATION = "EventDuration";

	/** Event name for result of average event duration query. */
	public static final String EVENT_AVG_EVENT_DURATION = "AvgEventDuration";
	
	/** Property names */
	public static final String EVENT_PROP_EVENT_ID = "eventId";
	public static final String EVENT_PROP_TIMESTAMP = "timestamp";
	public static final String EVENT_PROP_DURATION = "duration";

}
