package dst.ass2.ws;

/**
 * This interface defines the getters and setters of the 
 * GetStatsRequest Web service request object.
 */
public interface IGetStatsRequest {

	/**
	 * @return maximum number of streamings in the statistics
	 */
	int getMaxStreamings();

	/**
	 * @param maxStreamings maximum number of streamings in the statistics
	 */
	void setMaxStreamings(int maxStreamings);

}
