package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IEventStreamingDAO;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.impl.EventStreaming;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by amra.
 */
public class EventStreamingDAO implements IEventStreamingDAO {


    private EntityManager entityManager;

    public  EventStreamingDAO(EntityManager entityManager) {
        this.entityManager=entityManager;
    }


    @Override
    public List<IEventStreaming> findByStatus(EventStatus status) {

        SimpleExpression e = Restrictions.eq("status", status);

        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(EventStreaming.class).add(e).list();
    }


    @Override
    public IEventStreaming findById(Long id) {


        return entityManager.find(EventStreaming.class, id);
    }

    @Override
    public List<IEventStreaming> findAll() {

        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(EventStreaming.class).list();
    }

}



