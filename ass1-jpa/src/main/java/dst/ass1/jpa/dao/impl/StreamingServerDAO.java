package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IStreamingServerDAO;
import dst.ass1.jpa.model.IStreamingServer;
import dst.ass1.jpa.model.impl.Event;
import dst.ass1.jpa.model.impl.StreamingServer;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by amra.
 */
public class StreamingServerDAO implements IStreamingServerDAO{

    private EntityManager entityManager;

    public  StreamingServerDAO(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    @Override
    public IStreamingServer findById(Long id) {
        return entityManager.find(StreamingServer.class, id);
    }

    @Override
    public List<IStreamingServer> findAll() {

        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(StreamingServer.class).list();
    }
}
