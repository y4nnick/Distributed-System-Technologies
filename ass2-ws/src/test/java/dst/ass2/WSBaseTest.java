package dst.ass2;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.apache.openejb.api.LocalClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import dst.ass1.jpa.util.DatabaseHelper;

@LocalClient
public class WSBaseTest {

	protected static Context ctx;
	protected static EJBContainer ejbContainer;

	@PersistenceContext
	protected EntityManager em;

	@Resource
	protected UserTransaction userTransaction;

	@BeforeClass
	public static void startContainer() throws Exception {
		System.setProperty("openejb.validation.output.level", "VERBOSE");
		System.setProperty("openejb.jpa.auto-scan", "true");
		System.setProperty("openejb.embedded.initialcontext.close", "DESTROY");

		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.LocalInitialContextFactory");

		p.setProperty("openejb.embedded.remotable", "true");

		// define data-source
		p.put("dst_ds", "new://Resource?type=DataSource");
		p.put("dst_ds.JdbcDriver", "org.h2.Driver");
		p.put("dst_ds.JdbcUrl",
				"jdbc:h2:/tmp/database/dst;AUTO_SERVER=TRUE;MVCC=true");
		p.put("dst_ds.UserName", "");
		p.put("dst_ds.Password", "");
		p.put("dst_ds.JtaManaged", "true");

		// alter persistence unit (persistence.xml) ?
		// p.put("dst_pu_impl.hibernate.ejb.interceptor", "");

		ejbContainer = EJBContainer.createEJBContainer(p);
		ctx = ejbContainer.getContext();
	}

	@Before
	public void init() throws Exception {
		ctx.bind("inject", this);

		// clean database
		DatabaseHelper.cleanTables(em);
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
}
