package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.Query;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_2a02_3 extends AbstractTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testQuery() {
		try {

			Query query = em.createNamedQuery(Constants.Q_MOSTACTIVEEVENTMASTER);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(0, result.size());
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

	}
}
