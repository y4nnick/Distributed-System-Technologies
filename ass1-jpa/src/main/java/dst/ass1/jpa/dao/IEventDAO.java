package dst.ass1.jpa.dao;

import dst.ass1.jpa.model.IEvent;

import java.util.Date;
import java.util.List;

public interface IEventDAO extends GenericDAO<IEvent> {
	List<IEvent> findEventsForEventMasterAndGame(String eventMasterName,
			String game);

	List<IEvent> findEventsForStatusFinishedStartandFinish(Date start,
			Date finish);
}
