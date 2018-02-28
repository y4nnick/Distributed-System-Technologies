package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IEventMasterDAO;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.impl.EventMaster;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.util.List;

/**
 * Created by amra.
 */

public class EventMasterDAO implements IEventMasterDAO{


    private EntityManager entityManager;

    public  EventMasterDAO(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    @Override
    public List<IEventMaster> findByName(String eventMasterName) {
        Criteria criteria = entityManager.unwrap(org.hibernate.Session.class).createCriteria(EventMaster.class);

        if (eventMasterName != null) {
            criteria.add(Restrictions.eq("eventMasterName", eventMasterName));
        }

        return criteria.list();

    }

    @Override
    public IEventMaster findById(Long id) {

        return entityManager.find(EventMaster.class, id);
    }


    @Override
    public List<IEventMaster> findAll() {

        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(EventMaster.class).list();
    }
}
