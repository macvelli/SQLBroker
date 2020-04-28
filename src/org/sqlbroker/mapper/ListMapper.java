package org.sqlbroker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import root.adt.ListArray;
import root.lang.ParamString;

@SuppressWarnings("unchecked")
public class ListMapper<T> implements Mapper<ListArray<T>> {

	private final int		colIndex;
	private final String	colName;
	private final Value<T>	type;

	public ListMapper(final Class<T> clazz) {
		type = (Value<T>) Value.classMap.get(clazz);
		colName = null;
		colIndex = 1;
	}

	public ListMapper(final Class<T> clazz, final int colIndex) {
		type = (Value<T>) Value.classMap.get(clazz);
		colName = null;
		this.colIndex = colIndex;
	}

	public ListMapper(final Class<T> clazz, final String colName) {
		type = (Value<T>) Value.classMap.get(clazz);
		this.colName = colName;
		colIndex = 0;
	}

	public ListArray<T> map(final ResultSet rs) throws SQLException {
		final ListArray<T> a = new ListArray<T>();
		final int i = (colName == null) ? colIndex : rs.findColumn(colName);

		do {
			a.add(type.get(rs, i));
		} while (rs.next());

		return a;
	}

	@Override
	public String toString() {
		return ParamString.formatMsg("List<{P}> from column {P}", type, (colName == null) ? colIndex : colName);
	}

} // End ListMapper
