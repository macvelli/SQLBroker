package org.sqlbroker;

import root.data.structure.ListArray;
import root.jdbc.PooledDataSource;
import root.thread.Sync;
import root.thread.ThreadPool;
import root.timer.Watch;

/**
 * 
 * @author esmith
 */
public final class ConcurrentSQLBroker {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	/** The SQLBroker to use for executing the queries									*/
	private final SQLBroker						broker;

	private final ThreadPool					pool;

	private final ListArray<ConcurrentQuery>	queries;

	private final Watch							t;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ConcurrentSQLBroker(final PooledDataSource dataSource, final ThreadPool threadPool) {
		t = new Watch();
		broker = new SQLBroker(dataSource);
		queries = new ListArray<>();
		pool = threadPool;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void query(final String sql, final Parameters params) {
		final ConcurrentQuery query = new ConcurrentQuery(sql, params);

		if (!t.isRunning()) {
			queries.clear();
			t.start();
		}

		queries.add(query);
		pool.execute(query);
	}

	public final QueryResult[] getResults() throws InterruptedException {
		final QueryResult[] results = new QueryResult[queries.getSize()];

		int i=0;
		for (ConcurrentQuery q : queries) {
			results[i++] = q.getResults();
		}
		t.stop();

		return results;
	}

	public final long duration() {
		return t.elapsed();
	}

	//  <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	private final class ConcurrentQuery implements Runnable {

		/** The SQL query to execute in its own Thread												*/
		private final String sql;

		/** The Parameters to use for executing the query											*/
		private final Parameters params;

		/** The results of the SQL query															*/
		private QueryResult results;

		private final Sync sync;

		private ConcurrentQuery(final String sql, final Parameters params) {
			this.sql = sql;
			this.params = params;
			sync = new Sync();
		}

		private QueryResult getResults() throws InterruptedException {
			sync.lock();
			try {
				while (results == null) {
					sync.await();
				}
			} finally {
				sync.unlock();
			}

			return results;
		}

		public void run() {
			results = broker.query(sql, params);
			sync.lock();
			try { sync.signal(); } finally { sync.unlock(); }
		}

	}	// End ConcurrentQuery

}
