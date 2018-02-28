package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMOSPlatformDAO;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.impl.MOSPlatform;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by amra.
 */
public class MOSPlatformDAO implements IMOSPlatformDAO {

    private EntityManager entityManager;

    public  MOSPlatformDAO(EntityManager entityManager) {
        this.entityManager=entityManager;
    }


    @Override
    public List<IMOSPlatform> findByName(String name) {


        SimpleExpression e = Restrictions.eq("name", name);

        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(MOSPlatform.class).add(e).list();
    }

    @Override
    public IMOSPlatform findById(Long id) {

        return entityManager.find(MOSPlatform.class, id);
    }

    @Override
    public List<IMOSPlatform> findAll() {

        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(MOSPlatform.class).list();
    }
}
