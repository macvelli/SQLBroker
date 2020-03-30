/*
* Open source license text goes here.
 */

package org.sqlbroker;

import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import javax.sql.DataSource;

import org.sqlbroker.Parameters.Value;
import org.sqlbroker.helper.Insert;
import org.sqlbroker.helper.SQL;
import org.sqlbroker.helper.Select;
import org.sqlbroker.mapper.Mapper;

import root.data.structure.ListArray;
import root.jdbc.DatabaseException;
import root.jdbc.PooledConnection;
import root.jdbc.PooledDataSource;
import root.lang.StringExtractor;
import root.log.Log;
import root.util.Jdbc;

/**
 * SQLBroker is a sophisticated and easy to use JDBC library based on the
 * {@link SQLExecutor} library written by Jeff Smith. It retains much of the
 * simplicity of its predecessor while providing a number of enhancements
 * including:
 *
 * <ul>
 * <li><b>Streamlined Query Interface: </b>The <code>query(String)</code>
 *     method uses simplified connection handling where the {@link Connection}
 *     is closed after each invocation.
 * <li><b>Native Execute/Insert/Update/Batch: </b>Provides convenience
 *     methods for executing 1-N static SQL statements, updating with 1-N
 *     prepared statements, or inserting with a single prepared statement.
 * <li><b>Utilizes {@link DataSource}: </b>JDBC {@link Connection}s are now
 *     procured from an implementation of the {@link DataSource} interface.
 * </ul>
 *
 * Because all JDBC convenience methods close the {@link Connection} after
 * each call, it is highly desirable to use a JDBC connection-pooling
 * {@link DataSource} with SQLBroker to maximize performance. The SQLBroker
 * package includes {@link JunctionPoolDatabase} as a tightly-integrated solution
 * and it is recommended to use this implementation whenever possible.
 * 
 * TODO Think about caching result sizes based upon SQL statement and parameters, or do I even need that if the ListArray grows faster than ArrayList?
 *
 * @author esmith
 */
public final class SQLBroker {

	// <><><><><><><><><><><><><>< Static Artifacts ><><><><><><><><><><><><><>

	private static final Log log = new Log(SQLBroker.class);

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private final PooledDataSource dataSource;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SQLBroker(final PooledDataSource dataSource) {
		this.dataSource = dataSource;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final PooledDataSource getDataSource() {
		return dataSource;
	}

	public final int execute(final String sql) {
		log.debug("Executing SQL statement [{P}]", sql);

		final PooledConnection con = dataSource.getConnection();
		Statement stmt = null;

		try {
			stmt = con.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			throw new DatabaseException(sql, e);
		} finally {
			Jdbc.close(stmt);
			con.close();
		}
	}

	public final BatchResult batch(final String... stmts) {
		log.debug("Batch executing SQL statements");

		final PooledConnection con = dataSource.getConnection();
		Statement stmt = null;

		try {
			stmt = con.createStatement();
			return new BatchResult(stmt, stmts);
		} catch (BatchUpdateException e) {
			throw new DatabaseException(new BatchResult(e.getUpdateCounts(), stmts).toString(), e);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			Jdbc.close(stmt);
			con.close();
		}
	}

	public final BatchResult batch(final String sql, final BatchParams params) {
		log.debug("Batch executing SQL statement [{P}] with {P} sets of parameters", sql, params.size);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = con.prepareStatement(sql);
			for (int i=0; i < params.size; i++) {
				prepare(stmt, params.values[i]).addBatch();
			}
			return new BatchResult(sql, params, stmt);
		} catch (BatchUpdateException e) {
			throw new DatabaseException(new BatchResult(sql, params, e.getUpdateCounts()).toString(), e);
		} catch (SQLException e) {
			throw new DatabaseException(sql, e);
		} finally {
			Jdbc.close(stmt);
			con.close();
		}
	}

	public final int batch(final String updateSql, final String insertSql, final BatchParams params) {
		final BatchResult updateResult = batch(updateSql, params);
		final BatchParams insertBatch = new BatchParams(params.size);

		for (int i=0; i < updateResult.results.length; i++) {
			if (updateResult.results[i] == 0) {
				insertBatch.add(params.values[i]);
			}
		}

		if (insertBatch.size > 0) {
			batch(insertSql, insertBatch);
		}

		return insertBatch.size;
	}

	public final int count(final String sql, final Parameters params) {
		log.debug("Counting rows with SQL query [{P}] and {P}", sql, params);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = prepare(con.prepareStatement(sql), params);
			rs = stmt.executeQuery();
			rs.next();
			return rs.getInt(0);
		} catch (SQLException e) {
			throw new DatabaseException(getErrorMessage(sql, params), e);
		} finally {
			Jdbc.close(stmt, rs);
			con.close();
		}
	}

	/**
	 * Watch out and don't use a <code>SELECT COUNT()</code> query, use
	 * something like <code>SELECT 1</code> instead.
	 * 
	 * @param stmt
	 * @return
	 */
	public final boolean exists(final Select stmt) {
		return exists(stmt.toString(), stmt.getParams());
	}

	/**
	 * Watch out and don't use a <code>SELECT COUNT()</code> query, use
	 * something like <code>SELECT 1</code> instead.
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public final boolean exists(final String sql, final Parameters params) {
		log.debug("Determining if {P} exists using SQL query [{P}]", params, sql);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = prepare(con.prepareStatement(sql), params);
			rs = stmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new DatabaseException(getErrorMessage(sql, params), e);
		} finally {
			Jdbc.close(stmt, rs);
			con.close();
		}
	}

	public final int insert(final Insert stmt) {
		return insert(stmt.toString(), stmt.getParams());
	}

	public final int insert(final String sql, final Parameters params) {
		log.debug("Executing SQL insert [{P}] with {P}", sql, params);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = prepare(con.prepareStatement(sql), params);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			return (rs.next()) ? rs.getInt(1) : 0;
		} catch (SQLException e) {
			throw new DatabaseException(getErrorMessage(sql, params), e);
		} finally {
			Jdbc.close(stmt, rs);
			con.close();
		}
	}

	public final <T> T load(final String sql, final Parameters params, final Mapper<T> mapper) {
		log.debug("Loading {P} using SQL query [{P}] and {P}", mapper, sql, params);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = prepare(con.prepareStatement(sql), params);
			rs = stmt.executeQuery();
			return (rs.next()) ? mapper.map(rs) : null;
		} catch (SQLException e) {
			throw new DatabaseException(getErrorMessage(sql, params), e);
		} finally {
			Jdbc.close(stmt, rs);
			con.close();
		}
	}

	public final <T> ListArray<T> loadAll(final String sql, final Parameters params, final Mapper<T> mapper) {
		log.debug("Loading {P} using SQL query [{P}] and {P}", mapper, sql, params);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = prepare(con.prepareStatement(sql), params);
			rs = stmt.executeQuery();

			final ListArray<T> list = new ListArray<>();
			while (rs.next()) {
				list.add(mapper.map(rs));
			}
			return list;
		} catch (SQLException e) {
			throw new DatabaseException(getErrorMessage(sql, params), e);
		} finally {
			Jdbc.close(stmt, rs);
			con.close();
		}
	}

	public final QueryResult query(final Select stmt) {
		final Parameters params = stmt.getParams();

		return (params.getSize() == 0) ? query(stmt.toString()) : query(stmt.toString(), params);
	}

	public final QueryResult query(final String sql) {
		log.debug("Executing SQL query [{P}]", sql);

		final PooledConnection con = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			return new QueryResult(con, stmt, rs);
		} catch (SQLException e) {
			Jdbc.close(stmt, rs);
			con.close();
			throw new DatabaseException(sql, e);
		}
	}

	public final QueryResult query(final String sql, final Parameters params) {
		log.debug("Executing SQL query [{P}] with {P}", sql, params);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = prepare(con.prepareStatement(sql), params);
			rs = stmt.executeQuery();
			return new QueryResult(con, stmt, rs);
		} catch (SQLException e) {
			Jdbc.close(stmt, rs);
			con.close();
			throw new DatabaseException(getErrorMessage(sql, params), e);
		}
	}

	public final QueryResult query(final Select stmt, final int maxRows, final int seconds) {
		return query(stmt.toString(), stmt.getParams(), maxRows, seconds);
	}

	public final QueryResult query(final String sql, final Parameters params, final int maxRows, final int seconds) {
		log.debug("Executing SQL query [{P}] with {P}, maxRows={P}, seconds={P}", sql, params, maxRows, seconds);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = prepare(con.prepareStatement(sql), params);
			if (maxRows > 0) stmt.setMaxRows(maxRows);
			if (seconds > 0) stmt.setQueryTimeout(seconds);
			rs = stmt.executeQuery();
			return new QueryResult(con, stmt, rs);
		} catch (SQLException e) {
			Jdbc.close(stmt, rs);
			con.close();
			throw new DatabaseException(getErrorMessage(sql, params), e);
		}
	}

	public final UpdateResult update(final SQL stmt) {
		return update(stmt.toString(), stmt.getParams());
	}

	public final UpdateResult update(final String sql, final Parameters params) {
		log.debug("Executing SQL update [{P}] with {P}", sql, params);

		final PooledConnection con = dataSource.getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = prepare(con.prepareStatement(sql), params);
			return new UpdateResult(stmt.executeUpdate());
		} catch (SQLException e) {
			throw new DatabaseException(getErrorMessage(sql, params), e);
		} finally {
			Jdbc.close(stmt);
			con.close();
		}
	}

	//  <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private String getErrorMessage(final String sql, final Parameters params) {
		final StringExtractor builder = new StringExtractor(512);

		return builder.append("An exception occurred while executing [").append(sql).append("] with ").append(params).toString();
	}

	private PreparedStatement prepare(final PreparedStatement stmt, final Object[] params) throws SQLException {
		Object o;
		Class<?> clazz;

		for (int i=0; i < params.length;) {
			o = params[i++];

			if (o == null)
				stmt.setNull(i, Types.VARCHAR);
			else {
				clazz = o.getClass();

				if (clazz == String.class)
					stmt.setString(i, (String) o);
				else if (clazz == BigDecimal.class)
					stmt.setBigDecimal(i, (BigDecimal) o);
				else if (clazz == Integer.class)
					stmt.setInt(i, (Integer) o);
				else if (clazz == Timestamp.class)
					stmt.setTimestamp(i, (Timestamp) o);
				else if (clazz == Character.class)
					stmt.setString(i, o.toString());
				else if (clazz == Boolean.class)
					stmt.setBoolean(i, (Boolean) o);
				else if (clazz == Date.class)
					stmt.setDate(i, new java.sql.Date(((Date) o).getTime()));
				else if (clazz == Long.class)
					stmt.setLong(i, (Long) o);
				else if (clazz == Float.class)
					stmt.setFloat(i, (Float) o);
				else if (clazz == Short.class)
					stmt.setShort(i, (Short) o);
				else if (clazz == Double.class)
					stmt.setDouble(i, (Double) o);
				else if (clazz == Time.class)
					stmt.setTime(i, (Time) o);
				else
					stmt.setObject(i, o);
			}
		}

		return stmt;
	}

	private PreparedStatement prepare(final PreparedStatement stmt, final Parameters params) throws SQLException {
		for (Value v : params.getValues()) {
			v.setValue(stmt);
		}

		return stmt;
	}

}
