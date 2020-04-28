package org.sqlbroker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import root.lang.ParamString;

public class RowMapper<T> implements Mapper<T[]> {

	private final Value<T> type;

	@SuppressWarnings("unchecked")
	public RowMapper(final Class<T> clazz) {
		this.type = (Value<T>) Value.classMap.get(clazz);
	}

	public T[] map(final ResultSet rs) throws SQLException {
		return type.getRow(rs, rs.getMetaData().getColumnCount());
	}

	@Override
	public String toString() {
		return ParamString.formatMsg("{P}[]", type);
	}

} // End RowMapper
