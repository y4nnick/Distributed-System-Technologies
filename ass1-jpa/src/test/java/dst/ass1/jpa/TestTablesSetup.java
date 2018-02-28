package dst.ass1.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.DatabaseHelper;

public class TestTablesSetup extends AbstractTest {

	@Test
	public void testJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isTable(Constants.J_METADATA_SETTINGS, em));
	}

	@Test
	public void testTables1Jdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isTable(Constants.T_MEMBERSHIP, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_MEMBERSHIP,
				Constants.I_MOSPLATFORM, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_MEMBERSHIP,
				Constants.I_EVENTMASTER, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_MOSPLATFORM,
				Constants.I_MEMBERSHIP, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_EVENTMASTER,
				Constants.I_MEMBERSHIP, em));

	}

	@Test
	public void testTables2Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isTable(Constants.J_STREAMING_UPLINK, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.J_STREAMING_UPLINK,
				Constants.I_EVENTSTREAMINGS, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.J_STREAMING_UPLINK,
				Constants.I_UPLINKS, em));
	}

	@Test
	public void testTables3Jdbc() throws ClassNotFoundException,
			SQLException {
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_MOSPLATFORM,
				Constants.I_STREAMINGSERVER, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_STREAMINGSERVER,
				Constants.I_MOSPLATFORM, em));
		assertTrue(DatabaseHelper.isIndex(Constants.T_STREAMINGSERVER,
				Constants.I_MOSPLATFORM, true, em));
	}

	@Test
	public void testTables4Jdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_UPLINK,
				Constants.I_STREAMINGSERVER, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_STREAMINGSERVER,
				Constants.I_UPLINK, em));
		assertTrue(DatabaseHelper.isIndex(Constants.T_UPLINK,
				Constants.I_STREAMINGSERVER, true, em));
	}

	@Test
	public void testTables5Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isIndex(Constants.T_STREAMINGSERVER,
				Constants.I_MODERATOR, true, em));
		assertTrue(DatabaseHelper.isColumnInTable(Constants.T_STREAMINGSERVER,
				Constants.I_MODERATOR, em));
		assertFalse(DatabaseHelper.isColumnInTable(Constants.T_MODERATOR,
				Constants.I_STREAMINGSERVER, em));
	}

	@Test
	public void testTables6Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(DatabaseHelper.isColumnInTableWithType(Constants.T_EVENTMASTER, Constants.M_PASSWORD,
				"VARBINARY", "16", em));
	}

}
