package dst.ass2.ejb;

import static dst.ass2.ejb.util.EJBUtils.lookup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.NoSuchEJBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.util.test.TestData;
import dst.ass2.EJBBaseTest;
import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.EventManagementBean;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.IEventManagementBean;

public class Test_EventManagementBean extends EJBBaseTest {

	private final String game1 = "game1";
	private final String game2 = "game2";

	private IEventManagementBean eventManagementBean;

	@Before
	public void setUp() throws Exception {
		testingBean.insertTestData();
		managementBean.clearPriceCache();
		eventManagementBean = lookup(ctx, EventManagementBean.class);
		// remove all events from db
		removeEventsFromDB(false);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCache() {
		// cache should be empty
		List<AssignmentDTO> cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(0, cache.size());
	}

	@Test
	public void testLogin_With_CorrectCredentials() throws Exception {
		eventManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1);
	}

	@Test(expected = AssignmentException.class)
	public void testLogin_With_InvalidCredentials() throws Exception {
		eventManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1 + "1");
		fail("Login with invalid credentials passed. Expected: "
				+ AssignmentException.class.getName());
	}

	@Test
	public void testAdd_And_RemoveEvents() throws Exception {
		// get the ids from the database
		List<Long> ids = getPlatformIdsFromDB();
		assertEquals(2, ids.size());

		// add 2 events
		eventManagementBean.addEvent(ids.get(0), 1, game1,
				new ArrayList<String>());
		eventManagementBean.addEvent(ids.get(0), 2, game2,
				new ArrayList<String>());

		// there should be 2 events in the cache
		List<AssignmentDTO> cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isEventInCache(new AssignmentDTO(ids.get(0), 1, game1,
				new ArrayList<String>(), null), cache));
		assertTrue(isEventInCache(new AssignmentDTO(ids.get(0), 2, game2,
				new ArrayList<String>(), null), cache));

		// remove events for incorrect value
		eventManagementBean.removeEventsForPlatform(Long.MAX_VALUE);

		// cache should stay the same
		cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(0), 1, game1,
						new ArrayList<String>(), new ArrayList<Long>()), cache));
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(0), 2, game2,
						new ArrayList<String>(), new ArrayList<Long>()), cache));

		// remove events
		eventManagementBean.removeEventsForPlatform(ids.get(0));

		// cache should be empty
		cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(0, cache.size());
	}

	@Test
	public void testAddEvents_With_Login_And_Check_Discarded()
			throws Exception {

		// get the ids from the database
		List<Long> ids = getPlatformIdsFromDB();
		assertEquals(2, ids.size());

		managementBean_addPrices();

		eventManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1);

		List<String> settings = new ArrayList<String>();
		settings.add("setting1");
		settings.add("setting2");
		eventManagementBean.addEvent(ids.get(0), 2, game1, settings);

		List<String> settings2 = new ArrayList<String>();
		settings2.add("setting3");
		eventManagementBean.addEvent(ids.get(1), 6, game2, settings2);

		// check cache
		List<AssignmentDTO> cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(0), 2, game1,
						settings, new ArrayList<Long>()), cache));
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(1), 6, game2,
						settings2, new ArrayList<Long>()), cache));

		eventManagementBean.submitAssignments();

		// there have to be 2 events in total in the db
		List<IEvent> events = daoFactory.getEventDAO().findAll();

		assertNotNull(events);
		assertEquals(2, events.size());

		assertTrue(checkEventInList(game1, TestData.N_ENT8_1, settings,
				events));
		assertTrue(checkEventInList(game2, TestData.N_ENT8_1, settings2,
				events));

		// check if the bean was discarded after submitAssignments() was
		// called successfully!
		try {
			eventManagementBean.getCache();
			fail(NoSuchEJBException.class.getName() + " expected!");
		} catch (NoSuchEJBException e) {
			// Expected
		}
	}

	@Test
	public void testAddEvent_With_Login_AnotherEventMaster() throws Exception {

		// get the ids from the database
		List<Long> ids = getPlatformIdsFromDB();
		assertEquals(2, ids.size());

		managementBean_addPrices();

		eventManagementBean.login(TestData.N_ENT8_2, TestData.PW_ENT8_2);

		List<String> settings1 = new ArrayList<String>();
		settings1.add("s1");
		settings1.add("s2");
		settings1.add("s3");
		settings1.add("s4");

		final String game3 = "game3";

		eventManagementBean.addEvent(ids.get(0), 2, game3, settings1);

		// check cache
		List<AssignmentDTO> cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(1, cache.size());
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(0), 2, game3,
						settings1, new ArrayList<Long>()), cache));

		eventManagementBean.submitAssignments();

		// there has to be 1 event in total in the db
		List<IEvent> events = daoFactory.getEventDAO().findAll();

		assertNotNull(events);
		assertEquals(1, events.size());

		assertTrue(checkEventInList(game3, TestData.N_ENT8_2, settings1,
				events));
	}

	@Test
	public void testAddEvent_Without_Login() {
		try {

			// get the ids from the database
			List<Long> ids = getPlatformIdsFromDB();
			assertEquals(2, ids.size());

			managementBean.addPrice(0, new BigDecimal(50));

			List<String> settings1 = new ArrayList<String>();
			settings1.add("s1");
			settings1.add("s2");
			eventManagementBean.addEvent(ids.get(0), 2, game1, settings1);

			// try to submit assignments => without log in
			eventManagementBean.submitAssignments();

			fail("Assignments got submitted without login. "
					+ "AssignmentException expected!");
		} catch (Exception e) {
			// Expected Exception
		}

		// there shouldn't be any events in the db
		assertEquals(0, daoFactory.getEventDAO().findAll().size());
	}

	@Test
	public void testAddEvent_Without_Login2() throws Exception {

		// get the ids from the database
		List<Long> ids = getPlatformIdsFromDB();
		assertEquals(2, ids.size());

		managementBean_addPrices();

		List<String> settings1 = new ArrayList<String>();
		settings1.add("s1");
		settings1.add("s2");
		eventManagementBean.addEvent(ids.get(0), 2, game1, settings1);

		List<String> settings2 = new ArrayList<String>();
		settings2.add("s1");
		eventManagementBean.addEvent(ids.get(1), 6, game2, settings2);

		// check cache
		List<AssignmentDTO> cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(0), 2, game1,
						settings1, new ArrayList<Long>()), cache));
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(1), 6, game2,
						settings2, new ArrayList<Long>()), cache));

		// try to submit assignments => without log in
		try {
			eventManagementBean.submitAssignments();
			fail("Assignments got submitted without login. AssignmentException expected!");
		} catch (AssignmentException e) {
			// Expected Exception
		}

		// check cache => should not be cleared
		cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(0), 2, game1,
						settings1, new ArrayList<Long>()), cache));
		assertTrue(isEventInCache(
				new AssignmentDTO(ids.get(1), 6, game2,
						settings2, new ArrayList<Long>()), cache));

		// there shouldn't be any events in the db
		assertEquals(0, daoFactory.getEventDAO().findAll().size());

		// login
		eventManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1);
		// successfully submit Assignments
		eventManagementBean.submitAssignments();

		// check the database here

		// there have to be 2 events in total in the db
		List<IEvent> events = daoFactory.getEventDAO().findAll();
		System.err.println(events.size());
		assertNotNull(events);
		assertEquals(2, events.size());

		assertTrue(checkEventInList(game1, TestData.N_ENT8_1, settings1,
				events));
		assertTrue(checkEventInList(game2, TestData.N_ENT8_1, settings2,
				events));
	}

	@Test
	public void testAddEvent_With_WrongId() throws Exception {

		long nonExisting = Long.MAX_VALUE;

		// try to add a event for non-existing id.
		try {
			eventManagementBean.addEvent(nonExisting, 3, "game5",
					new ArrayList<String>());
			fail("ID is not available in DB. AssignmentException expected!");
		} catch (AssignmentException e) {
			// expected Exception
		}

		// cache should be empty
		List<AssignmentDTO> cache = eventManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(0, cache.size());

		// also db should be empty
		assertEquals(0, daoFactory.getEventDAO().findAll().size());
	}

	private boolean checkEventInList(String game, String eventMasterName,
			List<String> settings, List<IEvent> events) {

		for (IEvent event : events) {
			assertNotNull(event.getMetadata());
			String eventGame = event.getMetadata().getGame();
			assertNotNull(eventGame);
			assertNotNull(event.getEventMaster());
			String eventEventMasterName = event.getEventMaster()
					.getEventMasterName();
			assertNotNull(eventEventMasterName);
			List<String> eventSettings = event.getMetadata().getSettings();
			assertNotNull(eventSettings);
			assertNotNull(event.getEventStreaming());
			assertNotNull(event.getEventStreaming().getStart());

			if (eventGame.equals(game)
					&& eventEventMasterName.equals(eventMasterName)
					&& eventSettings.size() == settings.size()
					&& eventSettings.containsAll(settings))
				return true;
		}

		return false;
	}

	private boolean isEventInCache(AssignmentDTO assignmentDTO,
			List<AssignmentDTO> cache) {
		for (AssignmentDTO cached : cache) {
			Long id1 = cached.getPlatformId();
			Integer num1 = cached.getNumViewers();
			List<String> list1 = cached.getSettings();
			String string1 = cached.getGame();
			List<Long> list2 = cached.getUplinkIds();

			assertNotNull(id1);
			assertNotNull(num1);
			assertNotNull(list1);
			assertNotNull(string1);
			assertNotNull(list2);
			assertTrue(list2.size() > 0);

			if (id1.equals(assignmentDTO.getPlatformId())
					&& num1.equals(assignmentDTO.getNumViewers())
					&& list1.equals(assignmentDTO.getSettings())
					&& string1.equals(assignmentDTO.getGame())) {
				return true;
			}

		}
		return false;
	}
}
