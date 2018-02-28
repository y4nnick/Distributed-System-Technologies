package dst.ass1.jpa;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.lifecycle.EMLifecycleDemo;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_4a extends AbstractTest {

	private EMLifecycleDemo demo;

	@Before
	public void initDemo() {
		demo = new EMLifecycleDemo(em, modelFactory);
	}

	@Test
	public void testEMLifecycleDemo() {
		try {
			demo.demonstrateEntityMangerLifecycle();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
