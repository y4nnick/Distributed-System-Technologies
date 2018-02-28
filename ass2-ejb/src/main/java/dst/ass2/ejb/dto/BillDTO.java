package dst.ass2.ejb.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BillDTO implements Serializable {
	private static final long serialVersionUID = 1577495607705041680L;

	private List<BillPerEventMaster> bills;
	private BigDecimal totalPrice;
	private String eventMasterName;

	public String getEventMasterName() {
		return eventMasterName;
	}

	public void setEventMasterName(String eventMasterName) {
		this.eventMasterName = eventMasterName;
	}

	public List<BillPerEventMaster> getBills() {
		return bills;
	}

	public void setBills(List<BillPerEventMaster> bills) {
		this.bills = bills;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public class BillPerEventMaster implements Serializable {
		private static final long serialVersionUID = 8656991004468599034L;

		private Long eventId;
		private Integer numberOfUplinks;
		private BigDecimal setupCosts;
		private BigDecimal streamingCosts;
		private BigDecimal eventCosts;

		public Integer getNumberOfUplinks() {
			return numberOfUplinks;
		}

		public void setNumberOfUplinks(Integer numberOfUplinks) {
			this.numberOfUplinks = numberOfUplinks;
		}

		public BigDecimal getStreamingCosts() {
			return streamingCosts;
		}

		public void setStreamingCosts(BigDecimal streamingCosts) {
			this.streamingCosts = streamingCosts;
		}

		public BigDecimal getSetupCosts() {
			return setupCosts;
		}

		public void setSetupCosts(BigDecimal setupCosts) {
			this.setupCosts = setupCosts;
		}

		public BigDecimal getEventCosts() {
			return eventCosts;
		}

		public void setEventCosts(BigDecimal eventCosts) {
			this.eventCosts = eventCosts;
		}

		public Long getEventId() {
			return eventId;
		}

		public void setEventId(Long eventId) {
			this.eventId = eventId;
		}

		@Override
		public String toString() {
			return "BillPerEventMaster [eventId=" + eventId
					+ ", numberOfUplinks=" + numberOfUplinks
					+ ", setupCosts=" + setupCosts + ", streamingCosts="
					+ streamingCosts + ", eventCosts=" + eventCosts + "]";
		}

	}

	@Override
	public String toString() {
		return "BillDTO [bills=" + bills + ", totalPrice=" + totalPrice
				+ ", eventMasterName=" + eventMasterName + "]";
	}

}
