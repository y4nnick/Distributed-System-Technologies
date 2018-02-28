package dst.ass1.jpa.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * ##########################
 * 
 * DO NOT CHANGE THIS CLASS!
 * 
 * ##########################
 * 
 * Contains various convenience methods for database access.
 * <p/>
 * <b>Note that the caller is responsible for dealing with possible exceptions
 * as well as doing the connection handling.</b><br/>
 * In other words, a connection will not be closed even if a fatal error occurs.
 * However, other SQL resources i.e., {@link Statement Statements} and
 * {@link ResultSet ResultSets} created within the methods, which are not
 * returned to the caller, are closed before the method returns.
 */
public final class DatabaseHelper {
	private DatabaseHelper() {
	}

	/**
	 * Checks if the named table can be accessed via the given EntityManager.
	 * 
	 * @param tableName
	 *            the name of the table to find
	 * @param em
	 *            the EntityManager to use
	 * @return {@code true} if the database schema contains a table with the
	 *         given name, {@code false} otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isTable(final String tableName, EntityManager em)
			throws SQLException {
		final String sql = "show tables";
		final MutableBoolean ret = new MutableBoolean(false);
		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				try {
					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						String tbl = rs.getString(1);
						if (tbl.equalsIgnoreCase(tableName)) {
							ret.setValue(true);
							break;
						}
					}
				} finally {
					stmt.close();
				}
			}

		});
		return ret.getValue();
	}

	/**
	 * Detects the type of table inheritance.
	 * 
	 * @param em
	 *            the EntityManager to use
	 * @param tableName
	 *            the name of the table
	 * @return {@code 0} if the given table exists, {@code 1} otherwise.
	 * @throws SQLException
	 */
	public static int getInheritanceType(EntityManager em, String tableName)
			throws SQLException {
		return isTable(tableName, em) ? 0 : 1;
	}

	/**
	 * Checks whether a certain database table contains a column with the given
	 * name.
	 * 
	 * @param tableName
	 *            the name of the table to check
	 * @param column
	 *            the name of the column to find
	 * @param em
	 *            the EntityManager to use
	 * @return {@code true} if the table contains the column, {@code false}
	 *         otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isColumnInTable(String tableName, String column,
			EntityManager em) throws SQLException {
		final String sql = String
				.format("SELECT * FROM information_schema.columns WHERE table_name='%s' and column_name='%s'",
						tableName.toUpperCase(), column.toUpperCase());

		final MutableBoolean ret = new MutableBoolean(false);
		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				try {
					ret.setValue(stmt.executeQuery(sql).next());
				} finally {
					stmt.close();
				}
			}

		});
		return ret.getValue();

	}

	public static boolean isColumnInTableWithType(final String tableName,
			final String column, final String type, final String length,
			EntityManager em) throws SQLException {

		final MutableBoolean ret = new MutableBoolean(false);
		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				String sql = String
						.format("SELECT * FROM information_schema.columns "
								+ "WHERE table_name='%s' and column_name='%s' and "
								+ "type_name='%s' and character_maximum_length='%s'",
								tableName.toUpperCase(), column.toUpperCase(),
								type.toUpperCase(), length);
				Statement stmt = connection.createStatement();
				try {
					ret.setValue(stmt.executeQuery(sql).next());
				} finally {
					stmt.close();
				}
			}

		});
		return ret.getValue();
	}

	/**
	 * Checks whether a certain table contains an index for the given column
	 * name.
	 * 
	 * @param tableName
	 *            the name of the table to check
	 * @param indexName
	 *            the name of the column the index is created for
	 * @param nonUnique
	 *            {@code true} if the index is non unique, {@code false}
	 *            otherwise
	 * @param em
	 *            the EntityManager to use
	 * @return {@code true} if the index exists, {@code false} otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isIndex(final String tableName,
			final String indexName, final boolean nonUnique, EntityManager em)
			throws SQLException {
		final String sql = String
				.format("SELECT * FROM information_schema.indexes WHERE table_name='%s' and column_name='%s' and non_unique='%s'",
						tableName.toUpperCase(), indexName.toUpperCase(),
						nonUnique ? "1" : "0");

		final MutableBoolean ret = new MutableBoolean(false);
		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				try {
					ret.setValue(stmt.executeQuery(sql).next());
				} finally {
					stmt.close();
				}
			}

		});
		return ret.getValue();
	}

	public static boolean isComposedIndex(String tableName, String columnName1,
			String columnName2, EntityManager em) throws SQLException {

		String index_name1 = getIndexName(tableName, columnName1, em);

		String index_name2 = getIndexName(tableName, columnName2, em);

		if (index_name1 == null || index_name2 == null)
			return false;

		if (index_name1.equals(index_name2))
			return true;

		return false;
	}

	private static String getIndexName(final String tableName,
			final String columnName, EntityManager em) throws SQLException {
		final String sql_index = String
				.format("SELECT index_name FROM information_schema.indexes WHERE table_name='%s' and column_name='%s'",
						tableName.toUpperCase(), columnName.toUpperCase());

		final StringBuilder ret = new StringBuilder();
		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				String index_name = null;
				try {
					ResultSet rs = stmt.executeQuery(sql_index);
					if (rs.next()) {
						index_name = rs.getString(1);
					}
				} finally {
					stmt.close();
				}
				ret.append(index_name);
			}

		});
		return ret.toString();
	}

	/**
	 * Checks whether the given column of a certain table can contain
	 * {@code NULL} values.
	 * 
	 * @param tableName
	 *            the name of the table to check
	 * @param columnName
	 *            the name of the column to check
	 * @param em
	 *            the EntityManager to use
	 * @return {@code true} if the column is nullable, {@code false} otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isNullable(final String tableName,
			final String columnName, EntityManager em) throws SQLException {

		final String sql = String
				.format("SELECT * FROM information_schema.columns WHERE table_name='%s' and column_name='%s' and IS_NULLABLE=true",
						tableName.toUpperCase(), columnName.toUpperCase());

		final MutableBoolean ret = new MutableBoolean(false);
		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				try {
					ret.setValue(stmt.executeQuery(sql).next());
				} finally {
					stmt.close();
				}
			}

		});
		return ret.getValue();
	}

	/**
	 * Deletes all data from all tables that can be accessed via the given
	 * EntityManager.
	 * 
	 * @param em
	 *            the EntityManager to use
	 * @throws Exception
	 *             if a database access error occurs
	 */
	public static void cleanTables(EntityManager em) throws Exception {
		final List<String> tables = getTables(em);

		Session session = getSession(em);		
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				stmt.addBatch("SET FOREIGN_KEY_CHECKS=0");
				for (String table : tables) {
					if (table.toLowerCase().startsWith("hibernate"))
						continue;
					stmt.addBatch("truncate table " + table);
				}
				stmt.addBatch("SET FOREIGN_KEY_CHECKS=1");
				stmt.executeBatch();

				stmt.close();
			}

		});
	}

	/**
	 * Returns a list of all table-names for the given database/connection
	 * 
	 * @param em
	 *            the EntityManager to use
	 * @return List of table names
	 * @throws Exception
	 *             if a database access error occurs
	 */
	public static List<String> getTables(EntityManager em) throws Exception {
		final ArrayList<String> tables = new ArrayList<String>();
		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				String sql = "show tables";
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					tables.add(rs.getString(1));
				}
				rs.close();
			}
		});
		return tables;
	}

	/**
	 * Returns the amount of viewer capacity currently available.
	 * 
	 * @param em
	 *            the EntityManager to use
	 * @param id
	 *            the identifier of the requested MOSPlatform
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static int getAvailableViewerCapacity(EntityManager em, final String id)
			throws SQLException {
		final String sql = "SELECT SUM(" + Constants.M_VIEWERCAPACITY + ") "
				+ "FROM " + Constants.T_UPLINK + " " + "WHERE "
				+ Constants.I_STREAMINGSERVER + "=(" + "SELECT id FROM "
				+ Constants.T_STREAMINGSERVER + " " + "WHERE "
				+ Constants.I_MOSPLATFORM + "=" + id + " LIMIT 0,1)";

		final MutableInt ret = new MutableInt(0);
		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				try {
					ResultSet rs = stmt.executeQuery(sql);
					if (rs.next()) {
						ret.setValue(rs.getInt(1));
					}
				} finally {
					stmt.close();
				}
			}

		});
		return ret.getValue();

	}

	/**
	 * Returns the identifiers of existing MOSPlatforms.
	 * 
	 * @param em
	 *            the EntityManager to use
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static List<Long> getMOSPlatformIds(EntityManager em)
			throws SQLException {
		final List<Long> ids = new ArrayList<Long>();
		final String sql = "SELECT id FROM " + Constants.T_MOSPLATFORM;

		getSession(em).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stmt = connection.createStatement();
				try {
					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						ids.add(rs.getLong(1));
					}
				} finally {
					stmt.close();
				}
			}

		});
		return ids;
	}

	public static Session getSession(EntityManager em) {
		return em.unwrap(Session.class);
	}
}
