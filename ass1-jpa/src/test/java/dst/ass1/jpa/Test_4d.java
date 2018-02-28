package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;

import javax.persistence.Query;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.interceptor.SQLInterceptor;
import dst.ass1.jpa.util.Constants;

public class Test_4d extends AbstractTest {
	
	@Test
	public void testInterceptor() {
		SQLInterceptor.resetCounter();
		
		Query c = em.createQuery("select c from " + Constants.T_MODERATOR + " c");
		c.getResultList();
		
		assertEquals(1, SQLInterceptor.getSelectCount());
		
		c = em.createQuery("select distinct c from " + Constants.T_MODERATOR + " c");
		c.getResultList();
		
		assertEquals(2, SQLInterceptor.getSelectCount());
		
		c = em.createQuery("select e from " + Constants.T_STREAMINGSERVER + " e");
		c.getResultList();
		assertEquals(3, SQLInterceptor.getSelectCount());
	}
	
}
