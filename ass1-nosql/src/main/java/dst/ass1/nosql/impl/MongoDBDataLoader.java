package dst.ass1.nosql.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import dst.ass1.jpa.model.impl.Event;
import dst.ass1.jpa.util.Constants;
import dst.ass1.nosql.IMongoDbDataLoader;
import dst.ass1.nosql.MongoTestData;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by amra.
 */
public class MongoDBDataLoader implements IMongoDbDataLoader {

    private EntityManager entityManager;

    public MongoDBDataLoader(EntityManager em) {
        this.entityManager=em;
    }


    @Override
    public void loadData() throws Exception {

        MongoTestData mongoTestData = new MongoTestData();
        Mongo mongo = new Mongo();
        DB db = mongo.getDB(Constants.MONGO_DB_NAME);
        // get collection from mongodb
        DBCollection collection = db.getCollection(Constants.COLL_EVENTDATA);

        List<Event> events = entityManager.createNamedQuery(Constants.Q_ALLFINISHEDEVENTS, Event.class).getResultList();

        // get the data from hibernate and insert into mongodb
        for(Event event : events) {
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.append("event_id", event.getId());
            dbObject.append("event_finished", event.getEventStreaming().getEnd().getTime());
            // mongodb needs JSON objects so PARSE the string from the test data
            dbObject.append(mongoTestData.getDataDescription(event.getId()), JSON.parse(mongoTestData.getData(event.getId())) );
            collection.insert(dbObject);
        }

        collection.ensureIndex(new BasicDBObject().append("event_id", 1));
    }
}
