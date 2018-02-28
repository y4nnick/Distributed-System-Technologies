package dst.ass2.ejb.management.interfaces;

import java.math.BigDecimal;

public interface IPriceManagementBean {

	/**
	 * @param nrOfHistoricalEvents
	 * @return the price for the given number of historical events. If there was
	 *         no price for this number of events specified it returns 0.
	 */
	public BigDecimal getPrice(Integer nrOfHistoricalEvents);

	/**
	 * Creates a price-step for the given number of historical events.
	 * 
	 * @param nrOfHistoricalEvents
	 * @param price
	 */
	public void setPrice(Integer nrOfHistoricalEvents, BigDecimal price);

	/**
	 * Clears the cached price-steps.
	 */
	public void clearCache();

}
