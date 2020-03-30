package org.sqlbroker.helper;

/**
 * 
 * @author esmith
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
class Conditional<T extends Conditional<?>> extends SQL {

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final T where(final String criteria) {
		buf.append(" WHERE ").append(criteria);
		return (T) this;
	}

	public final T and(final String criteria) {
		buf.append(" AND ").append(criteria);
		return (T) this;
	}

	public final T and(final String criteria, final Object... values) {
		buf.append(" AND ").append(criteria);
		params.add(values);
		return (T) this;
	}

	public final T equalTo(final Object value) {
		buf.append(" = ?");
		params.add(value);
		return (T) this;
	}

	public final T notEqualTo(final Object value) {
		buf.append(" <> ?");
		params.add(value);
		return (T) this;
	}

	public final T greaterThan(final Object value) {
		buf.append(" > ?");
		params.add(value);
		return (T) this;
	}

	public final T greaterThanOrEqualTo(final Object value) {
		buf.append(" >= ?");
		params.add(value);
		return (T) this;
	}

	public final T lessThan(final Object value) {
		buf.append(" < ?");
		params.add(value);
		return (T) this;
	}

	public final T lessThanOrEqualTo(final Object value) {
		buf.append(" <= ?");
		params.add(value);
		return (T) this;
	}

	public final T like(final String value) {
		buf.append(" LIKE ?");
		params.add(value);
		return (T) this;
	}

	public final T notLike(final String value) {
		buf.append(" NOT LIKE ?");
		params.add(value);
		return (T) this;
	}

	public final T in(final Object... objs) {
		if (objs.length == 1) {
			return equalTo(objs[0]);
		}

		buf.append(" IN (");
		appendParams(objs.length);
		buf.append(')');
		params.add(objs);
		return (T) this;
	}

	public final T notIn(final Object... objs) {
		if (objs.length == 1) {
			return notEqualTo(objs[0]);
		}

		buf.append(" NOT IN (");
		appendParams(objs.length);
		buf.append(')');
		params.add(objs);
		return (T) this;
	}

	public final T isNull() {
		buf.append(" IS NULL");
		return (T) this;
	}

	public final T isNotNull() {
		buf.append(" IS NOT NULL");
		return (T) this;
	}

	public final T between(final Object a, final Object b) {
		buf.append(" BETWEEN ? AND ?");
		params.add(a);
		params.add(b);
		return (T) this;
	}

}
