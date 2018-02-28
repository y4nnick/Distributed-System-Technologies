package dst.ass1.jpa;

import static org.junit.Assert.assertTrue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_1a06 extends AbstractTest {

	@Test
	public void testEntity6OrderPreserved() throws NoSuchAlgorithmException {
		final int settings_count = 1000;
		IMetadata ent6_1 = modelFactory.createMetadata();

		ArrayList<String> settings = new ArrayList<String>();
		MessageDigest md = MessageDigest.getInstance("MD5");
		for (int i = 0; i < settings_count; i++) {
			settings.add(new String(md.digest(String.valueOf(Math.random())
					.getBytes())));
		}

		ent6_1.setSettings(settings);

		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(ent6_1);
		tx.commit();

		ent6_1 = daoFactory.getMetadataDAO().findById(ent6_1.getId());
		assertTrue(settings.equals(ent6_1.getSettings()));
	}

	@Test
	public void testEntity6OrderPreservedJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isColumnInTable(Constants.J_METADATA_SETTINGS,
				Constants.M_SETTINGS_ORDER, em));
	}
}
