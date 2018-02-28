package dst.ass1.jpa.interceptor;

import org.hibernate.EmptyInterceptor;

public class SQLInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 3894614218727237142L;


	private static int count;
	private static boolean verbose;

    //thread lock object
	private static Object syn = new Object();


	public String onPrepareStatement(String sql) {
		synchronized(syn) {
			if (verbose) System.out.println(sql);

			String upperSql = sql.toUpperCase();
			while (true) {
				if (upperSql.contains("SELECT")) {
                    // found a SELECT in sql
					count++;
                    // take rest of string after that select
					upperSql = upperSql.substring(upperSql.indexOf("SELECT")+"SELECT".length());
				} else {
                    // no select anymore -> exit
					break;
				}
			}
			return sql;
		}
	}

	public static void resetCounter() {
		synchronized(syn) {
			count=0;
		}
	}

	public static int getSelectCount() {
		synchronized(syn) {
			return count;
		}
	}

	/**
	 * If set to verbose the interceptor prints the intercepted SQL
	 * statements to System.out.
	 * 
	 * @param verbose
	 */

	public static void setVerbose(boolean verbose) {
		synchronized (syn) {
			SQLInterceptor.verbose = verbose;
		}
	}
}
