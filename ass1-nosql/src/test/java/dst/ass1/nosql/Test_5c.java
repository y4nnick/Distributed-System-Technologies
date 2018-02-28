package dst.ass1.nosql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import dst.ass1.AbstractNoSQLTest;
import dst.ass1.jpa.util.Constants;

public class Test_5c extends AbstractNoSQLTest {

	@Test
	public void testMapReduceStreaming() throws UnknownHostException,
			MongoException {
		Mongo mongo = new Mongo();
		DB db = mongo.getDB(Constants.MONGO_DB_NAME);
		IMongoDbQuery mongoQuery = mongoDbFactory.createQuery(db);

		List<DBObject> dbObjects = mongoQuery.mapReduceStreaming();
		boolean a[] = new boolean[dbObjects.size()];
		assertEquals(3, dbObjects.size());
		for (int i = 0; i < dbObjects.size(); i++) {
			DBObject obj = dbObjects.get(i);

			if (obj.get("_id") != null
					&& obj.get("_id").equals(MongoTestData.DATA_DESC_BLOCK)) {
				assertTrue(Double.valueOf(obj.get("value").toString())
						.compareTo(1.0) == 0);
				a[i] = true;
			} else if (obj.get("_id") != null && obj.get("_id").equals(MongoTestData.DATA_DESC_LOGS)) {
				assertTrue(Double.valueOf(obj.get("value").toString())
						.compareTo(1.0) == 0);
				a[i] = true;
			} else if (obj.get("_id") != null
					&& obj.get("_id").equals(MongoTestData.DATA_DESC_MATRIX)) {
				assertTrue(Double.valueOf(obj.get("value").toString())
						.compareTo(1.0) == 0);
				a[i] = true;
			}
		}

		for (int i = 0; i < a.length; i++)
			assertTrue(a[i]);
	}

}
