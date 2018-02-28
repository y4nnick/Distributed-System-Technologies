package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.IMOSPlatform;

public interface IMOSPlatformDAO extends GenericDAO<IMOSPlatform> {
	List<IMOSPlatform> findByName(String name);
}
