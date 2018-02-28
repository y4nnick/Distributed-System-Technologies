package dst.ass1.nosql;

import javax.persistence.EntityManager;

import com.mongodb.DB;
import dst.ass1.nosql.impl.MongoDBDataLoader;
import dst.ass1.nosql.impl.MongoDBQuery;

public class MongoDbFactory {

	public IMongoDbDataLoader createDataLoader(EntityManager entityManager) {

		return new MongoDBDataLoader(entityManager);
	}

	public IMongoDbQuery createQuery(DB db) {

		return new MongoDBQuery(db);
	}

}
