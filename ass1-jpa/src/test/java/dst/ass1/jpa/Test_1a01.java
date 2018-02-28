package dst.ass1.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_1a01 extends AbstractTest {
	
	@Test
	public void testModelFactory() {
		assertNotNull(modelFactory.createAddress());
		assertNotNull(modelFactory.createModerator());
		assertNotNull(modelFactory.createStreamingServer());
		assertNotNull(modelFactory.createUplink());
		assertNotNull(modelFactory.createMetadata());
		assertNotNull(modelFactory.createEventStreaming());
		assertNotNull(modelFactory.createPlatform());
		assertNotNull(modelFactory.createEvent());
		assertNotNull(modelFactory.createMembership());
		assertNotNull(modelFactory.createEventMaster());
	}

	@Test
	public void testDAOFactory() {
		DAOFactory daoDummy = new DAOFactory(em);

		assertNotNull(daoDummy.getModeratorDAO());
		assertNotNull(daoDummy.getStreamingServerDAO());
		assertNotNull(daoDummy.getUplinkDAO());
		assertNotNull(daoDummy.getMetadataDAO());
		assertNotNull(daoDummy.getEventStreamingDAO());
		assertNotNull(daoDummy.getPlatformDAO());
		assertNotNull(daoDummy.getEventDAO());
		assertNotNull(daoDummy.getMembershipDAO());
		assertNotNull(daoDummy.getEventMasterDAO());
	}

	@Test
	public void testEntities() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			EntityTransaction tx = em.getTransaction();

			if (tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception e1) {
				}
			}
			
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
