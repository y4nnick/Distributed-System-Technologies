package dst.ass1;

import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.mongodb.DB;
import com.mongodb.Mongo;

import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.dao.IEventDAO;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.util.test.TestData;

public class NoSQLTestData extends TestData {

	@SuppressWarnings("unused")
	private static DB db;
	@SuppressWarnings("unused")
	private static MongodProcess mongoProcess;
	private static MongodExecutable mongodExecutable;

	static {
		try {
			// Get open port
			final ServerSocket socket = new ServerSocket(27017);
			final int port = socket.getLocalPort();
			socket.close();

			// create runtime
			final MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
			mongodExecutable = runtime.prepare(new MongodConfig(Version.V2_1_2,
					port, false));
			mongoProcess = mongodExecutable.start();

			// create database
			final Mongo mongo = new Mongo("localhost", port);
			db = mongo.getDB(UUID.randomUUID().toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public NoSQLTestData(EntityManager em) {
		super(em);
	}

	public void insertTestData() throws NoSuchAlgorithmException {
		super.insertTestData();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		IEventDAO ent5DAO = (new DAOFactory(em)).getEventDAO();
		IEvent ent5_4 = ent5DAO.findById(entity5_4Id);
		if (ent5_4 != null) {
			em.remove(ent5_4);
			em.flush();
		}

		tx.commit();
	}

}
