package dst.ass1.jpa.dao;

import java.util.Date;
import java.util.HashMap;

import dst.ass1.jpa.model.IModerator;

public interface IModeratorDAO extends GenericDAO<IModerator> {
	public HashMap<IModerator, Date> findNextStreamingServerMaintenanceByModerators();
}
