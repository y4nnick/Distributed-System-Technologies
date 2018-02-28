package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_1a09 extends AbstractTest {

	@Test
	public void testEntity5Entity4Association() {
		List<IEvent> ent5_list = daoFactory.getEventDAO().findAll();

		assertNotNull(ent5_list);
		assertEquals(4, ent5_list.size());

		IEvent ent5_1 = daoFactory.getEventDAO().findById(
				testData.entity5_1Id);
		IEvent ent5_2 = daoFactory.getEventDAO().findById(
				testData.entity5_2Id);
		IEvent ent5_3 = daoFactory.getEventDAO().findById(
				testData.entity5_3Id);
		IEvent ent5_4 = daoFactory.getEventDAO().findById(
				testData.entity5_4Id);

		assertEquals(testData.entity5_1Id, ent5_1.getId());
		assertEquals(testData.entity4_1Id, ent5_1.getEventStreaming().getId());

		assertEquals(testData.entity5_2Id, ent5_2.getId());
		assertEquals(testData.entity4_2Id, ent5_2.getEventStreaming().getId());

		assertEquals(testData.entity5_3Id, ent5_3.getId());
		assertEquals(testData.entity4_3Id, ent5_3.getEventStreaming().getId());

		assertEquals(testData.entity5_4Id, ent5_4.getId());
		assertEquals(testData.entity4_4Id, ent5_4.getEventStreaming().getId());

		List<IEventStreaming> list = daoFactory.getEventStreamingDAO()
				.findAll();

		assertNotNull(list);
		assertEquals(4, list.size());

		IEventStreaming ent4_1 = daoFactory.getEventStreamingDAO()
				.findById(testData.entity4_1Id);
		IEventStreaming ent4_2 = daoFactory.getEventStreamingDAO()
				.findById(testData.entity4_2Id);
		IEventStreaming ent4_3 = daoFactory.getEventStreamingDAO()
				.findById(testData.entity4_3Id);
		IEventStreaming ent4_4 = daoFactory.getEventStreamingDAO()
				.findById(testData.entity4_4Id);

		assertEquals(testData.entity4_1Id, ent4_1.getId());
		assertEquals(EventStatus.FINISHED, ent4_1.getStatus());
		assertEquals(testData.entity5_1Id, ent4_1.getEvent().getId());

		assertEquals(testData.entity4_2Id, ent4_2.getId());
		assertEquals(EventStatus.SCHEDULED, ent4_2.getStatus());
		assertEquals(testData.entity5_2Id, ent4_2.getEvent().getId());

		assertEquals(testData.entity4_3Id, ent4_3.getId());
		assertEquals(EventStatus.SCHEDULED, ent4_3.getStatus());
		assertEquals(testData.entity5_3Id, ent4_3.getEvent().getId());

		assertEquals(testData.entity4_4Id, ent4_4.getId());
		assertEquals(EventStatus.SCHEDULED, ent4_4.getStatus());
		assertEquals(testData.entity5_4Id, ent4_4.getEvent().getId());
	}

	@Test
	public void testEntity5OptionalConstraint() {
		IEvent ent5 = daoFactory.getEventDAO().findById(
				testData.entity5_1Id);
		assertNotNull(ent5);

		boolean isConstraint = false;
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			ent5.setEventStreaming(null);
			em.persist(ent5);
			tx.commit();
		} catch (Exception e) {
			if (e.getCause().getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				isConstraint = true;
			}
		}

		assertTrue(isConstraint);
	}

	@Test
	public void testEntity5Entity4AssociationJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_EVENT,
				Constants.I_EVENTSTREAMING, em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
