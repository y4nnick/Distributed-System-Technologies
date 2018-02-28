package dst.ass3.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EventWrapper implements IEventWrapper {

	private Long id;
	private Long eventId;
	private LifecycleState state;
	private String classifiedBy;
	private EventType type;

	public EventWrapper() {
	}

	public EventWrapper(Long eventId, LifecycleState state, String classifiedBy,
						EventType type) {
		super();
		this.eventId = eventId;
		this.state = state;
		this.classifiedBy = classifiedBy;
		this.type = type;
	}

	@Id
	@GeneratedValue
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
		return "EventWrapper [id=" + id + ", eventId=" + eventId + ", status=" + state
				+ ", classifiedBy=" + classifiedBy + ", type=" + type + "]";
	}

}
