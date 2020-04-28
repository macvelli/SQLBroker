package org.sqlbroker;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import root.adt.ListExtractable;
import root.jdbc.SqlType;
import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 * - Does not include @Deprecated methods such as <code>void setUnicodeStream()</code>.
 *
 * TODO:
 * 		+ Search for SQLException, there shouldn't be any until the Value classes (done)
 * 		+ Go to beginning of Value class declarations and search for index, there shouldn't be any in the Value classes (done)
 *
 * @author esmith
 */
public final class Parameters implements Extractable {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private int index;
	private final ListExtractable<Value> values;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Parameters(final int size) {
		values = new ListExtractable<>(size);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final Parameters add(final Array a) {
		index++;
		values.add(new ArrayValue(index, a));
		return this;
	}

	public final Parameters addAsciiStream(final InputStream is) {
		index++;
		values.add(new AsciiStreamValue(index, is));
		return this;
	}

	public final Parameters addAsciiStream(final InputStream is, final int length) {
		index++;
		values.add(new AsciiStreamValue(index, is, length));
		return this;
	}

	public final Parameters add(final BigDecimal b) {
		index++;
		values.add((b == null) ? new NullValue(index, SqlType.DECIMAL) : new BigDecimalValue(index, b));
		return this;
	}

	public final Parameters addBinaryStream(final InputStream is) {
		index++;
		values.add(new BinaryStreamValue(index, is));
		return this;
	}

	public final Parameters addBinaryStream(final InputStream is, final int length) {
		index++;
		values.add(new BinaryStreamValue(index, is, length));
		return this;
	}

	public final Parameters addBlob(final Blob b) {
		index++;
		values.add(new BlobValue(index, b));
		return this;
	}

	public final Parameters addBlob(final InputStream is) {
		index++;
		values.add(new BlobValue(index, is));
		return this;
	}

	public final Parameters addBlob(final InputStream is, final long length) {
		index++;
		values.add(new BlobValue(index, is, length));
		return this;
	}

	public final Parameters add(final boolean b) {
		index++;
		values.add(new BooleanValue(index, b));
		return this;
	}

	public final Parameters add(final Boolean b) {
		index++;
		values.add((b == null) ? new NullValue(index, SqlType.BOOLEAN) : new BooleanValue(index, b));
		return this;
	}

	public final Parameters add(final byte b) {
		index++;
		values.add(new ByteValue(index, b));
		return this;
	}

	public final Parameters add(final Byte b) {
		index++;
		values.add((b == null) ? new NullValue(index, SqlType.TINYINT) : new ByteValue(index, b));
		return this;
	}

	public final Parameters add(final byte[] b) {
		index++;
		values.add((b == null) ? new NullValue(index, SqlType.BINARY) : new ByteArrayValue(index, b));
		return this;
	}

	public final Parameters addCharacterStream(final Reader reader) {
		index++;
		values.add(new CharacterStreamValue(index, reader));
		return this;
	}

	public final Parameters addCharacterStream(final Reader reader, final int length) {
		index++;
		values.add(new CharacterStreamValue(index, reader, length));
		return this;
	}

	public final Parameters addClob(final Clob c) {
		index++;
		values.add(new ClobValue(index, c));
		return this;
	}

	public final Parameters addClob(final Reader reader) {
		index++;
		values.add(new ClobValue(index, reader));
		return this;
	}

	public final Parameters addClob(final Reader reader, final long length) {
		index++;
		values.add(new ClobValue(index, reader, length));
		return this;
	}

	public final Parameters add(final Date d) {
		index++;
		values.add((d == null) ? new NullValue(index, SqlType.DATE) : new DateValue(index, d));
		return this;
	}

	public final Parameters add(final Date d, final Calendar cal) {
		index++;
		values.add((d == null) ? new NullValue(index, SqlType.DATE) : new DateValue(index, d, cal));
		return this;
	}

	public final Parameters add(final double d) {
		index++;
		values.add(new DoubleValue(index, d));
		return this;
	}

	public final Parameters add(final Double d) {
		index++;
		values.add((d == null) ? new NullValue(index, SqlType.DOUBLE) : new DoubleValue(index, d));
		return this;
	}

	public final Parameters add(final float f) {
		index++;
		values.add(new FloatValue(index, f));
		return this;
	}

	public final Parameters add(final Float f) {
		index++;
		values.add((f == null) ? new NullValue(index, SqlType.FLOAT) : new FloatValue(index, f));
		return this;
	}

	public final Parameters add(final int i) {
		index++;
		values.add(new IntegerValue(index, i));
		return this;
	}

	public final Parameters add(final Integer i) {
		index++;
		values.add((i == null) ? new NullValue(index, SqlType.INTEGER) : new IntegerValue(index, i));
		return this;
	}

	public final Parameters add(final long l) {
		index++;
		values.add(new LongValue(index, l));
		return this;
	}

	public final Parameters add(final Long l) {
		index++;
		values.add((l == null) ? new NullValue(index, SqlType.BIGINT) : new LongValue(index, l));
		return this;
	}

	public final Parameters addNCharacterStream(final Reader reader) {
		index++;
		values.add(new NCharacterStreamValue(index, reader));
		return this;
	}

	public final Parameters addNCharacterStream(final Reader reader, final long length) {
		index++;
		values.add(new NCharacterStreamValue(index, reader, length));
		return this;
	}

	public final Parameters addNClob(final NClob c) {
		index++;
		values.add(new NClobValue(index, c));
		return this;
	}

	public final Parameters addNClob(final Reader reader) {
		index++;
		values.add(new NClobValue(index, reader));
		return this;
	}

	public final Parameters addNClob(final Reader reader, final long length) {
		index++;
		values.add(new NClobValue(index, reader, length));
		return this;
	}

	public final Parameters addNString(final String s) {
		index++;
		values.add((s == null) ? new NullValue(index, SqlType.NVARCHAR) : new NStringValue(index, s));
		return this;
	}

	public final Parameters add(final Object o) {
		index++;
		values.add(new ObjectValue(index, o));
		return this;
	}

	public final Parameters add(final Object o, final SqlType targetType) {
		index++;
		values.add(new ObjectValue(index, o, targetType));
		return this;
	}

	public final Parameters add(final Object o, final SqlType targetType, final int scale) {
		index++;
		values.add(new ObjectValue(index, o, targetType, scale));
		return this;
	}

	public final Parameters add(final Object... objs) {
		for (Object o : objs) {
			index++;
			values.add(new ObjectValue(index, o));
		}
		return this;
	}

	public final Parameters add(final Ref r) {
		index++;
		values.add(new RefValue(index, r));
		return this;
	}

	public final Parameters add(final RowId r) {
		index++;
		values.add(new RowIdValue(index, r));
		return this;
	}

	public final Parameters add(final short s) {
		index++;
		values.add(new ShortValue(index, s));
		return this;
	}

	public final Parameters add(final Short s) {
		index++;
		values.add((s == null) ? new NullValue(index, SqlType.SMALLINT) : new ShortValue(index, s));
		return this;
	}

	public final Parameters add(final SQLXML s) {
		index++;
		values.add(new SQLXMLValue(index, s));
		return this;
	}

	public final Parameters add(final String s) {
		index++;
		values.add((s == null) ? new NullValue(index, SqlType.VARCHAR) : new StringValue(index, s));
		return this;
	}

	public final Parameters add(final Time t) {
		index++;
		values.add((t == null) ? new NullValue(index, SqlType.TIME) : new TimeValue(index, t));
		return this;
	}

	public final Parameters add(final Time t, final Calendar cal) {
		index++;
		values.add((t == null) ? new NullValue(index, SqlType.TIME) : new TimeValue(index, t, cal));
		return this;
	}

	public final Parameters add(final Timestamp t) {
		index++;
		values.add((t == null) ? new NullValue(index, SqlType.TIMESTAMP) : new TimestampValue(index, t));
		return this;
	}

	public final Parameters add(final Timestamp t, final Calendar cal) {
		index++;
		values.add((t == null) ? new NullValue(index, SqlType.TIMESTAMP) : new TimestampValue(index, t, cal));
		return this;
	}

	public final Parameters add(final URL url) {
		index++;
		values.add(new URLValue(index, url));
		return this;
	}

	public final int getSize() {
		return values.getSize();
	}

	public final ListExtractable<Value> getValues() {
		return values;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append("Parameters:");
		values.extract(extractor);
	}

	@Override
	public final String toString() {
		final StringExtractor chars = new StringExtractor(256);
		extract(chars);
		return chars.toString();
	}

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	abstract class Value implements Extractable {

		protected final int i;

		Value(final int i) {
			this.i = i;
		}

		public abstract void setValue(PreparedStatement statement) throws SQLException;

	}	// End Value

	private final class ArrayValue extends Value {

		private final Array a;

		private ArrayValue(final int i, final Array a) {
			super(i);
			this.a = a;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setArray(i, a);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(a);
		}

	}	// End ArrayValue

	private final class AsciiStreamValue extends Value {

		private final int length;
		private final InputStream is;

		private AsciiStreamValue(final int i, final InputStream is) {
			super(i);
			this.is = is;
			this.length = 0;
		}

		private AsciiStreamValue(final int i, final InputStream is, final int length) {
			super(i);
			this.is = is;
			this.length = length;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (length > 0) {
				statement.setAsciiStream(i, is, length);
			} else {
				statement.setAsciiStream(i, is);
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(is);
		}

	}	// End AsciiStreamValue

	private final class BigDecimalValue extends Value {

		private final BigDecimal b;

		private BigDecimalValue(final int i, final BigDecimal b) {
			super(i);
			this.b = b;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setBigDecimal(i, b);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(b);
		}

	}	// End BigDecimalValue

	private final class BinaryStreamValue extends Value {

		private final int length;
		private final InputStream is;

		private BinaryStreamValue(final int i, final InputStream is) {
			super(i);
			this.is = is;
			this.length = 0;
		}

		private BinaryStreamValue(final int i, final InputStream is, final int length) {
			super(i);
			this.is = is;
			this.length = length;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (length > 0) {
				statement.setBinaryStream(i, is, length);
			} else {
				statement.setBinaryStream(i, is);
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(is);
		}

	}	// End BinaryStreamValue

	private final class BlobValue extends Value {

		private final Blob b;
		private final InputStream is;
		private final long length;

		private BlobValue(final int i, final Blob b) {
			super(i);
			this.b = b;
			this.is = null;
			this.length = 0;
		}

		private BlobValue(final int i, final InputStream is) {
			super(i);
			this.b = null;
			this.is = is;
			this.length = 0;
		}

		private BlobValue(final int i, final InputStream is, final long length) {
			super(i);
			this.b = null;
			this.is = is;
			this.length = length;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (is == null) {
				statement.setBlob(i, b);
			} else {
				if (length > 0) {
					statement.setBlob(i, is, length);
				} else {
					statement.setBlob(i, is);
				}
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			if (is == null) {
				extractor.append(b);
			} else {
				extractor.append(is);
			}
		}

	}	// End BlobValue

	private final class BooleanValue extends Value {

		private final boolean b;

		private BooleanValue(final int i, final boolean b) {
			super(i);
			this.b = b;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setBoolean(i, b);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(b);
		}

	}	// End BooleanValue

	private final class ByteValue extends Value {

		private final byte b;

		private ByteValue(final int i, final byte b) {
			super(i);
			this.b = b;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setByte(i, b);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(b);
		}

	}	// End ByteValue

	private final class ByteArrayValue extends Value {

		private final byte[] b;

		private ByteArrayValue(final int i, final byte[] b) {
			super(i);
			this.b = b;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setBytes(i, b);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(b);
		}

	}	// End ByteArrayValue

	private final class CharacterStreamValue extends Value {

		private final int length;
		private final Reader r;

		private CharacterStreamValue(final int i, final Reader r) {
			super(i);
			this.r = r;
			this.length = 0;
		}

		private CharacterStreamValue(final int i, final Reader r, final int length) {
			super(i);
			this.r = r;
			this.length = length;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (length > 0) {
				statement.setCharacterStream(i, r, length);
			} else {
				statement.setCharacterStream(i, r);
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(r);
		}

	}	// End CharacterStreamValue

	private final class ClobValue extends Value {

		private final Clob c;
		private final Reader reader;
		private final long length;

		private ClobValue(final int i, final Clob c) {
			super(i);
			this.c = c;
			this.reader = null;
			this.length = 0;
		}

		private ClobValue(final int i, final Reader reader) {
			super(i);
			this.c = null;
			this.reader = reader;
			this.length = 0;
		}

		private ClobValue(final int i, final Reader reader, final long length) {
			super(i);
			this.c = null;
			this.reader = reader;
			this.length = length;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (reader == null) {
				statement.setClob(i, c);
			} else {
				if (length > 0) {
					statement.setClob(i, reader, length);
				} else {
					statement.setClob(i, reader);
				}
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			if (reader == null) {
				extractor.append(c);
			} else {
				extractor.append(reader);
			}
		}

	}	// End ClobValue

	private final class DateValue extends Value {

		private final Date d;
		private final Calendar cal;

		private DateValue(final int i, final Date d) {
			super(i);
			this.d = d;
			this.cal = null;
		}

		private DateValue(final int i, final Date d, final Calendar cal) {
			super(i);
			this.d = d;
			this.cal = cal;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (cal == null) {
				statement.setDate(i, new java.sql.Date(d.getTime()));
			} else {
				statement.setDate(i, new java.sql.Date(d.getTime()), cal);
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(d);
		}

	}	// End DateValue

	private final class DoubleValue extends Value {

		private final double d;

		private DoubleValue(final int i, final double d) {
			super(i);
			this.d = d;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setDouble(i, d);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(d);
		}

	}	// End DoubleValue

	private final class FloatValue extends Value {

		private final float f;

		private FloatValue(final int i, final float f) {
			super(i);
			this.f = f;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setFloat(i, f);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(f);
		}

	}	// End FloatValue

	private final class IntegerValue extends Value {

		private final int j;

		private IntegerValue(final int i, final int j) {
			super(i);
			this.j = j;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setInt(i, j);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(j);
		}

	}	// End IntegerValue

	private final class LongValue extends Value {

		private final long l;

		private LongValue(final int i, final long l) {
			super(i);
			this.l = l;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setLong(i, l);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(l);
		}

	}	// End LongValue

	private final class NCharacterStreamValue extends Value {

		private final Reader reader;
		private final long length;

		private NCharacterStreamValue(final int i, final Reader reader) {
			super(i);
			this.reader = reader;
			this.length = 0;
		}

		private NCharacterStreamValue(final int i, final Reader reader, final long length) {
			super(i);
			this.reader = reader;
			this.length = length;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (length > 0) {
				statement.setNCharacterStream(i, reader, length);
			} else {
				statement.setNCharacterStream(i, reader);
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(reader);
		}

	}	// End NCharacterStreamValue

	private final class NClobValue extends Value {

		private final NClob c;
		private final Reader reader;
		private final long length;

		private NClobValue(final int i, final NClob c) {
			super(i);
			this.c = c;
			this.reader = null;
			this.length = 0;
		}

		private NClobValue(final int i, final Reader reader) {
			super(i);
			this.c = null;
			this.reader = reader;
			this.length = 0;
		}

		private NClobValue(final int i, final Reader reader, final long length) {
			super(i);
			this.c = null;
			this.reader = reader;
			this.length = length;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (reader == null) {
				statement.setNClob(i, c);
			} else {
				if (length > 0) {
					statement.setNClob(i, reader, length);
				} else {
					statement.setNClob(i, reader);
				}
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			if (reader == null) {
				extractor.append(c);
			} else {
				extractor.append(reader);
			}
		}

	}	// End NClobValue

	private final class NStringValue extends Value {

		private final String s;

		private NStringValue(final int i, final String s) {
			super(i);
			this.s = s;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setNString(i, s);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(s);
		}

	}	// End NStringValue

	private final class NullValue extends Value {

		private final SqlType t;

		private NullValue(final int i, final SqlType t) {
			super(i);
			this.t = t;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setNull(i, t.getCode());
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(t);
		}

	}	// End NullValue

	private final class ObjectValue extends Value {

		private final Object o;
		private final SqlType targetType;
		private final int scale;

		private ObjectValue(final int i, final Object o) {
			super(i);
			this.o = o;
			this.targetType = null;
			this.scale = -1;
		}

		private ObjectValue(final int i, final Object o, final SqlType targetType) {
			super(i);
			this.o = o;
			this.targetType = targetType;
			this.scale = -1;
		}

		private ObjectValue(final int i, final Object o, final SqlType targetType, final int scale) {
			super(i);
			this.o = o;
			this.targetType = targetType;
			this.scale = scale;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (targetType == null) {
				statement.setObject(i, o);
			} else {
				if (scale < 0) {
					statement.setObject(i, o, targetType.getCode());
				} else {
					statement.setObject(i, o, targetType.getCode(), scale);
				}
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(o);
		}

	}	// End ObjectValue

	private final class RefValue extends Value {

		private final Ref r;

		private RefValue(final int i, final Ref r) {
			super(i);
			this.r = r;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setRef(i, r);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(r);
		}

	}	// End RefValue

	private final class RowIdValue extends Value {

		private final RowId r;

		private RowIdValue(final int i, final RowId r) {
			super(i);
			this.r = r;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setRowId(i, r);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(r);
		}

	}	// End RowIdValue

	private final class ShortValue extends Value {

		private final short s;

		private ShortValue(final int i, final short s) {
			super(i);
			this.s = s;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setShort(i, s);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(s);
		}

	}	// End ShortValue

	private final class SQLXMLValue extends Value {

		private final SQLXML s;

		private SQLXMLValue(final int i, final SQLXML s) {
			super(i);
			this.s = s;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setSQLXML(i, s);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(s);
		}

	}	// End SQLXMLValue

	private final class StringValue extends Value {

		private final String s;

		private StringValue(final int i, final String s) {
			super(i);
			this.s = s;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setString(i, s);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(s);
		}

	}	// End StringValue

	private final class TimeValue extends Value {

		private final Time t;
		private final Calendar cal;

		private TimeValue(final int i, final Time t) {
			super(i);
			this.t = t;
			this.cal = null;
		}

		private TimeValue(final int i, final Time t, final Calendar cal) {
			super(i);
			this.t = t;
			this.cal = cal;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (cal == null) {
				statement.setTime(i, t);
			} else {
				statement.setTime(i, t, cal);
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(t);
		}

	}	// End TimeValue

	private final class TimestampValue extends Value {

		private final Timestamp t;
		private final Calendar cal;

		private TimestampValue(final int i, final Timestamp t) {
			super(i);
			this.t = t;
			this.cal = null;
		}

		private TimestampValue(final int i, final Timestamp t, final Calendar cal) {
			super(i);
			this.t = t;
			this.cal = cal;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			if (cal == null) {
				statement.setTimestamp(i, t);
			} else {
				statement.setTimestamp(i, t, cal);
			}
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(t);
		}

	}	// End TimestampValue

	private final class URLValue extends Value {

		private final URL url;

		private URLValue(final int i, final URL url) {
			super(i);
			this.url = url;
		}

		@Override
		public final void setValue(final PreparedStatement statement) throws SQLException {
			statement.setURL(i, url);
		}

		@Override
		public final void extract(final StringExtractor extractor) {
			extractor.append(url);
		}

	}	// End URLValue

} // End Parameters
