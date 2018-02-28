package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.DatabaseHelper;

public class Test_1a07 extends AbstractTest {

	@Test
	public void testAssociation() {
		IEvent ent5_1 = daoFactory.getEventDAO().findById(
				testData.entity5_1Id);
		assertNotNull(ent5_1);
		assertEquals(testData.entity6_1Id, ent5_1.getMetadata().getId());
	}

	@Test
	public void testAssociationJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_EVENT,
				Constants.I_METADATA, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_METADATA,
				Constants.I_EVENT, em));
		assertTrue(DatabaseHelper.isIndex(Constants.T_EVENT,
				Constants.I_METADATA, false, em));
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}

}
