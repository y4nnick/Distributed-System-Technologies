package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_2c02 extends AbstractTest {

	@Test
	public void testFindEventsForStatusFinishedStartandFinish1() {
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			
			IEvent ent5 = daoFactory.getEventDAO().findById(
					testData.entity5_1Id);
			ent5.getEventStreaming().setStatus(EventStatus.SCHEDULED);

			em.persist(ent5);

			List<IEvent> list = daoFactory.getEventDAO()
					.findEventsForStatusFinishedStartandFinish(null, null);
			boolean check = list == null || list.size() == 0;
			assertTrue(check);
		} finally {
			if (tx != null) {
				tx.rollback();
			}
		}
	}

	@Test
	public void testFindEventsForStatusFinishedStartandFinish2() {
		List<IEvent> list = daoFactory.getEventDAO()
				.findEventsForStatusFinishedStartandFinish(null, null);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(testData.entity5_1Id, list.get(0).getId());
	}

	@Test
	public void testFindEventsForStatusFinishedStartandFinish3() {
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			IEvent ent5 = daoFactory.getEventDAO().findById(
					testData.entity5_1Id);
			ent5.getEventStreaming().setStatus(EventStatus.FINISHED);
			ent5.getEventStreaming().setStart(createDate(2012, 1, 20));
			ent5.getEventStreaming().setEnd(createDate(2012, 11, 30));

			em.persist(ent5);

			tx.commit();

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

		List<IEvent> events = daoFactory.getEventDAO()
				.findEventsForStatusFinishedStartandFinish(
						createDate(2012, 1, 1), createDate(2012, 12, 31));
		assertNotNull(events);
		assertEquals(0, events.size());

		events = daoFactory.getEventDAO()
				.findEventsForStatusFinishedStartandFinish(
						createDate(2012, 1, 1), createDate(2012, 10, 1));
		assertNotNull(events);
		assertEquals(0, events.size());

		events = daoFactory.getEventDAO()
				.findEventsForStatusFinishedStartandFinish(
						createDate(2012, 10, 1), createDate(2012, 12, 1));
		assertNotNull(events);
		assertEquals(0, events.size());

		events = daoFactory.getEventDAO()
				.findEventsForStatusFinishedStartandFinish(
						createDate(2012, 1, 20), createDate(2012, 11, 30));
		assertEquals(1, events.size());
		assertEquals(testData.entity5_1Id, events.get(0).getId());

		events = daoFactory.getEventDAO()
				.findEventsForStatusFinishedStartandFinish(
						createDate(2012, 1, 20), null);
		assertEquals(1, events.size());
		assertEquals(testData.entity5_1Id, events.get(0).getId());

		events = daoFactory.getEventDAO()
				.findEventsForStatusFinishedStartandFinish(null,
						createDate(2012, 11, 30));
		assertEquals(1, events.size());
		assertEquals(testData.entity5_1Id, events.get(0).getId());

	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}

	private Date createDate(int year, int month, int day) {

		String temp = year + "/" + month + "/" + day;
		Date date = null;

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			date = formatter.parse(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;

	}

}
