package dst.ass2;

import static dst.ass2.ejb.util.EJBUtils.lookup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import dst.ass2.ejb.session.interfaces.IEventManagementBean;
import org.apache.openejb.api.LocalClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.util.DatabaseHelper;
import dst.ass1.jpa.util.test.TestData;
import dst.ass2.ejb.session.GeneralManagementBean;
import dst.ass2.ejb.session.EventManagementBean;
import dst.ass2.ejb.session.TestingBean;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.IGeneralManagementBean;
import dst.ass2.ejb.session.interfaces.ITestingBean;

@LocalClient
public class EJBBaseTest {

	protected static Context ctx;
	protected static EJBContainer ejbContainer;

	@PersistenceContext
	protected EntityManager em;

	@Resource
	protected UserTransaction userTransaction;

	protected DAOFactory daoFactory;

	protected ITestingBean testingBean;
	protected IGeneralManagementBean managementBean;

	@BeforeClass
	public static void startContainer() throws Exception {

		System.setProperty("openejb.validation.output.level", "VERBOSE");
		System.setProperty("openejb.jpa.auto-scan", "true");
		System.setProperty("openejb.embedded.initialcontext.close", "DESTROY");

		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.LocalInitialContextFactory");

		// define data-source
		p.put("dst_ds", "new://Resource?type=DataSource");
		p.put("dst_ds.JdbcDriver", "org.h2.Driver");
		p.put("dst_ds.JdbcUrl",
				"jdbc:h2:/tmp/database/dst;AUTO_SERVER=TRUE;MVCC=true");
		p.put("dst_ds.UserName", "");
		p.put("dst_ds.Password", "");
		p.put("dst_ds.JtaManaged", "true");

		ejbContainer = EJBContainer.createEJBContainer(p);
		ctx = ejbContainer.getContext();
	}

	@Before
	public void init() throws Exception {
		ctx.bind("inject", this);

		// clean database
		DatabaseHelper.cleanTables(em);

		testingBean = lookup(ctx, TestingBean.class);
		managementBean = lookup(ctx, GeneralManagementBean.class);

		daoFactory = new DAOFactory(em);
	}

	@AfterClass
	public static void closeContainer() {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}

		if (ejbContainer != null) {
			ejbContainer.close();
		}
	}

	protected void managementBean_clearPrices() {
		managementBean.clearPriceCache();
	}

	protected void managementBean_addPrices() {
		managementBean.addPrice(0, new BigDecimal(50));
		managementBean.addPrice(1, new BigDecimal(45));
		managementBean.addPrice(2, new BigDecimal(40));
		managementBean.addPrice(4, new BigDecimal(35));
		managementBean.addPrice(10, new BigDecimal(30));
		managementBean.addPrice(20, new BigDecimal(20));
		managementBean.addPrice(100, new BigDecimal(15));
	}

	protected void removeEventsFromDB(boolean onlyScheduled) throws Exception {
		userTransaction.begin();
		for (IEvent event : daoFactory.getEventDAO().findAll()) {
			if (onlyScheduled) {
				if (event.getEventStreaming().getStatus()
						.equals(EventStatus.SCHEDULED))
					em.remove(event);
			} else
				em.remove(event);
		}
		userTransaction.commit();
	}

	protected List<Long> getPlatformIdsFromDB() {
		List<IMOSPlatform> platforms = daoFactory.getPlatformDAO().findAll();
		List<Long> ids = new ArrayList<Long>();
		for (IMOSPlatform platform : platforms) {
			ids.add(platform.getId());
		}
		return ids;
	}

	protected void addEventsForEventMaster1(List<Long> ids)
			throws NamingException, AssignmentException {
		IEventManagementBean eventManagementBean = lookup(ctx,
				EventManagementBean.class);

		eventManagementBean.login(TestData.N_ENT8_1, TestData.PW_ENT8_1);

		List<String> settings1 = new ArrayList<String>();
		settings1.add("s1");
		settings1.add("s2");
		eventManagementBean.addEvent(ids.get(0), 2, "game1", settings1);

		List<String> settings2 = new ArrayList<String>();
		settings2.add("s1");
		eventManagementBean.addEvent(ids.get(1), 6, "game2", settings2);

		eventManagementBean.submitAssignments();
	}

	protected void addEventsForEventMaster2(List<Long> ids)
			throws NamingException, AssignmentException {
		IEventManagementBean eventManagementBean = lookup(ctx,
				EventManagementBean.class);

		eventManagementBean.login(TestData.N_ENT8_2, TestData.PW_ENT8_2);

		List<String> params3 = new ArrayList<String>();
		params3.add("s1");
		params3.add("s2");
		params3.add("s3");
		params3.add("s4");
		eventManagementBean.addEvent(ids.get(0), 2, "game3",
				params3);

		List<String> params5 = new ArrayList<String>();

		eventManagementBean.addEvent(ids.get(0), 3, "game4",
				params5);

		eventManagementBean.submitAssignments();
	}

}
