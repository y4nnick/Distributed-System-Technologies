package dst.ass1.jpa.model;

public interface IEvent {

	public Long getId();

	public void setId(Long id);

	public Integer getStreamingTime();

	public void setStreamingTime(Integer streamingTime);

	public Integer getAttendingViewers();

	public void setAttendingViewers(Integer attendingViewers);

	public boolean isPaid();

	public void setPaid(boolean isPaid);

	public IMetadata getMetadata();

	public void setMetadata(IMetadata metadata);

	public IEventMaster getEventMaster();

	public void setEventMaster(IEventMaster eventMaster);

	/*
	 * Please make sure that the Event entity is the owning side of the relation
	 * with EventStreaming and therefore also propagates (cascades) EntityManager
	 * operations to the relating entity.
	 */

	public IEventStreaming getEventStreaming();

	public void setEventStreaming(IEventStreaming eventStreaming);

}