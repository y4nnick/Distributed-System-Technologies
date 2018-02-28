package dst.ass1.jpa;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IStreamingServer;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass1.jpa.util.test.TestData;

public class Test_1a03 extends AbstractTest {

	@Test
	public void testEntity2NameConstraint() {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			// Entity #2
			IStreamingServer ent2_2 = daoFactory.getStreamingServerDAO().findById(
					testData.entity2_1Id);
			ent2_2.setName(TestData.N_ENT2_2);
			em.persist(ent2_2);
			em.flush();

		} catch (PersistenceException e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				isConstraint = true;
			}

		} finally {
			tx.rollback();
		}

		assertTrue(isConstraint);
	}

	@Test
	public void testEntity2NameConstraintJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_STREAMINGSERVER, "name", false,
				em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}

}
