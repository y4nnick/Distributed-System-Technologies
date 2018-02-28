package dst.ass3.model;

public interface IEventWrapper {

	public Long getId();

	public void setId(Long id);

	public Long getEventId();

	public void setEventId(Long eventId);

	public LifecycleState getState();

	public void setState(LifecycleState status);

	public String getClassifiedBy();

	public void setClassifiedBy(String classifiedBy);

	public EventType getType();

	public void setType(EventType type);
}
