package dst.ass3.dto;

import java.io.Serializable;

import dst.ass3.model.IEventWrapper;
import dst.ass3.model.EventType;
import dst.ass3.model.LifecycleState;

public class EventWrapperDTO implements Serializable, IEventWrapper {

	private static final long serialVersionUID = 4134104076758220138L;
	private Long id;
	private Long eventId;
	private LifecycleState state;
	private String classifiedBy;
	private EventType type;

	public EventWrapperDTO() {
	}

	public EventWrapperDTO(Long id, Long eventId, LifecycleState state,
			String classifiedBy, EventType type) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.state = state;
		this.classifiedBy = classifiedBy;
		this.type = type;
	}

	public EventWrapperDTO(IEventWrapper event) {
		super();
		this.id = event.getId();
		this.eventId = event.getEventId();
		this.state = event.getState();
		this.classifiedBy = event.getClassifiedBy();
		this.type = event.getType();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public LifecycleState getState() {
		return state;
	}

	public void setState(LifecycleState state) {
		this.state = state;
	}

	public String getClassifiedBy() {
		return classifiedBy;
	}

	public void setClassifiedBy(String classifiedBy) {
		this.classifiedBy = classifiedBy;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", eventId=" + eventId + ", classifiedBy="
				+ classifiedBy + ", type=" + type + ", state=" + state + "]";
	}

}
