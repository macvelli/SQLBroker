package org.sqlbroker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import root.adt.ListArray;
import root.lang.ParamString;

public class TableMapper<T> implements Mapper<ListArray<T[]>> {

	private final Value<T> type;

	@SuppressWarnings("unchecked")
	public TableMapper(final Class<T> clazz) {
		this.type = (Value<T>) Value.classMap.get(clazz);
	}

	public ListArray<T[]> map(final ResultSet rs) throws SQLException {
		final int rowLen = rs.getMetaData().getColumnCount();
		final ListArray<T[]> a = new ListArray<T[]>();

		do {
			a.add(type.getRow(rs, rowLen));
		} while (rs.next());

		return a;
	}

	@Override
	public String toString() {
		return ParamString.formatMsg("List<{P}[]>", type);
	}

} // End TableMapper
