package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.EventStatus;

public interface IEventStreamingDAO extends GenericDAO<IEventStreaming> {
	List<IEventStreaming> findByStatus(EventStatus status);
}
