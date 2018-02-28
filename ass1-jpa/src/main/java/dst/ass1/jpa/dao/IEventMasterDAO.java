package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.IEventMaster;

public interface IEventMasterDAO extends GenericDAO<IEventMaster> {
	List<IEventMaster> findByName(String eventMasterName);
}
