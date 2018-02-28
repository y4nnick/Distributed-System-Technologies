package dst.ass3.event;

import dst.ass3.model.IEventWrapper;
import dst.ass3.model.EventType;
import dst.ass3.model.LifecycleState;
import dst.ass3.dto.EventWrapperDTO;

/**
 * Factory for instantiating objects used in the eventing tests
 */
public class EventingFactory {

	public static IEventProcessing getInstance() {

		// TODO

		return null;
	}

	public static IEventWrapper createEventWrapper(Long id, Long EventId,
			LifecycleState state, String classifiedBy, EventType type) {
		return new EventWrapperDTO(id, EventId, state, classifiedBy, type);
	}
}
