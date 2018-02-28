package dst.ass1.nosql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

import dst.ass1.AbstractNoSQLTest;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.util.Constants;

public class Test_5a extends AbstractNoSQLTest {

	@Test
	public void testMongoDataLoader() throws Exception {
		MongoTestData mongoTestData = new MongoTestData();
		Mongo mongo = new Mongo();
		DB db = mongo.getDB(Constants.MONGO_DB_NAME);
		DBCollection collection = db.getCollection(Constants.COLL_EVENTDATA);
		DBCursor cursor = collection.find();
		ArrayList<DBObject> dbData = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			dbData.add(cursor.next());
		}
		cursor.close();

		List<IEvent> list = daoFactory.getEventDAO().findAll();

		assertEquals(list.size(), dbData.size());

		for (IEvent ent5 : list) {
			boolean isFound = false;
			for (DBObject obj : dbData) {
				if (Long.valueOf(obj.get(Constants.I_EVENT).toString())
						.equals(ent5.getId())) {
					isFound = true;
					assertEquals(
							ent5.getEventStreaming().getEnd().getTime(),
							Long.valueOf(
									obj.get(Constants.PROP_EVENTFINISHED)
											.toString()).longValue());
					String desc = mongoTestData.getDataDescription(ent5.getId());
					assertEquals(obj.get(desc),
							JSON.parse(mongoTestData.getData(ent5.getId())));
				}
			}
			assertTrue(isFound);
		}
	}

}
