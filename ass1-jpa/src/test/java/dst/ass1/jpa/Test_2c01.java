package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.IEventDAO;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2c01 extends AbstractTest {

	@Test
	public void findEventsForEventMasterAndGame1() {
		IEventDAO ent5_Dao = daoFactory.getEventDAO();
		List<IEvent> ent5_list = ent5_Dao.findEventsForEventMasterAndGame(
				dst.ass1.jpa.util.test.TestData.N_ENT8_1, TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getEventIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1Id));
	}

	@Test
	public void findEventsForEventMasterAndGame2() {
		IEventDAO ent5_Dao = daoFactory.getEventDAO();
		List<IEvent> ent5_list = ent5_Dao.findEventsForEventMasterAndGame(
				dst.ass1.jpa.util.test.TestData.N_ENT8_1, "invalid_game");
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findEventsForEventMasterAndGame3() {
		IEventDAO ent5_Dao = daoFactory.getEventDAO();
		List<IEvent> ent5_list = ent5_Dao.findEventsForEventMasterAndGame(
				dst.ass1.jpa.util.test.TestData.N_ENT8_2, TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findEventsForEventMasterAndGame4() {
		IEventDAO ent5_Dao = daoFactory.getEventDAO();
		List<IEvent> ent5_list = ent5_Dao.findEventsForEventMasterAndGame(
				dst.ass1.jpa.util.test.TestData.N_ENT8_2, null);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getEventIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_2Id));
	}

	@Test
	public void findEventsForEventMasterAndGame5() {
		IEventDAO ent5_Dao = daoFactory.getEventDAO();
		List<IEvent> ent5_list = ent5_Dao.findEventsForEventMasterAndGame(
				"eventMaster5", TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findEventsForEventMasterAndGame6() {
		IEventDAO ent5_Dao = daoFactory.getEventDAO();
		List<IEvent> ent5_list = ent5_Dao.findEventsForEventMasterAndGame(
				null, TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getEventIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1Id));
	}

	@Test
	public void findEventsForEventMasterAndGame7() {
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			IEvent ent5 = daoFactory.getEventDAO().findById(
					testData.entity5_3Id);
			ent5.getMetadata().setGame(TestData.N_ENT6_1);

			em.persist(ent5);

			tx.commit();

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

		IEventDAO ent5_Dao = daoFactory.getEventDAO();
		List<IEvent> ent5 = ent5_Dao.findEventsForEventMasterAndGame(
				dst.ass1.jpa.util.test.TestData.N_ENT8_1, TestData.N_ENT6_1);
		assertNotNull(ent5);
		assertEquals(2, ent5.size());

		List<Long> ids = getEventIds(ent5);
		assertTrue(ids.contains(testData.entity5_1Id));
		assertTrue(ids.contains(testData.entity5_3Id));
	}

	@Test
	public void findEventsForEventMasterAndGame8() {
		IEventDAO ent5_Dao = daoFactory.getEventDAO();
		List<IEvent> ent5_list = ent5_Dao.findEventsForEventMasterAndGame(
				null, null);
		assertNotNull(ent5_list);
		assertEquals(4, ent5_list.size());

		List<Long> ids = getEventIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1Id));
		assertTrue(ids.contains(testData.entity5_2Id));
		assertTrue(ids.contains(testData.entity5_3Id));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
