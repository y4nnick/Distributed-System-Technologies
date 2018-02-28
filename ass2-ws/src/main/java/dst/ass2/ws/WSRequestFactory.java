package dst.ass2.ws;

import dst.ass2.ws.impl.GetStatsRequest;

/**
 * Factory used to instantiate Web service request objects.
 */
public class WSRequestFactory {

	/**
	 * Create an instance of IGetStatsRequest
	 * @return
	 */
	public IGetStatsRequest createGetStatsRequest() {

		return new GetStatsRequest();
	}

}
