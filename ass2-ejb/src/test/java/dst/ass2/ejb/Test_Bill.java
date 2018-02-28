package dst.ass2.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.util.test.TestData;
import dst.ass2.EJBBaseTest;
import dst.ass2.ejb.dto.BillDTO;
import dst.ass2.ejb.dto.BillDTO.BillPerEventMaster;

public class Test_Bill extends EJBBaseTest {

	@Before
	public void setUp() throws Exception {
		testingBean.insertTestData();
		managementBean.clearPriceCache();
		removeEventsFromDB(true);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetBill_forEventMaster1() throws Exception {
		// get all available ids from db
		List<Long> ids = getPlatformIdsFromDB();

		assertEquals(2, ids.size());

		// add prices to management bean
		managementBean_addPrices();

		// add events
		addEventsForEventMaster1(ids);

		// finish all events on db level!
		finishEventsInDB();

		Future<BillDTO> result = managementBean
				.getBillForEventMaster(TestData.N_ENT8_1);
		while (!result.isDone()) {
			Thread.sleep(100);
		}

		// gather all paid events for eventMaster
		List<Long> paid = findPaidEventsForEventMasterInDB(TestData.N_ENT8_1);

		assertEquals(3, paid.size());

		BillDTO billDTO = result.get();
		assertNotNull(billDTO);

		List<BillPerEventMaster> bills = billDTO.getBills();
		assertNotNull(bills);
		assertEquals(3, bills.size());

		Map<Long, BillPerEventMaster> temp = createMap(bills);

		BillPerEventMaster bill = temp.get(paid.get(0));

		assertNotNull(bill);
		assertNotNull(bill.getNumberOfUplinks());
		assertTrue(bill.getNumberOfUplinks() >= 1);
		assertNotNull(bill.getSetupCosts());

		final BigDecimal expectedSetupCosts_withDiscount = new BigDecimal(45.00);
		final BigDecimal expectedSetupCosts = new BigDecimal(50.00);

		// In case discount was considered only for the total price
		assertTrue(
				generateAssertMessage(expectedSetupCosts_withDiscount,
						expectedSetupCosts, bill.getSetupCosts()),
				bill.getSetupCosts().compareTo(expectedSetupCosts_withDiscount) == 0
						|| bill.getSetupCosts().compareTo(expectedSetupCosts) == 0);

		assertNotNull(bill.getStreamingCosts());

		final BigDecimal expectedStreamingCosts_withDiscount = new BigDecimal(
				135.00);
		final BigDecimal expectedStreamingCosts = new BigDecimal(150.00);

		// In case discount was considered only for the total price
		assertTrue(
				generateAssertMessage(expectedStreamingCosts_withDiscount,
						expectedStreamingCosts, bill.getStreamingCosts()),
				bill.getStreamingCosts().compareTo(
						expectedStreamingCosts_withDiscount) == 0
						|| bill.getStreamingCosts().compareTo(
								expectedStreamingCosts) == 0);

		assertNotNull(bill.getEventCosts());

		final BigDecimal expectedEventCosts = new BigDecimal(180.00);
		assertTrue(
				generateAssertMessage(expectedEventCosts,
						bill.getEventCosts()), bill.getEventCosts()
						.compareTo(expectedEventCosts) == 0);

		bill = temp.get(paid.get(1));

		assertNotNull(bill);
		assertNotNull(bill.getNumberOfUplinks());
		assertTrue(bill.getNumberOfUplinks() >= 1);
		assertNotNull(bill.getSetupCosts());

		final BigDecimal expectedSetupCosts2_withDiscount = new BigDecimal(
				40.50);

		// In case discount was considered only for the total price
		assertTrue(
				generateAssertMessage(expectedSetupCosts2_withDiscount,
						expectedSetupCosts, bill.getSetupCosts()),
				bill.getSetupCosts()
						.compareTo(expectedSetupCosts2_withDiscount) == 0
						|| bill.getSetupCosts().compareTo(
								expectedSetupCosts_withDiscount) == 0);

		assertNotNull(bill.getStreamingCosts());

		final BigDecimal expectedStreamingCosts2 = new BigDecimal(0.00);

		assertTrue(String.format("Expected %s, but was %s",
				expectedStreamingCosts2, bill.getStreamingCosts()), bill
				.getStreamingCosts().compareTo(expectedStreamingCosts2) == 0);

		assertNotNull(bill.getEventCosts());

		final BigDecimal expectedEventCosts2 = new BigDecimal(40.50);

		assertTrue(
				generateAssertMessage(expectedEventCosts2,
						bill.getEventCosts()), bill.getEventCosts()
						.compareTo(expectedEventCosts2) == 0);

		bill = temp.get(paid.get(2));

		assertNotNull(bill);
		assertNotNull(bill.getNumberOfUplinks());
		assertTrue(bill.getNumberOfUplinks() >= 1);
		assertNotNull(bill.getSetupCosts());

		final BigDecimal expectedSetupCosts3_withDiscount = new BigDecimal(
				28.00);
		final BigDecimal expectedSetupCosts3 = new BigDecimal(40.00);

		// In case discount was considered only for the total price
		assertTrue(
				generateAssertMessage(expectedSetupCosts3_withDiscount,
						expectedSetupCosts3, bill.getSetupCosts()),
				bill.getSetupCosts()
						.compareTo(expectedSetupCosts3_withDiscount) == 0
						|| bill.getSetupCosts().compareTo(expectedSetupCosts3) == 0);

		assertNotNull(bill.getStreamingCosts());

		assertTrue(bill.getStreamingCosts()
				.compareTo(expectedStreamingCosts2) == 0);
		assertNotNull(bill.getEventCosts());

		final BigDecimal expectedEventCosts3 = new BigDecimal(28.00);

		assertTrue(
				generateAssertMessage(expectedEventCosts3,
						bill.getEventCosts()), bill.getEventCosts()
						.compareTo(expectedEventCosts3) == 0);

		assertNotNull(billDTO.getTotalPrice());

		final BigDecimal expectedTotalCosts = new BigDecimal(248.50);
		assertTrue(String.format("Expected %s, but was %s", expectedTotalCosts,
				billDTO.getTotalPrice()),
				billDTO.getTotalPrice().compareTo(expectedTotalCosts) == 0);

		assertNotNull(billDTO.getEventMasterName());
		assertTrue(billDTO.getEventMasterName().equals(TestData.N_ENT8_1));

	}

	@Test
	public void testGetBill_forEventMaster2() throws Exception {
		// get all available ids from db
		List<Long> ids = getPlatformIdsFromDB();

		assertEquals(2, ids.size());

		// add prices to management bean
		managementBean_addPrices();

		// add events for eventMaster1
		addEventsForEventMaster2(ids);

		// finish all events on db level!
		finishEventsInDB();

		Future<BillDTO> result = managementBean
				.getBillForEventMaster(TestData.N_ENT8_2);
		while (!result.isDone()) {
			Thread.sleep(100);
		}

		// gather all paid events for eventMaster
		List<Long> paid = findPaidEventsForEventMasterInDB(TestData.N_ENT8_2);

		assertEquals(2, paid.size());

		BillDTO billDTO = result.get();
		assertNotNull(billDTO);

		List<BillPerEventMaster> bills = billDTO.getBills();
		assertNotNull(bills);

		assertEquals(2, bills.size());

		Map<Long, BillPerEventMaster> temp = createMap(bills);

		BillPerEventMaster bill = temp.get(paid.get(0));

		assertNotNull(bill);
		assertNotNull(bill.getNumberOfUplinks());
		assertTrue(bill.getNumberOfUplinks() >= 1);
		assertNotNull(bill.getSetupCosts());

		final BigDecimal expectedSetupCosts_withDiscount = new BigDecimal(40.00);
		final BigDecimal expectedSetupCosts = new BigDecimal(50.00);

		// In case discount was considered only for the total price
		assertTrue(
				generateAssertMessage(expectedSetupCosts_withDiscount,
						expectedSetupCosts, bill.getSetupCosts()),
				bill.getSetupCosts().compareTo(expectedSetupCosts_withDiscount) == 0
						|| bill.getSetupCosts().compareTo(expectedSetupCosts) == 0);

		assertNotNull(bill.getStreamingCosts());

		final BigDecimal expectedStreamingCosts = new BigDecimal(0.00);
		assertTrue(
				generateAssertMessage(expectedStreamingCosts,
						bill.getStreamingCosts()), bill.getStreamingCosts()
						.compareTo(expectedStreamingCosts) == 0);

		assertNotNull(bill.getEventCosts());

		final BigDecimal expectedEventCosts = new BigDecimal(40.00);

		assertTrue(
				generateAssertMessage(expectedEventCosts,
						bill.getEventCosts()), bill.getEventCosts()
						.compareTo(expectedEventCosts) == 0);

		bill = temp.get(paid.get(1));

		assertNotNull(bill);
		assertNotNull(bill.getNumberOfUplinks());
		assertTrue(bill.getNumberOfUplinks() >= 1);
		assertNotNull(bill.getSetupCosts());

		final BigDecimal expectedSetupCosts2_withDiscount = new BigDecimal(
				36.00);
		final BigDecimal expectedSetupCosts2 = new BigDecimal(45.00);

		// In case discount was considered only for the total price
		assertTrue(
				generateAssertMessage(expectedSetupCosts2_withDiscount,
						expectedSetupCosts2, bill.getSetupCosts()),
				bill.getSetupCosts()
						.compareTo(expectedSetupCosts2_withDiscount) == 0
						|| bill.getSetupCosts().compareTo(expectedSetupCosts2) == 0);

		assertNotNull(bill.getStreamingCosts());

		final BigDecimal expectedStreamingCosts2 = new BigDecimal(0.00);
		assertTrue(
				generateAssertMessage(expectedStreamingCosts,
						bill.getStreamingCosts()), bill.getStreamingCosts()
						.compareTo(expectedStreamingCosts2) == 0);

		assertNotNull(bill.getEventCosts());

		final BigDecimal expectedEventCosts2 = new BigDecimal(36.00);
		assertTrue(
				generateAssertMessage(expectedEventCosts2,
						bill.getEventCosts()), bill.getEventCosts()
						.compareTo(expectedEventCosts2) == 0);

		assertNotNull(billDTO.getTotalPrice());

		final BigDecimal expectedTotalPrice = new BigDecimal(76.00);
		assertTrue(
				generateAssertMessage(expectedTotalPrice,
						billDTO.getTotalPrice()), billDTO.getTotalPrice()
						.compareTo(expectedTotalPrice) == 0);

		assertNotNull(billDTO.getEventMasterName());
		assertTrue(billDTO.getEventMasterName().equals(TestData.N_ENT8_2));
	}

	@Test
	public void testGetBill_ForNotExistingEventMaster() {
		String eventMastername = "not_existing_eventMaster";

		try {
			Future<BillDTO> result = managementBean
					.getBillForEventMaster(eventMastername);

			while (!result.isDone()) {
				Thread.sleep(100);
			}

			result.get();

			fail(String.format("EventMaster: %s should not exist!", eventMastername));
		} catch (Exception e) {
			// Expected exception since eventMaster should not exist!
		}

	}

	private Map<Long, BillDTO.BillPerEventMaster> createMap(List<BillDTO.BillPerEventMaster> bills) {
		Map<Long, BillDTO.BillPerEventMaster> ret = new HashMap<Long, BillDTO.BillPerEventMaster>();

		for (BillPerEventMaster bill : bills)
			ret.put(bill.getEventId(), bill);

		return ret;
	}

	private List<Long> findPaidEventsForEventMasterInDB(final String eventMaster) {
		List<Long> paid = new ArrayList<Long>();
		List<IEventStreaming> streamings = daoFactory
				.getEventStreamingDAO().findAll();
		for (IEventStreaming streaming : streamings) {
			IEvent event = streaming.getEvent();
			if (event != null && event.isPaid()
					&& event.getEventMaster().getEventMasterName()
							.equalsIgnoreCase(eventMaster)) {
				paid.add(event.getId());
			}
		}
		return paid;
	}

	private void finishEventsInDB() throws Exception {
		userTransaction.begin();
		List<IEventStreaming> streamings = daoFactory
				.getEventStreamingDAO().findAll();
		for (IEventStreaming stream : streamings) {
			stream.setStatus(EventStatus.FINISHED);
			stream.setEnd(new Date());
			em.persist(stream);
		}
		userTransaction.commit();
	}

	private String generateAssertMessage(final BigDecimal expected,
			final BigDecimal actual) {
		return String.format("Expected %s, but was %s", expected, actual);
	}

	private String generateAssertMessage(final BigDecimal expected,
			final BigDecimal expectedAlternative, final BigDecimal actual) {
		return String.format("Expected either %s or %s, but was %s", expected,
				expectedAlternative, actual);
	}

}
