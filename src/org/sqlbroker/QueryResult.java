package org.sqlbroker;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import root.adt.SetHashed;
import root.jdbc.DatabaseException;
import root.jdbc.PooledConnection;
import root.lang.Itemizer;
import root.util.Jdbc;

/**
 * - Does not include @Deprecated methods such as <code>BigDecimal getBigDecimal(String columnLabel, int scale)</code>
 *   and <code>InputStream getUnicodeStream(String columnLabel)</code>.
 * - There are convenience methods such as <code>Boolean getBooleanObject(String columnLabel)</code> to get the
 *   object wrappers for primitive values, which will return <code>null</code> if the database value is missing.
 *
 * TODO Figure out how to remove all the throws SQLException (done)
 * TODO getDate() returns a java.sql.Date, make it return a java.util.Date instead
 * TODO Every time the columnLabelSet is checked and a label cannot be found, throw an exception instead of returning null
 * 	- This enforces consistency between column labels used in SQL queries and column labels used in code
 *
 * @author esmith
 */
public final class QueryResult implements Itemizer<QueryResult> {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	/** Keeps track of when <code>close()</code> is called				*/
	private boolean isClosed;

	/** The index of the {@link Itemizer} being processed				*/
	private int index;

	/** The {@link PooledConnection} that executed the SQL statement	*/
	private final PooledConnection	con;

	/** The {@link Statement} that created the {@link ResultSet}		*/
	private final Statement			stmt;

	/** The wrapped {@link ResultSet}									*/
	private final ResultSet			resultSet;

	/** The set of column labels present on the {@link ResultSet}		*/
	private final SetHashed<String> columnLabelSet;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	QueryResult(final PooledConnection con, final Statement stmt, final ResultSet resultSet) throws SQLException {
		this.index = -1;
		this.con = con;
		this.stmt = stmt;
		this.resultSet = resultSet;

		final ResultSetMetaData meta = resultSet.getMetaData();
		final int numCols = meta.getColumnCount();

		columnLabelSet = new SetHashed<>(numCols);
		for (int i=0; i < numCols; ) {
			columnLabelSet.add(meta.getColumnName(++i));
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean hasNext() {
		if (isClosed) {
			return false;
		}

		boolean hasNext;

		try {
			hasNext = resultSet.next();
		} catch (SQLException e) {
			hasNext = false;
		}

		if (!hasNext) {
			close();
		}

		return hasNext;
	}

	@Override
	public final QueryResult next() {
		index++;
		return this;
	}

	@Override
	public final void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public final int getIndex() {
		return index;
	}

	@Override
	public final int getSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Itemizer<QueryResult> iterator() {
		return this;
	}

	@Override
	public final void reset() {
		throw new UnsupportedOperationException();
	}

	public final void close() {
		if (!isClosed) {
			Jdbc.close(stmt, resultSet);
			con.close();
		}

		isClosed = true;
	}

	public final Array getArray(final int columnIndex) {
		try {
			return resultSet.getArray(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Array getArray(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getArray(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final InputStream getAsciiStream(final int columnIndex) {
		try {
			return resultSet.getAsciiStream(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final InputStream getAsciiStream(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getAsciiStream(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final BigDecimal getBigDecimal(final int columnIndex) {
		try {
			return resultSet.getBigDecimal(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final BigDecimal getBigDecimal(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getBigDecimal(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final InputStream getBinaryStream(final int columnIndex) {
		try {
			return resultSet.getBinaryStream(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final InputStream getBinaryStream(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getBinaryStream(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Blob getBlob(final int columnIndex) {
		try {
			return resultSet.getBlob(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Blob getBlob(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getBlob(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final boolean getBoolean(final int columnIndex) {
		try {
			return resultSet.getBoolean(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final boolean getBoolean(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return false;
		}

		try {
			return resultSet.getBoolean(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Boolean getBooleanObject(final int columnIndex) {
		try {
			final boolean b = resultSet.getBoolean(columnIndex);

			return (resultSet.wasNull()) ? null : Boolean.valueOf(b);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Boolean getBooleanObject(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			final boolean b = resultSet.getBoolean(columnLabel);

			return (resultSet.wasNull()) ? null : Boolean.valueOf(b);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final byte getByte(final int columnIndex) {
		try {
			return resultSet.getByte(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final byte getByte(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return 0;
		}

		try {
			return resultSet.getByte(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Byte getByteObject(final int columnIndex) {
		try {
			final byte b = resultSet.getByte(columnIndex);

			return (resultSet.wasNull()) ? null : Byte.valueOf(b);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Byte getByteObject(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			final byte b = resultSet.getByte(columnLabel);

			return (resultSet.wasNull()) ? null : Byte.valueOf(b);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final byte[] getBytes(final int columnIndex) {
		try {
			return resultSet.getBytes(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final byte[] getBytes(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getBytes(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Reader getCharacterStream(final int columnIndex) {
		try {
			return resultSet.getCharacterStream(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Reader getCharacterStream(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getCharacterStream(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Clob getClob(final int columnIndex) {
		try {
			return resultSet.getClob(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Clob getClob(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getClob(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Date getDate(final int columnIndex) {
		try {
			return resultSet.getDate(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Date getDate(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getDate(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Date getDate(final int columnIndex, final Calendar cal) {
		try {
			return resultSet.getDate(columnIndex, cal);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Date getDate(final String columnLabel, final Calendar cal) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getDate(columnLabel, cal);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final double getDouble(final int columnIndex) {
		try {
			return resultSet.getDouble(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final double getDouble(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return 0;
		}

		try {
			return resultSet.getDouble(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Double getDoubleObject(final int columnIndex) {
		try {
			final double d = resultSet.getDouble(columnIndex);

			return (resultSet.wasNull()) ? null : Double.valueOf(d);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Double getDoubleObject(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			final double d = resultSet.getDouble(columnLabel);

			return (resultSet.wasNull()) ? null : Double.valueOf(d);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final float getFloat(final int columnIndex) {
		try {
			return resultSet.getFloat(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final float getFloat(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return 0;
		}

		try {
			return resultSet.getFloat(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Float getFloatObject(final int columnIndex) {
		try {
			final float f = resultSet.getFloat(columnIndex);

			return (resultSet.wasNull()) ? null : Float.valueOf(f);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Float getFloatObject(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			final float f = resultSet.getFloat(columnLabel);

			return (resultSet.wasNull()) ? null : Float.valueOf(f);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final int getInt(final int columnIndex) {
		try {
			return resultSet.getInt(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final int getInt(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return 0;
		}

		try {
			return resultSet.getInt(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	/**
	 * Returns an <code>int[]</code> based on the column labels submitted.
	 *
	 * @param columnLabels The column labels.
	 * @return an <code>int[]</code> based on the column labels submitted.
	 */
	public final int[] getIntArray(final String... columnLabels) {
		final int[] ints = new int[columnLabels.length];

		int i=0;
		for (String s : columnLabels) {
			ints[i++] = getInt(s);
		}

		return ints;
	}

	public final Integer getIntObject(final int columnIndex) {
		try {
			final int i = resultSet.getInt(columnIndex);

			return (resultSet.wasNull()) ? null : Integer.valueOf(i);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Integer getIntObject(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			final int i = resultSet.getInt(columnLabel);

			return (resultSet.wasNull()) ? null : Integer.valueOf(i);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final long getLong(final int columnIndex) {
		try {
			return resultSet.getLong(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final long getLong(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return 0;
		}

		try {
			return resultSet.getLong(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Long getLongObject(final int columnIndex) {
		try {
			final long l = resultSet.getLong(columnIndex);

			return (resultSet.wasNull()) ? null : Long.valueOf(l);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Long getLongObject(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			final long l = resultSet.getLong(columnLabel);

			return (resultSet.wasNull()) ? null : Long.valueOf(l);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Reader getNCharacterStream(final int columnIndex) {
		try {
			return resultSet.getNCharacterStream(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Reader getNCharacterStream(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getNCharacterStream(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final NClob getNClob(final int columnIndex) {
		try {
			return resultSet.getNClob(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final NClob getNClob(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getNClob(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final String getNString(final int columnIndex) {
		try {
			return resultSet.getNString(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final String getNString(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getNString(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Object getObject(final int columnIndex) {
		try {
			return resultSet.getObject(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Object getObject(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getObject(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Object getObject(final int columnIndex, final Map<String, Class<?>> map) {
		try {
			return resultSet.getObject(columnIndex, map);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Object getObject(final String columnLabel, final Map<String, Class<?>> map) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getObject(columnLabel, map);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final <T> T getObject(final int columnIndex, final Class<T> type) {
		try {
			return resultSet.getObject(columnIndex, type);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final <T> T getObject(final String columnLabel, final Class<T> type) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getObject(columnLabel, type);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Ref getRef(final int columnIndex) {
		try {
			return resultSet.getRef(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Ref getRef(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getRef(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final RowId getRowId(final int columnIndex) {
		try {
			return resultSet.getRowId(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final RowId getRowId(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getRowId(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final short getShort(final int columnIndex) {
		try {
			return resultSet.getShort(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final short getShort(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return 0;
		}

		try {
			return resultSet.getShort(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Short getShortObject(final int columnIndex) {
		try {
			final short s = resultSet.getShort(columnIndex);

			return (resultSet.wasNull()) ? null : Short.valueOf(s);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Short getShortObject(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			final short s = resultSet.getShort(columnLabel);

			return (resultSet.wasNull()) ? null : Short.valueOf(s);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final SQLXML getSQLXML(final int columnIndex) {
		try {
			return resultSet.getSQLXML(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final SQLXML getSQLXML(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getSQLXML(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final String getString(final int columnIndex) {
		try {
			return resultSet.getString(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final String getString(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getString(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Time getTime(final int columnIndex) {
		try {
			return resultSet.getTime(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Time getTime(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getTime(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Time getTime(final int columnIndex, final Calendar cal) {
		try {
			return resultSet.getTime(columnIndex, cal);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Time getTime(final String columnLabel, final Calendar cal) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getTime(columnLabel, cal);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Timestamp getTimestamp(final int columnIndex) {
		try {
			return resultSet.getTimestamp(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Timestamp getTimestamp(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getTimestamp(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Timestamp getTimestamp(final int columnIndex, final Calendar cal) {
		try {
			return resultSet.getTimestamp(columnIndex, cal);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final Timestamp getTimestamp(final String columnLabel, final Calendar cal) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getTimestamp(columnLabel, cal);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final URL getURL(final int columnIndex) {
		try {
			return resultSet.getURL(columnIndex);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

	public final URL getURL(final String columnLabel) {
		if (!columnLabelSet.contains(columnLabel)) {
			return null;
		}

		try {
			return resultSet.getURL(columnLabel);
		} catch (SQLException e) {
			close();
			throw new DatabaseException(e);
		}
	}

}
