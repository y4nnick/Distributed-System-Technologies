package dst.ass1.nosql;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import dst.ass1.AbstractNoSQLTest;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.util.Constants;

public class Test_5b extends AbstractNoSQLTest {

	@Test
	public void testFindFinishedQuery() throws UnknownHostException,
			MongoException {
		Mongo mongo = new Mongo();
		DB db = mongo.getDB(Constants.MONGO_DB_NAME);

		IMongoDbQuery mongoQuery = mongoDbFactory.createQuery(db);

		List<IEvent> list = daoFactory.getEventDAO().findAll();
		for (IEvent ent5 : list) {
			assertEquals(ent5.getEventStreaming().getEnd().getTime(), mongoQuery
					.findFinishedByEventId(ent5.getId()).longValue());
		}

		mongo.close();
	}

	@Test
	public void testFinishedGt() throws UnknownHostException, MongoException {
		long time = 1325397600;
		Mongo mongo = new Mongo();
		DB db = mongo.getDB(Constants.MONGO_DB_NAME);
		IMongoDbQuery mongoQuery = mongoDbFactory.createQuery(db);

		List<IEvent> list = daoFactory.getEventDAO().findAll();
		ArrayList<Long> ids1 = new ArrayList<Long>();
		for (IEvent ent5 : list) {
			if (ent5.getEventStreaming().getEnd().getTime() > time)
				ids1.add(ent5.getId());
		}

		List<Long> ids2 = mongoQuery.findFinishedGt(time);

		assertEquals(ids2.size(), ids1.size());

		Collections.sort(ids1);
		Collections.sort(ids2);

		for (int i = 0; i < ids1.size(); i++) {
			assertEquals(ids1.get(i), ids2.get(i));
		}

	}
}
