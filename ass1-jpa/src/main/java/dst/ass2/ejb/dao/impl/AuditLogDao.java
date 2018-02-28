package dst.ass2.ejb.dao.impl;

import dst.ass2.ejb.dao.IAuditLogDAO;
import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.impl.AuditLog;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by amra.
 */
public class AuditLogDao implements IAuditLogDAO {


    private EntityManager entityManager;

    public  AuditLogDao(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    @Override
    public IAuditLog findById(Long id) {

        return entityManager.find(AuditLog.class, id);
    }

    @Override
    public List<IAuditLog> findAll() {
        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(AuditLog.class).list();
    }
}
