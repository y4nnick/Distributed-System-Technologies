package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.model.IMOSPlatform;

public interface IUplinkDAO extends GenericDAO<IUplink> {
	List<IUplink> findByPlatform(IMOSPlatform platform);
}
