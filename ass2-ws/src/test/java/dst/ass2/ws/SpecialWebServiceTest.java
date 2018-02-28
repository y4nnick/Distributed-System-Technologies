package dst.ass2.ws;

import static dst.ass2.ws.util.MiscUtils.filterStreamingDtos;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.dao.IEventDAO;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.util.test.TestData;
import dst.ass2.WSBaseTest;
import dst.ass2.ws.session.exception.WebServiceException;
import dst.ass2.ws.session.interfaces.IEventStatisticsBean;
import dst.ass2.ws.util.WebServiceUtils;

/**
 * Test scenario for the service implementation.
 */
public class SpecialWebServiceTest extends WSBaseTest {
	long currTime = System.currentTimeMillis();
	long startTime = currTime - 10000;

	TestData testData;
	DAOFactory daoFactory;

	IEventStatisticsBean service;
	WSRequestFactory factory;

	@Before
	public void setUp() throws Exception {
		this.testData = new TestData(em);
		daoFactory = new DAOFactory(em);

		userTransaction.begin();
		testData.insertTestData_withoutTransaction();
		userTransaction.commit();

		service = WebServiceUtils.getServiceProxy(IEventStatisticsBean.class,
				Constants.NAMESPACE, Constants.SERVICE_NAME,
				Constants.SERVICE_WSDL_URL);
		factory = new WSRequestFactory();

	}

	@Test
	public void testScenario1() throws Exception {
		IGetStatsRequest request1 = factory.createGetStatsRequest();
		request1.setMaxStreamings(10);
		IGetStatsResponse stats = service.getStatisticsForPlatform(request1,
				TestData.N_ENT1_1);
		assertEquals("The name must be '" + TestData.N_ENT1_1 + "'",
				TestData.N_ENT1_1, stats.getStatistics().getName());
		assertEquals(
				"There must be 0 streamings",
				0,
				filterStreamingDtos(stats.getStatistics().getStreamings(),
						startTime).size());
	}

	@Test
	public void testScenario2() throws Exception {

		// prepare db and finish streamings
		userTransaction.begin();

		IEventDAO eventDAO = daoFactory.getEventDAO();

		IEvent event = eventDAO.findById(testData.entity5_1Id);
		IEventStreaming streaming = event.getEventStreaming();
		streaming.setStart(new Date(startTime));
		streaming.setEnd(new Date(currTime));
		streaming.setStatus(EventStatus.FINISHED);

		em.persist(streaming);

		event = eventDAO.findById(testData.entity5_2Id);
		streaming = event.getEventStreaming();
		streaming.setStart(new Date(startTime));
		streaming.setEnd(new Date(currTime));
		streaming.setStatus(EventStatus.FINISHED);

		em.persist(streaming);

		userTransaction.commit();

		IGetStatsRequest request1 = factory.createGetStatsRequest();
		request1.setMaxStreamings(10);
		IGetStatsResponse stats = service.getStatisticsForPlatform(request1,
				TestData.N_ENT1_1);
		assertEquals("The name must be '" + TestData.N_ENT1_1 + "'",
				TestData.N_ENT1_1, stats.getStatistics().getName());
		assertEquals(
				"There must be 2 streamings",
				2,
				filterStreamingDtos(stats.getStatistics().getStreamings(),
						startTime).size());
	}

	@Test
	public void testScenario3() throws Exception {
		IGetStatsRequest request2 = factory.createGetStatsRequest();
		request2.setMaxStreamings(10);
		IGetStatsResponse stats = service.getStatisticsForPlatform(request2,
				TestData.N_ENT1_2);
		assertEquals("The name of the statistics must be '" + TestData.N_ENT1_2
				+ "'", TestData.N_ENT1_2, stats.getStatistics().getName());
		assertEquals(
				"There must be 0 streamings",
				0,
				filterStreamingDtos(stats.getStatistics().getStreamings(),
						startTime).size());
	}

	@Test(expected = WebServiceException.class)
	public void testScenario4() throws Exception {
		IGetStatsRequest request3 = factory.createGetStatsRequest();
		request3.setMaxStreamings(10);
		service.getStatisticsForPlatform(request3, "invalid");
		fail("Exception expected when requesting statistics for unknown.");
	}
}
