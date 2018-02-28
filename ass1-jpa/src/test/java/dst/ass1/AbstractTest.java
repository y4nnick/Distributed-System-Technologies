package dst.ass1;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass1.jpa.util.test.TestData;

public abstract class AbstractTest {

	protected EntityManagerFactory emf;
	protected EntityManager em;
	protected ModelFactory modelFactory;
	protected DAOFactory daoFactory;

	protected TestData testData;

	@Before
	public void init() throws Exception {
		emf = AbstractTestSuite.getEmf();
		em = emf.createEntityManager();
		modelFactory = new ModelFactory();
		testData = new TestData(em);
		daoFactory = new DAOFactory(em);
		DatabaseHelper.cleanTables(em);

		setUpDatabase();
	}

	@After
	public void clean() throws Exception {
		DatabaseHelper.cleanTables(em);

		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		em.close();
	}

	protected EntityManager getFreshEntityManager() {
		return emf.createEntityManager();
	}

	protected void setUpDatabase() throws Exception {
	}

	protected List<Long> getEventIds(List<IEvent> events) {
		List<Long> ids = new ArrayList<Long>();

		for (IEvent event : events)
			ids.add(event.getId());

		return ids;
	}

	protected List<Long> getEventMasterIds(List<IEventMaster> eventMasters) {
		List<Long> ids = new ArrayList<Long>();

		for (IEventMaster eventMaster : eventMasters)
			ids.add(eventMaster.getId());

		return ids;
	}

	protected List<Long> getUplinkIds(List<IUplink> list) {
		List<Long> ids = new ArrayList<Long>();

		for (IUplink o : list)
			ids.add(o.getId());

		return ids;
	}

	protected boolean checkMembership(Long eventMasterId, Long platformId,
			Double discount, List<IMembership> memberships) {

		for (IMembership membership : memberships) {
			IMembershipKey memId = membership.getId();
			assertNotNull(memId);

			IMOSPlatform platform = memId.getMOSPlatform();
			assertNotNull(platform);
			Long currPlatformId = platform.getId();
			assertNotNull(currPlatformId);

			IEventMaster memEventMaster = memId.getEventMaster();
			assertNotNull(memEventMaster);
			Long memEventMasterId = memEventMaster.getId();
			assertNotNull(memEventMasterId);

			Double memDiscount = membership.getDiscount();
			assertNotNull(memDiscount);

			assertNotNull(membership.getRegistration());

			if (currPlatformId.equals(platformId) && memEventMasterId.equals(eventMasterId)
					&& memDiscount.equals(discount))
				return true;
		}

		return false;
	}

}
