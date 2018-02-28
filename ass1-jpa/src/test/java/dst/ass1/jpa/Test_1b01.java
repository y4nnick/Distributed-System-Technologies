package dst.ass1.jpa;

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

public class Test_1b01 extends AbstractTest {

	@Test
	public void testEventMasterAccountNoBankCodeConstraint() throws SQLException,
			ClassNotFoundException {

		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			IEventMaster ent8 = daoFactory.getEventMasterDAO().findById(
					testData.entity8_1Id);
			ent8.setAccountNo("account2");
			ent8.setBankCode("bank2");
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
	public void testEventMasterAccountNoBankCodeConstraintJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_EVENTMASTER, Constants.M_ACCOUNT, false,
				em));
		assertTrue(DatabaseHelper.isIndex(Constants.T_EVENTMASTER, Constants.M_BANKCODE, false,
				em));
		assertTrue(DatabaseHelper.isComposedIndex(Constants.T_EVENTMASTER,
				Constants.M_ACCOUNT, Constants.M_BANKCODE, em));

		assertTrue(DatabaseHelper.isNullable(Constants.T_EVENTMASTER, Constants.M_ACCOUNT, em));
		assertTrue(DatabaseHelper.isNullable(Constants.T_EVENTMASTER, Constants.M_BANKCODE, em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
