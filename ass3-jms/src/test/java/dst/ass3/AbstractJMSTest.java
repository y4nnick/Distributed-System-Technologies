package dst.ass3;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import dst.ass3.jms.JMSFactory;
import dst.ass3.jms.uplink.IUplink;
import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.streamingserver.IStreamingServer;
import dst.ass3.model.EventType;

public abstract class AbstractJMSTest {

	protected static final String UPLINK1_NAME = "u1";
	protected static final String UPLINK2_NAME = "u2";
	protected static final String UPLINK3_NAME = "u3";
	protected static final String UPLINK4_NAME = "u4";

	protected static final String STREAMINGSERVER1_NAME = "ss1";
	protected static final String STREAMINGSERVER2_NAME = "ss2";

	protected IScheduler scheduler;
	protected IStreamingServer ss2;
	protected IStreamingServer ss1;
	protected IUplink u4;
	protected IUplink u3;
	protected IUplink u2;
	protected IUplink u1;

	protected EJBContainer ejbContainer;
	protected Context ctx;

	/**
	 * Time to wait for semaphores to reach required value
	 */
	public int DEFAULT_CHECK_TIMEOUT = 5;

	public void init() {
		this.u1 = JMSFactory.createUplink(UPLINK1_NAME,
				STREAMINGSERVER1_NAME, EventType.INTERACTIVE);
		this.u2 = JMSFactory.createUplink(UPLINK2_NAME,
				STREAMINGSERVER1_NAME, EventType.PRESENTATION);
		this.u3 = JMSFactory.createUplink(UPLINK3_NAME,
				STREAMINGSERVER2_NAME, EventType.INTERACTIVE);
		this.u4 = JMSFactory.createUplink(UPLINK4_NAME,
				STREAMINGSERVER2_NAME, EventType.PRESENTATION);

		this.ss1 = JMSFactory.createStreamingServer(STREAMINGSERVER1_NAME);
		this.ss2 = JMSFactory.createStreamingServer(STREAMINGSERVER2_NAME);

		this.scheduler = JMSFactory.createScheduler();

		System.setProperty("openejb.validation.output.level", "VERBOSE");
		System.setProperty("openejb.embedded.initialcontext.close", "DESTROY");

		try {
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

			ctx.bind("inject", scheduler);

			ctx.bind("inject", ss1);
			ctx.bind("inject", ss2);
                        
			ctx.bind("inject", u1);
			ctx.bind("inject", u2);
			ctx.bind("inject", u3);
			ctx.bind("inject", u4);
		} catch (Exception e) {
			e.printStackTrace();

			throw new RuntimeException(e);
		}

		System.out.println("******************************"
				+ this.getClass().getCanonicalName()
				+ "******************************");
	}

	public void shutdown() {
		if(ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
		
		if (ejbContainer != null)
			ejbContainer.close();
	}

}
