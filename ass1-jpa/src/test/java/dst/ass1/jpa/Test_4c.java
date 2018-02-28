package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.listener.DefaultListener;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_4c extends AbstractTest {

	@Test
	public void testDefaultListener() {
		try {
			assertEquals(1, DefaultListener.getRemoveOperations());
			assertTrue(DefaultListener.getPersistOperations() > 0);
			assertEquals(
					new Double(DefaultListener.getAverageTimeToPersist()),
					new Double((double) DefaultListener
							.getOverallTimeToPersist()
							/ DefaultListener.getPersistOperations()));

			List<IModerator> ent7_list = daoFactory.getModeratorDAO().findAll();
			assertNotNull(ent7_list);
			assertTrue(ent7_list.size() > 0);

			int loadOperations = DefaultListener.getLoadOperations();
			em.refresh(ent7_list.get(0));
			assertEquals(loadOperations + 1,
					DefaultListener.getLoadOperations());

			EntityTransaction tx = em.getTransaction();
			tx.begin();

			ent7_list = daoFactory.getModeratorDAO().findAll();
			assertNotNull(ent7_list);
			assertTrue(ent7_list.size() > 0);

			ent7_list.get(0).setFirstName("updated");
			em.persist(ent7_list.get(0));

			tx.commit();

			assertEquals(1, DefaultListener.getUpdateOperations());

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

	}

	protected void setUpDatabase() throws NoSuchAlgorithmException,
			ClassNotFoundException, SQLException {

		DefaultListener.clear();

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			IModerator ent7_1 = modelFactory.createModerator();
			ent7_1.setFirstName(TestData.N_ENT7_1);
			em.persist(ent7_1);
			em.flush();
			em.remove(ent7_1);

			ent7_1 = modelFactory.createModerator();
			ent7_1.setFirstName(TestData.N_ENT7_1);
			em.persist(ent7_1);

			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));

		}
	}
}
