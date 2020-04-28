package org.sqlbroker;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;

import root.adt.MapHashed;
import root.lang.ParamString;

/**
 * SQLResults stores the results of a sql query in the form of objects stored in an Arraylist
 * container object. It contains handy methods like getLong(), getDate(), getString(), etc.
 * for accessing fields in a SQLResults (result set). <br><br>
 *
 * Here is a complete code example:<br>
 * <pre>
 *   driverName = "oracle.jdbc.driver.OracleDriver";
 *   connURL = "jdbc:oracle:thin:@SNOWMASS:1521:WDEV";
 *   username = "wdevprja";
 *   password = "password";
 *   ConnectionPool conPool = new ConnectionPool(int numPooledCon,
 *                                               String driverName,
 *                                               String conURL,
 *                                               String username,
 *                                               String password);
 *
 *   SQLExecutor sqlExec = new SQLExecutor(conPool);
 *   <b>SQLResults res = sqlExec.runQueryCloseCon("SELECT * FROM INV");
 *   String out = "";
 *   for (int row=0; row < res.getRowCount(); row++)
 *      out += res.getLong(row, "TEST_ID") + " " + res.getString(row, "NOTES") + " " +
 *             res.getDate(row, "TEST_DT") + " " + res.getDouble(row, "AMOUNT") + " " +
 *             res.getString(row, "CODE") + "\n";</b>
 *   System.out.println(out);
 * </pre>
 *
 * There is also a handy toString() method which can be used to generate a text table
 * of the entire contents of the SQLResults result set. Here is a code example:
 * <pre>
 *   System.out.println(res.toString());  //output results as a text table
 * </pre>
 * For a simple employee table, the output of this toString() method will look
 * something like:<br>
 * <pre>
 * EMPID       FNAME       LNAME
 * ----------------------------------
 * 1.0         mark        cook
 * 2.0         carlos      moya
 * 3.0         Jeff        Smith
 * </pre>
 * @author Jeff S Smith
 */
public final class QueryResult_Old implements Iterable<QueryResult_Old.Row> {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private final Row rows;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Creates a <code>QueryResults</code> instance from an open
	 * {@link ResultSet}. Stores all of the row data in an {@link SList} of
	 * {@link Row} objects for offline data retrieval.
	 *
	 * @param rs		The {@link ResultSet} to extract results from.
	 * @throws SQLException
	 */
	QueryResult_Old(final ResultSet rs) throws SQLException {
		rows = new Row(rs);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final QueryResult_Old expect(final int expected) {
		if (expected != rows.size) {
			throw new AssertionError(ParamString.formatMsg("Expected: {P} Actual: {P}", expected, rows.size));
		}

		return this;
	}

	public final Row first() {
		if (rows.size > 0) {
			rows.index++;
			return rows;
		}

		return null;
	}

	public final Iterator<Row> iterator() {
		return rows;
	}

	public final int size() {
		return rows.size;
	}

	//  <><><><><><><><><><><><><><> Inner Classes <><><><><><><><><><><><><><>

	public final class Row implements Iterator<Row> {

		private int size;

		private int index;

		private Object[][] data;

		/** A {@link MapHashed} associating column names with indicies	*/
		private final MapHashed<String, Integer> indexMap;

		private Row(final ResultSet rs) throws SQLException {
			final ResultSetMetaData meta = rs.getMetaData();
			final int numCols = meta.getColumnCount();
			index = -1;

			// 1. Initialize the column name-index map
			indexMap = new MapHashed<>(numCols);
			for (int i=0; i < numCols; i++) {
				indexMap.put(meta.getColumnName(i+1), i);
			}

			data = new Object[64][];

			while (rs.next()) {
				if (size == data.length) {
					final Object[][] o = new Object[size << 1][];
					System.arraycopy(data, 0, o, 0, size);
					data = o;
				}

				data[size] = new Object[numCols];
				for (int i=0; i < numCols; ) {
					data[size][i] = rs.getObject(++i);
				}

				size++;
			}
		}

		/**
		 * Returns the value at the specified column index as a
		 * {@link BigDecimal}.
		 *
		 * @param i	The column index
		 * @return	the {@link BigDecimal} at the specified column index.
		 */
		public BigDecimal getBigDecimal(final int i) {
			final Object o = data[index][i];

			return (o == null)
					? new BigDecimal("0.00")
					: (o instanceof BigDecimal) ? (BigDecimal) o : new BigDecimal(o.toString());
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * {@link BigDecimal}.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return a {@link BigDecimal} mapped to the column name.
		 */
		public BigDecimal getBigDecimal(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null)
					? new BigDecimal("0.00")
					: (o instanceof BigDecimal) ? (BigDecimal) o : new BigDecimal(o.toString());
		}

		/**
		 * Returns the value at the specified column index as a
		 * <code>boolean</code>.
		 *
		 * @param i	The column index
		 * @return	the <code>boolean</code> at the specified column index.
		 */
		public boolean getBoolean(final int i) {
			final Object o = data[index][i];

			return (o != null && ((Boolean) o).booleanValue());
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * <code>boolean</code>.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return a <code>boolean</code> mapped to the column name.
		 */
		public boolean getBoolean(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o != null && ((Boolean) o).booleanValue());
		}

		/**
		 * Returns the value at the specified column index as a <code>byte</code>.
		 *
		 * @param i	The column index
		 * @return	the <code>byte</code> at the specified column index.
		 */
		public byte getByte(final int i) {
			final Object o = data[index][i];

			return (o == null) ? 0 : ((Number) o).byteValue();
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * <code>byte</code>.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return a <code>byte</code> mapped to the column name.
		 */
		public byte getByte(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null) ? 0 : ((Number) o).byteValue();
		}

		/**
		 * Returns the value at the specified column index as a <code>char</code>.
		 *
		 * @param i	The column index
		 * @return	the <code>char</code> at the specified column index.
		 */
		public char getChar(final int i) {
			final Object o = data[index][i];

			return (o == null) ? '\u0000' : o.toString().charAt(0);
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * <code>char</code>.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return a <code>char</code> mapped to the column name.
		 */
		public char getChar(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null) ? '\u0000' : o.toString().charAt(0);
		}

		/**
		 * Returns the value at the specified column index as a {@link Date}.
		 *
		 * @param i	The column index
		 * @return	the {@link Date} at the specified column index.
		 */
		public Date getDate(final int i) {
			return (Date) data[index][i];
		}

		/**
		 * Returns the value mapped to the specified column name as an
		 * {@link Date}.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return an {@link Date} mapped to the column name.
		 */
		public Date getDate(final String colName) {
			return (Date) data[index][indexMap.get(colName)];
		}

		/**
		 * Returns the value at the specified column index as a
		 * <code>double</code>.
		 *
		 * @param i	The column index
		 * @return	the <code>double</code> at the specified column index.
		 */
		public double getDouble(final int i) {
			final Object o = data[index][i];

			return (o == null) ? 0.0d : ((Number) o).doubleValue();
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * <code>double</code>.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return a <code>double</code> mapped to the column name.
		 */
		public double getDouble(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null) ? 0.0d : ((Number) o).doubleValue();
		}

		/**
		 * Returns the value at the specified column index as a <code>float</code>.
		 *
		 * @param i	The column index
		 * @return	the <code>float</code> at the specified column index.
		 */
		public float getFloat(final int i) {
			final Object o = data[index][i];

			return (o == null) ? 0.0f : ((Number) o).floatValue();
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * <code>float</code>.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return a <code>float</code> mapped to the column name.
		 */
		public float getFloat(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null) ? 0.0f : ((Number) o).floatValue();
		}

		/**
		 * Returns the value at the specified column index as an <code>int</code>.
		 *
		 * @param i	The column index
		 * @return	the <code>int</code> at the specified column index.
		 */
		public int getInt(final int i) {
			final Object o = data[index][i];

			return (o == null) ? 0 : ((Number) o).intValue();
		}

		/**
		 * Returns the value mapped to the specified column name as an
		 * <code>int</code>.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return an <code>int</code> mapped to the column name.
		 */
		public int getInt(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null) ? 0 : ((Number) o).intValue();
		}

		/**
		 * Returns an <code>int[]</code> based on the column names submitted.
		 *
		 * @param colNames	The column names.
		 * @return an <code>int[]</code> based on the column names submitted.
		 */
		public int[] getIntArray(final String... colNames) {
			final int[] ints = new int[colNames.length];

			int i=0;
			for (String s : colNames)
				ints[i++] = getInt(s);

			return ints;
		}

		/**
		 * Returns the value at the specified column index as a <code>long</code>.
		 *
		 * @param i	The column index
		 * @return	the <code>long</code> at the specified column index.
		 */
		public long getLong(final int i) {
			final Object o = data[index][i];

			return (o == null) ? 0L : ((Number) o).longValue();
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * <code>long</code>.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return a <code>long</code> mapped to the column name.
		 */
		public long getLong(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null) ? 0L : ((Number) o).longValue();
		}

		/**
		 * Returns the value at the specified column index as an {@link Object}.
		 *
		 * @param i	The column index
		 * @return	the {@link Object} at the specified column index.
		 */
		public Object getObject(final int i) {
			return data[index][i];
		}

		/**
		 * Returns the value mapped to the specified column name as an
		 * {@link Object}.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return an {@link Object} mapped to the column name.
		 */
		public Object getObject(final String colName) {
			return data[index][indexMap.get(colName)];
		}

		/**
		 * Returns the value at the specified column index as a <code>short</code>.
		 *
		 * @param i	The column index
		 * @return	the <code>short</code> at the specified column index.
		 */
		public short getShort(final int i) {
			final Object o = data[index][i];

			return (o == null) ? 0 : ((Number) o).shortValue();
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * <code>short</code>.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return a <code>short</code> mapped to the column name.
		 */
		public short getShort(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null) ? 0 : ((Number) o).shortValue();
		}

		/**
		 * Returns the value at the specified column index as a {@link String}.
		 *
		 * @param i	The column index
		 * @return	the {@link String} at the specified column index.
		 */
		public String getString(final int i) {
			final Object o = data[index][i];

			return (o == null) ? null : o.toString();
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * {@link String}.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return an {@link String} mapped to the column name.
		 */
		public String getString(final String colName) {
			final Object o = data[index][indexMap.get(colName)];

			return (o == null) ? null : o.toString();
		}

		/**
		 * Returns the value at the specified column index as a {@link Time}.
		 *
		 * @param i	The column index
		 * @return	the {@link Time} at the specified column index.
		 */
		public Time getTime(final int i) {
			return (Time) data[index][i];
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * {@link Time}.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return an {@link Time} mapped to the column name.
		 */
		public Time getTime(final String colName) {
			return (Time) data[index][indexMap.get(colName)];
		}

		/**
		 * Returns the value at the specified column index as a
		 * {@link Timestamp}.
		 *
		 * @param i	The column index
		 * @return	the {@link Timestamp} at the specified column index.
		 */
		public Timestamp getTimestamp(final int i) {
			return (Timestamp) data[index][i];
		}

		/**
		 * Returns the value mapped to the specified column name as a
		 * {@link Timestamp}.
		 *
		 * @param colName	The name of the column whose value to fetch.
		 * @return an {@link Timestamp} mapped to the column name.
		 */
		public Timestamp getTimestamp(final String colName) {
			return (Timestamp) data[index][indexMap.get(colName)];
		}

		public int getIndex() {
			return index;
		}

		public boolean hasNext() {
			return ++index < size;
		}

		public Row next() {
			return this;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}	// End Row

}	// End QueryResults
