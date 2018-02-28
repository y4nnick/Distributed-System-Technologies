package dst.ass3;

import org.junit.After;
import org.junit.Before;

import com.espertech.esper.client.EventBean;

import dst.ass3.event.EventingFactory;
import dst.ass3.event.IEventProcessing;
import dst.ass3.model.EventType;
import dst.ass3.model.LifecycleState;

public abstract class AbstractEventTest {

	protected final String CLASSIFIEDBY = "ss1";
	protected final int allowedInaccuracy = 500;

	protected IEventProcessing test;

	@Before
	public void setup() {
		System.out.println("******************************"
				+ this.getClass().getCanonicalName()
				+ "******************************");

		test = EventingFactory.getInstance();
	}

	@After
	public void shutdown() {
		test.close();
	}

	protected EventType getEventType(EventBean e) {
		try {
			return (EventType) e.get("type");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	protected LifecycleState getEventStatus(EventBean e) {
		try {
			return (LifecycleState) e.get("state");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
