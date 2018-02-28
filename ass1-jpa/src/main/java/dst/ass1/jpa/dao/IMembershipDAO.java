package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.IMembership;

public interface IMembershipDAO extends GenericDAO<IMembership> {
	List<IMembership> findByEventMasterAndPlatform(IEventMaster eventMaster,
			IMOSPlatform platform);

	/*
	 * Note: Please note that for the implementation of this DAO interface you
	 * are not required to implement the findById(Long id) method.
	 */
}
