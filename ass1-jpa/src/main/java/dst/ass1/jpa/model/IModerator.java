package dst.ass1.jpa.model;

import java.util.List;

public interface IModerator extends IPerson {

	public List<IStreamingServer> getAdvisedStreamingServers();

	public void setAdvisedStreamingServers(
			List<IStreamingServer> streamingServers);

	public void addAdvisedStreamingServers(IStreamingServer streamingServer);

}
