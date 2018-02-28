package dst.ass3.dto;

import java.io.Serializable;

public class NewEventWrapperDTO implements Serializable {

	private static final long serialVersionUID = 843972285375484461L;
	private Long eventId;

	public NewEventWrapperDTO() {
	}

	public NewEventWrapperDTO(Long eventId) {
		this.eventId = eventId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

}
