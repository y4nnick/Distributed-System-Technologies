package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2a01_1 extends AbstractTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQuery() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_EVENTMASTERSSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
			query.setParameter("minNr", 2L);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(1, result.size());

			assertTrue(getEventMasterIds(result).contains(testData.entity8_1Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQuery_2() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_EVENTMASTERSSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", "foobar");
			query.setParameter("minNr", 2L);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(0, result.size());

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQuery_3() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_EVENTMASTERSSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
			query.setParameter("minNr", 0L);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(2, result.size());

			List<Long> ids = getEventMasterIds(result);
			assertTrue(ids.contains(testData.entity8_1Id));
			assertTrue(ids.contains(testData.entity8_2Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQuery_4() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_EVENTMASTERSSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
			query.setParameter("minNr", 1L);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(2, result.size());

			List<Long> ids = getEventMasterIds(result);
			assertTrue(ids.contains(testData.entity8_1Id));
			assertTrue(ids.contains(testData.entity8_2Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQuery_5() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_EVENTMASTERSSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
			query.setParameter("minNr", 3L);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(0, result.size());

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQuery_6() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_EVENTMASTERSSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", "invalidName");
			query.setParameter("minNr", 2L);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(0, result.size());

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}
	
	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
