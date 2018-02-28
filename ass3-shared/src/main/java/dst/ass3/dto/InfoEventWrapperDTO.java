package dst.ass3.dto;

import java.io.Serializable;

public class InfoEventWrapperDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long eventWrapperId;

	public InfoEventWrapperDTO() {
	}

	public InfoEventWrapperDTO(Long eventWrapperId) {
		super();
		this.eventWrapperId = eventWrapperId;
	}

	public Long getEventWrapperId() {
		return eventWrapperId;
	}

	public void setEventWrapperId(Long eventWrapperId) {
		this.eventWrapperId = eventWrapperId;
	}

}
