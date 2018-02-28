package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IUplinkDAO;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.IStreamingServer;
import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.model.impl.Uplink;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amra.
 */
public class UplinkDao implements IUplinkDAO {

    private EntityManager entityManager;

    public  UplinkDao(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    @Override
    public List<IUplink> findByPlatform(IMOSPlatform platform) {

        List<IUplink> results = new ArrayList<>();

        for (IStreamingServer s : platform.getStreamingServers()){
            results.addAll(s.getUplinks());
        }

        return results;
    }

    @Override
    public IUplink findById(Long id) {

        return entityManager.find(Uplink.class, id);
    }

    @Override
    public List<IUplink> findAll() {
        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(Uplink.class).list();
    }
}
