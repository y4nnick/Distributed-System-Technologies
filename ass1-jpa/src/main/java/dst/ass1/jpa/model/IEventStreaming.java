package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface IEventStreaming {

	public Long getId();

	public void setId(Long id);

	public Date getStart();

	public void setStart(Date start);

	public Date getEnd();

	public void setEnd(Date end);

	public EventStatus getStatus();

	public void setStatus(EventStatus eventStatus);

	public List<IUplink> getUplinks();

	public void setUplinks(List<IUplink> uplinks);

	public void addUplinks(IUplink uplink);

	public IEvent getEvent();

	public void setEvent(IEvent event);

}