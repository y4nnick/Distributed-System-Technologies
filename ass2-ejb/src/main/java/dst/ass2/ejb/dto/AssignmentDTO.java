package dst.ass2.ejb.dto;

import java.io.Serializable;
import java.util.List;

public class AssignmentDTO implements Serializable {

	private static final long serialVersionUID = -6468125387570684307L;
	private Long platformId;
	private Integer numViewers;
	private String game;
	private List<String> settings;
	private List<Long> uplinkIds;

	public AssignmentDTO(Long platformId, Integer numViewers,
			String game, List<String> settings, List<Long> uplinkIds) {
		super();
		this.platformId = platformId;
		this.numViewers = numViewers;
		this.game = game;
		this.settings = settings;
		this.uplinkIds = uplinkIds;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public Integer getNumViewers() {
		return numViewers;
	}

	public void setNumViewers(Integer numViewers) {
		this.numViewers = numViewers;
	}

	public List<String> getSettings() {
		return settings;
	}

	public void setSettings(List<String> settings) {
		this.settings = settings;
	}

	public List<Long> getUplinkIds() {
		return uplinkIds;
	}

	public void setUplinkIds(List<Long> uplinkIds) {
		this.uplinkIds = uplinkIds;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((uplinkIds == null) ? 0 : uplinkIds.hashCode());
		result = prime * result
				+ ((platformId == null) ? 0 : platformId.hashCode());
		result = prime * result
				+ ((numViewers == null) ? 0 : numViewers.hashCode());
		result = prime * result
				+ ((settings == null) ? 0 : settings.hashCode());
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssignmentDTO other = (AssignmentDTO) obj;
		if (uplinkIds == null) {
			if (other.uplinkIds != null)
				return false;
		} else if (!uplinkIds.equals(other.uplinkIds))
			return false;
		if (platformId == null) {
			if (other.platformId != null)
				return false;
		} else if (!platformId.equals(other.platformId))
			return false;
		if (numViewers == null) {
			if (other.numViewers != null)
				return false;
		} else if (!numViewers.equals(other.numViewers))
			return false;
		if (settings == null) {
			if (other.settings != null)
				return false;
		} else if (!settings.equals(other.settings))
			return false;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AssignmentDTO [platformId=" + platformId + ", numViewers="
				+ numViewers + ", game=" + game + ", settings=" + settings
				+ ", uplinkIds=" + uplinkIds + "]";
	}

}
