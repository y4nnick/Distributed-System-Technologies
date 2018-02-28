package dst.ass1.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_1b02 extends AbstractTest {

	@Test
	public void testEventMasterEventMasternameConstraint() throws SQLException,
			ClassNotFoundException {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			IEventMaster ent8 = daoFactory.getEventMasterDAO().findById(
					testData.entity8_1Id);
			ent8.setEventMasterName(dst.ass1.jpa.util.test.TestData.N_ENT8_2);
			em.persist(ent8);
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
	public void testEventMasterEventMasternameConstraintJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_EVENTMASTER, Constants.M_EVENTMASTERNAME,
				false, em));
	}

	@Test
	public void testEventMasterNotNullConstraint() {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			IEventMaster ent8 = daoFactory.getEventMasterDAO().findById(
					testData.entity8_1Id);
			ent8.setEventMasterName(null);
			em.persist(ent8);
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
	public void testEventMasterNotNullConstraintJdbc()
			throws ClassNotFoundException, SQLException {
		assertFalse(DatabaseHelper.isNullable(Constants.T_EVENTMASTER, Constants.M_EVENTMASTERNAME,
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
