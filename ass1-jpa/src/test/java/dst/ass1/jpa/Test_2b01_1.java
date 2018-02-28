package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_2b01_1 extends AbstractTest {

	@Test
	public void testNamedQueryExists() {
		boolean exists = false;
		try {
			em.createNamedQuery(Constants.Q_STREAMINGSERVERSOFMODERATOR);
			exists = true;
		} catch (Exception e) {
			exists = false;
		}
		assertTrue(exists);
	}

	@Test
	public void testQuery() {

		// retrieve a new/fresh EntityManager, so that no entities can be cached
		EntityManager freshEM = getFreshEntityManager();

		DAOFactory daoFactory = new DAOFactory(freshEM);
		HashMap<IModerator, Date> map = daoFactory.getModeratorDAO()
				.findNextStreamingServerMaintenanceByModerators();
		assertEquals(3, map.size());

		boolean isId1 = false;
		boolean isId2 = false;
		boolean isId3 = false;

		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<?, ?> pairs = (Map.Entry<?, ?>) it.next();
			long id = ((IPerson) pairs.getKey()).getId();
			Date value = (Date) pairs.getValue();

			if (id == testData.entity7_1Id) {
				isId1 = true;
				assertEquals(new Date(0), value);
			} else if (id == testData.entity7_2Id) {
				isId2 = true;
				assertEquals(new Date(1), value);
			} else if (id == testData.entity7_3Id) {
				isId3 = true;
				assertEquals(new Date(2), value);
			}
		}

		assertTrue(isId1);
		assertTrue(isId2);
		assertTrue(isId3);

		freshEM.clear();
		freshEM.close();
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
