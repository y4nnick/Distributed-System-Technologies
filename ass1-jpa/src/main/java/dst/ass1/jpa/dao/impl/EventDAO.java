package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IEventDAO;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.impl.Event;
import dst.ass1.jpa.model.impl.EventStreaming;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by amra.
 *
 */


public class EventDAO implements IEventDAO{


    private EntityManager entityManager;

    public  EventDAO(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    @Override
    public List<IEvent> findEventsForEventMasterAndGame(String eventMasterName, String game) {

        Criteria criteria = entityManager.unwrap(org.hibernate.Session.class).createCriteria(Event.class);

        if (eventMasterName != null) {
            criteria.createCriteria("eventMaster").add(Restrictions.eq("eventMasterName" , eventMasterName));
        }

        if (game != null) {
            criteria.createCriteria("metadata").add(Restrictions.eq("game" , game));
        }

        return criteria.list();
    }



    @Override
    public List<IEvent> findEventsForStatusFinishedStartandFinish(Date start, Date finish) {
        EventStreaming es = new EventStreaming();

        es.setStatus(EventStatus.FINISHED);
        if (start != null) es.setStart(start);
        if (finish != null) es.setEnd(finish);

        Criteria criteria = entityManager.unwrap(org.hibernate.Session.class).createCriteria(Event.class);
        return criteria.createCriteria("eventStreaming").add(Example.create(es)).list();

    }

    @Override
    public IEvent findById(Long id) {
        return entityManager.find(Event.class, id);
    }

    @Override
    public List<IEvent> findAll() {
        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(Event.class).list();
    }

}
