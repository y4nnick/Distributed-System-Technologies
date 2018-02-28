package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMetadataDAO;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.impl.Metadata;


import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by amra.
 */
public class MetadataDAO implements IMetadataDAO {

    private EntityManager entityManager;

    public MetadataDAO(EntityManager entityManager) {
      this.entityManager=entityManager;
    }


    @Override
    public IMetadata findById(Long id) {

        return entityManager.find(Metadata.class, id);
    }

    @Override
    public List<IMetadata> findAll() {

        return entityManager.unwrap(org.hibernate.Session.class).createCriteria(Metadata.class).list();
    }
}
