package org.sqlbroker;

import java.sql.SQLException;
import java.sql.Statement;

public class BatchResult {

	private int	numErrors;
	private int	notExecuted;
	private int	numRowsAffected;

	private String cached;

	private final String		sql;
	private final String[]		stmts;
	private final BatchParams	params;

	final int[] results;

	BatchResult(final Statement stmt, final String[] stmts) throws SQLException {
		this.sql = null;
		this.params = null;
		this.stmts = stmts;
		this.results = stmt.executeBatch();
	}

	public BatchResult(final int[] updateCounts, String[] stmts) {
		this.sql = null;
		this.params = null;
		this.stmts = stmts;
		this.results = updateCounts;
	}

	BatchResult(final String sql, final BatchParams params, final Statement stmt) throws SQLException {
		this.stmts = null;
		this.sql = sql;
		this.params = params;
		this.results = stmt.executeBatch();
		stmt.clearBatch();
	}

	BatchResult(final String sql, final BatchParams params, final int[] updateCounts) {
		this.stmts = null;
		this.sql = sql;
		this.params = params;
		this.results = updateCounts;
	}

	public String toString() {
		if (cached == null) {
			final int batchSize = (params != null) ? params.size() : stmts.length;
			final StringBuilder builder = new StringBuilder(batchSize * 256 + 375);
			int i = 0;
			numErrors = 0;
			notExecuted = 0;
			numRowsAffected = 0;

			builder.append("Batch Results: ");
			if (sql == null) {
				for (String s : stmts) {
					resultRow(builder, i++);
					builder.append(", Sql: [").append(s).append(']');
				}
			} else {
				builder.append("[sql=").append(sql).append(']');
				for (Object[] p : params.values) {
					resultRow(builder, i++);
					builder.append(", Params: ").append(p);
				}
			}

			builder.append("\nStatements run: ").append(batchSize - notExecuted);
			builder.append(", Rows affected: ").append(numRowsAffected);
			builder.append(", Errors: ").append(numErrors);
			builder.append(", Not executed: ").append(notExecuted);
			cached = builder.toString();
		}

		return cached;
	}

//	**************************** Private Classes *****************************

	private void resultRow(final StringBuilder builder, final int i) {
		builder.append("\nRow: ").append(i+1).append(", Status: ");

		if (i >= results.length) {
			notExecuted++;
			builder.append("NOT EXECUTED");
		} else if (results[i] == Statement.EXECUTE_FAILED) {
			numErrors++;
			notExecuted++;
			builder.append("FAILED");
		} else if (results[i] == Statement.SUCCESS_NO_INFO)
			builder.append("OK NO INFO");
		else {
			numRowsAffected += results[i];
			builder.append("OK [updateCount=").append(results[i]).append(']');
		}
	}

}	// End BatchResult
