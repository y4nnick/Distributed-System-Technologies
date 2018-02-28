package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_2a03 extends AbstractTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQuery() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em.createNamedQuery(Constants.Q_ALLFINISHEDEVENTS);

			List<IEvent> result = (List<IEvent>) query.getResultList();
			assertNotNull(result);
			assertEquals(1, result.size());

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

			IEvent ent5 = daoFactory.getEventDAO().findById(
					testData.entity5_1Id);
			ent5.getEventStreaming().setStatus(EventStatus.SCHEDULED);

			Query query = em.createNamedQuery(Constants.Q_ALLFINISHEDEVENTS);

			List<IEvent> result = (List<IEvent>) query.getResultList();
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
