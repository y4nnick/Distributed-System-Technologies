package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMembershipDAO;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.impl.Membership;
import dst.ass1.jpa.model.impl.MembershipKey;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by amra.
 */
public class MembershipDAO implements IMembershipDAO {

    private EntityManager entityManager;

    public  MembershipDAO (EntityManager entityManager) {
        this.entityManager=entityManager;
    }


    @Override
    public List<IMembership> findByEventMasterAndPlatform(IEventMaster eventMaster, IMOSPlatform platform) {
        MembershipKey ms = new MembershipKey();

        ms.setEventMaster(eventMaster);
        ms.setMOSPlatform(platform);
        Criteria criteria = entityManager.unwrap(org.hibernate.Session.class).createCriteria("membershipId");

        return criteria.add(Example.create(ms)).list();
    }

    @Override
    public IMembership findById(Long id) {

        return entityManager.find(Membership.class, id);
    }

    @Override
    public List<IMembership> findAll() {
        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(Membership.class).list();
    }
}
