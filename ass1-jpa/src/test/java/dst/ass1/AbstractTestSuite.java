package dst.ass1;

import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import dst.ass1.jpa.util.PersistenceUtil;

public abstract class AbstractTestSuite {

	private static EntityManagerFactory emf;

	@BeforeClass
	public static void setUpClass() {
		try {
			emf = Persistence
					.createEntityManagerFactory(PersistenceUtil.PERSISTENCE_UNIT_NAME);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@AfterClass
	public static void tearDownClass() throws SQLException {
		emf.close();
	}

	public static EntityManagerFactory getEmf() {
		if (emf == null) {
			setUpClass();
		}
		return emf;
	}

	public static void setEmf(EntityManagerFactory emf) {
		AbstractTestSuite.emf = emf;
	}

}