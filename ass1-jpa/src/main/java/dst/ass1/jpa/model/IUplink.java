package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface IUplink {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public Integer getViewerCapacity();

	public void setViewerCapacity(Integer viewerCapacity);

	public String getRegion();

	public void setRegion(String region);

	public Date getActivated();

	public void setActivated(Date activated);

	public Date getLastUpdate();

	public void setLastUpdate(Date lastUpdate);

	public IStreamingServer getStreamingServer();

	public void setStreamingServer(IStreamingServer streamingServer);

	public List<IEventStreaming> getEventStreamings();

	public void setEventStreamings(List<IEventStreaming> streamings);

	public void addEventStreaming(IEventStreaming streaming);

}