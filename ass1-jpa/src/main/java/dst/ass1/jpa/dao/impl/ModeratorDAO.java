package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IModeratorDAO;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.impl.Moderator;
import dst.ass1.jpa.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amra.
 */


public class ModeratorDAO implements IModeratorDAO {

    private EntityManager entityManager;

    public  ModeratorDAO(EntityManager entityManager) {
        this.entityManager=entityManager;
    }


      public HashMap<IModerator, Date> findNextStreamingServerMaintenanceByModerators() {
          List<Object> list = entityManager.createNamedQuery(Constants.Q_STREAMINGSERVERSOFMODERATOR).getResultList();
          HashMap<IModerator, Date> result = new HashMap<>();

          for (Object entry : list) {

              IModerator moderator = (IModerator) entry;
              Query query = entityManager.createNamedQuery("MINStreamingServer");
              query.setParameter("mid", moderator.getId());
              List list2 =query.getResultList();

              for (Object entry2 : list2){
                  result.put(moderator, (Date) entry2);
              }

          }

          return result;
    }



    @Override
    public IModerator findById(Long id) {

        return entityManager.find(Moderator.class, id);
    }

    @Override
    public List<IModerator> findAll() {

        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(Moderator.class).list();
    }
}
