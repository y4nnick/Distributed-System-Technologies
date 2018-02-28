package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

public class Test_2a01_2 extends AbstractTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQuery() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em.createNamedQuery(Constants.Q_EVENTMASTERSSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
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

}
