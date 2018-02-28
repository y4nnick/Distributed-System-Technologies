package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface IStreamingServer {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public Date getLastMaintenance();

	public void setLastMaintenance(Date lastMaintenance);

	public Date getNextMaintenance();

	public void setNextMaintenance(Date nextMaintenance);

	public List<IStreamingServer> getComposedOf();

	public void setComposedOf(List<IStreamingServer> composedOf);

	public void addComposedOf(IStreamingServer streamingServer);

	public List<IStreamingServer> getPartOf();

	public void setPartOf(List<IStreamingServer> partOf);

	public void addPartOf(IStreamingServer streamingServer);

	public List<IUplink> getUplinks();

	public void setUplinks(List<IUplink> uplinks);

	public void addUplink(IUplink uplink);

	public IModerator getModerator();

	public void setModerator(IModerator moderator);

	public IMOSPlatform getMOSPlatform();

	public void setMOSPlatform(IMOSPlatform platform);

}