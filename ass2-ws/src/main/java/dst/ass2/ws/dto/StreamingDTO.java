package dst.ass2.ws.dto;

import java.util.Date;

public class StreamingDTO {

	private Date startDate;
	private Date endDate;
	private int numViewers;

	public StreamingDTO() {
	}

	public StreamingDTO(Date startDate, Date endDate, int numViewers) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.numViewers = numViewers;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getNumViewers() {
		return numViewers;
	}

	public void setNumViewers(int numViewers) {
		this.numViewers = numViewers;
	}

	@Override
	public String toString() {
		return startDate.toString() + " -- " + endDate.toString() + " ("
				+ numViewers + ")";
	}

}
