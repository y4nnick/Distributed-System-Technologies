package dst.ass3.dto;

import java.io.Serializable;

import dst.ass3.model.IEventWrapper;
import dst.ass3.model.EventType;
import dst.ass3.model.LifecycleState;

public class StreamEventWrapperDTO implements Serializable {

	private static final long serialVersionUID = 2537272216199752190L;
	private Long id;
	private Long eventId;
	private LifecycleState state;
	private String classifiedBy;
	private EventType type;

	public StreamEventWrapperDTO() {
	}

	public StreamEventWrapperDTO(Long id, Long eventId, LifecycleState state,
								 String classifiedBy, EventType type) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.state = state;
		this.classifiedBy = classifiedBy;
		this.type = type;
	}

	public StreamEventWrapperDTO(IEventWrapper event) {
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
		return "Event [id=" + id + ", eventId=" + eventId + ", classifiedBy=" + classifiedBy
				+ ", state=" + state + ", type=" + type + "]";
	}

}
