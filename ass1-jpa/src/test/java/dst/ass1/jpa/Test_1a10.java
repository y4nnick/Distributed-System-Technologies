package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IStreamingServer;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;

public class Test_1a10 extends AbstractTest {

	@Test
	public void testEntity2Association() {
		IStreamingServer ent2_1 = daoFactory.getStreamingServerDAO().findById(
				testData.entity2_1Id);
		assertNotNull(ent2_1.getComposedOf());
		assertEquals(1, ent2_1.getComposedOf().size());
		assertEquals(testData.entity2_2Id, ent2_1.getComposedOf().get(0)
				.getId());
	}

	@Test
	public void testEntity2ComposedJdbc() throws ClassNotFoundException,
			SQLException {
		final String sql = "SELECT " + Constants.M_PARTOF + "_id, "
				+ Constants.M_COMPOSEDOF + "_id from "
				+ Constants.J_STREAMINGSERVER_COMPOSEDOF;
		em.unwrap(Session.class).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				assertTrue(rs.next());

				assertEquals((long) testData.entity2_1Id,
						rs.getLong(Constants.M_PARTOF + "_id"));
				assertEquals((long) testData.entity2_2Id,
						rs.getLong(Constants.M_COMPOSEDOF + "_id"));

				assertFalse(rs.next());

				rs.close();
			}
		});
	}

	protected void setUpDatabase() {
		try {
			testData.insertTestData();
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
