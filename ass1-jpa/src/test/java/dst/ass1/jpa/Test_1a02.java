package dst.ass1.jpa;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass1.jpa.util.test.TestData;

public class Test_1a02 extends AbstractTest {

	@Test
	public void testEntity1Constraint() {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			// Entity #1
			IMOSPlatform ent1_1 = modelFactory.createPlatform();
			ent1_1.setName(TestData.N_ENT1_1);

			IMOSPlatform ent1_2 = modelFactory.createPlatform();
			ent1_2.setName(TestData.N_ENT1_1);

			em.persist(ent1_1);
			em.persist(ent1_2);
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
	public void testEntity1ConstraintJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_MOSPLATFORM, "name", false,
				em));
		assertTrue(DatabaseHelper.isNullable(Constants.T_MOSPLATFORM, "name", em));

	}

}
