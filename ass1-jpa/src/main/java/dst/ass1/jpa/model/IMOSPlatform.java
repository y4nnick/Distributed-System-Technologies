package dst.ass1.jpa.model;

import java.math.BigDecimal;
import java.util.List;

public interface IMOSPlatform {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public String getUrl();

	public void setUrl(String url);

	public BigDecimal getCostsPerStreamingMinute();

	public void setCostsPerStreamingMinute(BigDecimal costsPerStreamingMinute);

	public List<IMembership> getMemberships();

	public void setMemberships(List<IMembership> memberships);

	public void addMembership(IMembership membership);

	public List<IStreamingServer> getStreamingServers();

	public void setStreamingServers(List<IStreamingServer> streamingServers);

	public void addStreamingServer(IStreamingServer streamingServer);

}