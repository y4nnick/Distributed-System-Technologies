package dst.ass1.nosql.impl;

import com.mongodb.*;
import com.mongodb.util.JSON;
import dst.ass1.jpa.util.Constants;
import dst.ass1.nosql.IMongoDbQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amra.
 */
public class MongoDBQuery implements IMongoDbQuery {

    private DB db;
    public MongoDBQuery(DB db) {
        this.db=db;
    }

    @Override
    public Long findFinishedByEventId(Long id) {
        DBCollection collection = db.getCollection(Constants.COLL_EVENTDATA);

        DBObject dbObject = collection.findOne(new BasicDBObject().append("event_id", id));

        if (dbObject == null) {
            return null;
        } else {
            return (Long) dbObject.get("event_finished");
        }
    }

    @Override
    public List<Long> findFinishedGt(Long time) {
        DBCollection collection = db.getCollection(Constants.COLL_EVENTDATA);

        BasicDBObject o1 = new BasicDBObject().append("event_finished", new BasicDBObject().append("$gt", time));

        DBCursor dbCursor = collection.find(o1, new BasicDBObject().append("event_id", 1));
        System.out.println("findFinishedGt: Found "+dbCursor.size()+" events!");

        List<Long> IDlistresults = new ArrayList<>();

        while (dbCursor.hasNext()) {
            DBObject o = dbCursor.next();
            System.out.println("findFinishedGt: Found event "+JSON.serialize(o));
            IDlistresults.add( (Long) o.get("event_id"));
        }

        return IDlistresults;
    }

    @Override
    public List<DBObject> mapReduceStreaming() {


        DBCollection collection = db.getCollection(Constants.COLL_EVENTDATA);

        String m =  "function() { " +
                "    for (var maps in this) { " +
                "        switch (maps) { " +
                "        case 'logs': emit('logs', 1); break;" +
                "        case 'matrix': emit('matrix', 1); break;" +
                "        case 'alignment_block': emit('alignment_block', 1); break; " +
                "        default: break;"  +
                "        } " +
                "    }" +
                "}";

        System.out.println("map function " + m);

        String r = "function (key, values) {" +
                "    return values.length; " +
                "}";
        System.out.println("reduce function " +r);

        MapReduceOutput out = collection.mapReduce(m, r, null, MapReduceCommand.OutputType.INLINE, null);

        List<DBObject> dbObjects = new ArrayList<>();

        for(DBObject dbObject : out.results()){
            dbObjects.add(dbObject);
            System.out.println("mapReduceStreaming result: " +dbObject);
        }

        return dbObjects;
    }
}
