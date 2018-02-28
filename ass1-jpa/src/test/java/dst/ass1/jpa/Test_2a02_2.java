package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_2a02_2 extends AbstractTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testQuery() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			ModelFactory modelFactory = new ModelFactory();

			IEventMaster ent8 = daoFactory.getEventMasterDAO().findById(
					testData.entity8_2Id);
			assertNotNull(ent8);

			IMetadata ent6_1 = modelFactory.createMetadata();
			ent6_1.setGame("ctx_env");
			ent6_1.addSetting("param");
			em.persist(ent6_1);

			IEventStreaming ent4_1 = modelFactory.createEventStreaming();
			ent4_1.setStart(new Date(System.currentTimeMillis() - 18000000));
			ent4_1.setEnd(new Date());
			ent4_1.setStatus(EventStatus.SCHEDULED);

			IUplink ent3_1 = daoFactory.getUplinkDAO().findById(
					testData.entity3_1Id);
			assertNotNull(ent3_1);
			ent4_1.addUplinks(ent3_1);
			ent3_1.addEventStreaming(ent4_1);

			IEvent ent5 = modelFactory.createEvent();
			ent5.setAttendingViewers(2);
			ent5.setStreamingTime(0);
			ent5.setMetadata(ent6_1);
			ent5.setEventStreaming(ent4_1);
			ent5.setEventMaster(ent8);
			ent8.addEvent(ent5);

			em.persist(ent4_1);
			em.persist(ent3_1);
			em.persist(ent5);
			em.persist(ent8);

			tx.commit();

			Query query = em.createNamedQuery(Constants.Q_MOSTACTIVEEVENTMASTER);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(1, result.size());

			List<Long> ids = getEventMasterIds(result);

			assertTrue(ids.contains(testData.entity8_1Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
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
