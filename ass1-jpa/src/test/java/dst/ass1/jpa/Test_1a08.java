package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_1a08 extends AbstractTest {

	@Test
	public void testEventMasterEntity5Association() {
		IEventMaster ent8 = daoFactory.getEventMasterDAO().findById(
				testData.entity8_1Id);
		assertNotNull(ent8);
		assertNotNull(ent8.getEvents());

		List<Long> ent5Ids = getEventIds(ent8.getEvents());

		assertEquals(3, ent5Ids.size());

		assertTrue(ent5Ids.contains(testData.entity5_1Id));
		assertTrue(ent5Ids.contains(testData.entity5_3Id));
		assertTrue(ent5Ids.contains(testData.entity5_4Id));

		IEvent ent5_1 = daoFactory.getEventDAO().findById(
				testData.entity5_1Id);
		IEvent ent5_2 = daoFactory.getEventDAO().findById(
				testData.entity5_2Id);

		assertNotNull(ent5_1);
		assertNotNull(ent5_2);

		assertEquals(testData.entity8_1Id,
				ent5_1.getEventMaster().getId());
		assertEquals(testData.entity8_2Id,
				ent5_2.getEventMaster().getId());
	}

	@Test
	public void testEventMasterEntity5AssociationJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_EVENT,
				Constants.I_EVENTMASTER, em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
