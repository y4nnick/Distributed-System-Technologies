package dst.ass2.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass2.EJBBaseTest;
import dst.ass2.ejb.model.IPrice;

public class Test_GeneralManagementBean extends EJBBaseTest {

	private DAOFactory daoFactory;

	@Before
	public void setUp() {
		testingBean.insertTestData();
		managementBean.clearPriceCache();
		daoFactory = new DAOFactory(em);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testAddPrice() {
		BigDecimal cost1 = new BigDecimal(50);
		BigDecimal cost2 = new BigDecimal(45);

		managementBean.addPrice(0, cost1);
		managementBean.addPrice(1, cost2);

		List<IPrice> prices = daoFactory.getPriceDAO().findAll();

		assertEquals(2, prices.size());

		for (IPrice price : prices) {
			if (cost1.equals(price.getPrice()))
				assertEquals(0, price.getNrOfHistoricalEvents().intValue());

			if (cost2.equals(price.getPrice()))
				assertEquals(1, price.getNrOfHistoricalEvents().intValue());
		}
	}

	@Test
	public void testAddPrice_Cache() {
		try {
			managementBean.addPrice(0, new BigDecimal(50));

			List<IPrice> prices = daoFactory.getPriceDAO().findAll();

			assertEquals(1, prices.size());

			userTransaction.begin();
			for (IPrice price : prices) {
				em.remove(em.merge(price));
			}
			userTransaction.commit();

			// add same price a second time => should be cached and therefore
			// not stored in the db
			managementBean.addPrice(0, new BigDecimal(50));

			prices = daoFactory.getPriceDAO().findAll();

			assertEquals(0, prices.size());

			// clear cache
			managementBean.clearPriceCache();

			// add same price to cleared cache => should be stored in the db
			managementBean.addPrice(0, new BigDecimal(50));
			prices = daoFactory.getPriceDAO().findAll();

			assertEquals(1, prices.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}
	}
}
