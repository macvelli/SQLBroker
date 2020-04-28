package org.sqlbroker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import root.adt.MapHashed;


public interface Value<V> {

	public V get(final ResultSet rs, final int i) throws SQLException;

	public V[] getRow(final ResultSet rs, final int rowLen) throws SQLException;

//	Value<BigDecimal> BIGDECIMAL = new Value<BigDecimal>() {
//		public BigDecimal get(final ResultSet rs, final int i) throws SQLException { return rs.getBigDecimal(i); }
//
//		public BigDecimal[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final BigDecimal[] t = new BigDecimal[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getBigDecimal(++i);
//
//			return t;
//		}
//
//		public String toString() { return "BigDecimal"; }
//
//	};
//
//	Value<Boolean> BOOLEAN = new Value<Boolean>() {
//		public Boolean get(final ResultSet rs, final int i) throws SQLException { return rs.getBoolean(i); }
//
//		public Boolean[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Boolean[] t = new Boolean[rowLen];
//
//			for (int i=0; i < rowLe	// End Valuen; )
//				t[i] = rs.getBoolean(++i);
//
//			return t;
//		}
//
//		public String toString() { return "boolean"; }
//	};
//
//	Value<Byte> BYTE = new Value<Byte>() {
//		public Byte get(final ResultSet rs, final int i) throws SQLException { return rs.getByte(i); }
//
//		public Byte[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Byte[] t = new Byte[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getByte(++i);
//
//			return t;
//		}
//
//		public String toString() { return "byte"; }
//	};
//
//	Value<byte[]> BYTES = new Value<byte[]>() {
//		public byte[] get(final ResultSet rs, final int i) throws SQLException { return rs.getBytes(i); }
//
//		public byte[][] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final byte[][] t = new byte[rowLen][];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getBytes(++i);
//
//			return t;
//		}
//
//		public String toString() { return "byte[]"; }
//	};
//
//	Value<Date> DATE = new Value<Date>() {
//		public Date get(final ResultSet rs, final int i) throws SQLException { return rs.getDate(i); }
//
//		public Date[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Date[] t = new Date[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getDate(++i);
//
//			return t;
//		}
//
//		public String toString() { return "Date"; }
//	};
//
//	Value<Double> DOUBLE = new Value<Double>() {
//		public Double get(final ResultSet rs, final int i) throws SQLException { return rs.getDouble(i); }
//
//		public Double[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Double[] t = new Double[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getDouble(++i);
//
//			return t;
//		}
//
//		public String toString() { return "double"; }
//	};
//
//	Value<Float> FLOAT = new Value<Float>() {
//		public Float get(final ResultSet rs, final int i) throws SQLException { return rs.getFloat(i); }
//
//		public Float[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Float[] t = new Float[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getFloat(++i);
//
//			return t;
//		}
//
//		public String toString() { return "float"; }
//	};
//
//	Value<Integer> INTEGER = new Value<Integer>() {
//		public Integer get(final ResultSet rs, final int i) throws SQLException { return rs.getInt(i);	}
//
//		public Integer[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Integer[] t = new Integer[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getInt(++i);
//
//			return t;
//		}
//
//		public String toString() { return "int"; }
//	};
//
//	Value<Long> LONG = new Value<Long>() {
//		public Long get(final ResultSet rs, final int i) throws SQLException { return rs.getLong(i); }
//
//		public Long[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Long[] t = new Long[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getLong(++i);
//
//			return t;
//		}
//
//		public String toString() { return "long"; }
//	};
//
//	Value<Object> OBJECT = new Value<Object>() {
//		public Object get(final ResultSet rs, final int i) throws SQLException { return rs.getObject(i); }
//
//		public Object[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Object[] t = new Object[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getObject(++i);
//
//			return t;
//		}
//
//		public String toString() { return "Object"; }
//	};
//
//	Value<Short> SHORT = new Value<Short>() {
//		public Short get(final ResultSet rs, final int i) throws SQLException { return rs.getShort(i); }
//
//		public Short[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Short[] t = new Short[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getShort(++i);
//
//			return t;
//		}
//
//		public String toString() { return "short"; }
//	};
//
//	Value<String> STRING = new Value<String>() {
//		public String get(final ResultSet rs, final int i) throws SQLException { return rs.getString(i); }
//
//		public String[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final String[] t = new String[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getString(++i);
//
//			return t;
//		}
//
//		public String toString() { return "String"; }
//	};
//
//	Value<Timestamp> TIMESTAMP = new Value<Timestamp>() {
//		public Timestamp get(final ResultSet rs, final int i) throws SQLException { return rs.getTimestamp(i); }
//
//		public Timestamp[] getRow(final ResultSet rs, final int rowLen) throws SQLException {
//			final Timestamp[] t = new Timestamp[rowLen];
//
//			for (int i=0; i < rowLen; )
//				t[i] = rs.getTimestamp(++i);
//
//			return t;
//		}
//
//		public String toString() { return "Timestamp"; }
//	};

	MapHashed<Class<?>, Value<?>> classMap = new MapHashed<>();
//
//	ConstantMap<Class<?>, Value<?>>	classMap = new ConstantMap<Class<?>, Value<?>>(
//			new Class<?>[] {BigDecimal.class, boolean.class, byte.class, byte[].class, Date.class, double.class, float.class, int.class, long.class, Object.class, short.class, String.class, Timestamp.class},
//			new Value<?>[] {Value.BIGDECIMAL, Value.BOOLEAN, Value.BYTE, Value.BYTES, Value.DATE, Value.DOUBLE, Value.FLOAT, Value.INTEGER, Value.LONG, Value.OBJECT, Value.SHORT, Value.STRING, Value.TIMESTAMP},
//			13);

}
