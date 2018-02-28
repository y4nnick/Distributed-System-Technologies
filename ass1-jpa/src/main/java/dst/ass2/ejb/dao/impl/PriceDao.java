package dst.ass2.ejb.dao.impl;

import dst.ass2.ejb.dao.IPriceDAO;
import dst.ass2.ejb.model.IPrice;
import dst.ass2.ejb.model.impl.Price;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by amra.
 */
public class PriceDao implements IPriceDAO {



    private EntityManager entityManager;

    public  PriceDao(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    @Override
    public IPrice findById(Long id) {
        return entityManager.find(Price.class, id);
    }

    @Override
    public List<IPrice> findAll() {
        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(Price.class).list();
    }
}
