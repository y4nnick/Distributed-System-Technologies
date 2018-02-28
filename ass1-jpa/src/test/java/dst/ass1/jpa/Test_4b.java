package dst.ass1.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_4b extends AbstractTest {

	@Test
	public void testEntityListener() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {

			IUplink ent3_1 = daoFactory.getUplinkDAO().findById(
					testData.entity3_1Id);
			assertNotNull(ent3_1);

			ent3_1.setRegion("AUT-VIE@1160");
			Date lastUpdate = ent3_1.getLastUpdate();
			try {
				// for Testing
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			em.flush();

			assertTrue(ent3_1.getLastUpdate().after(lastUpdate));

			tx.rollback();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
