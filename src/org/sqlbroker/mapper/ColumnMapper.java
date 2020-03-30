package org.sqlbroker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import root.lang.ParamStr;

@SuppressWarnings("unchecked")
public class ColumnMapper<T> implements Mapper<T> {

	private final int		colIndex;
	private final String	colName;
	private final Value<T>	type;

	public ColumnMapper(final Class<T> clazz) {
		type = (Value<T>) Value.classMap.get(clazz);
		colName = null;
		colIndex = 1;
	}

	public ColumnMapper(final Class<T> clazz, final int colIndex) {
		type = (Value<T>) Value.classMap.get(clazz);
		colName = null;
		this.colIndex = colIndex;
	}

	public ColumnMapper(final Class<T> clazz, final String colName) {
		type = (Value<T>) Value.classMap.get(clazz);
		this.colName = colName;
		colIndex = 0;
	}

	public T map(final ResultSet rs) throws SQLException {
		return type.get(rs, (colName == null) ? colIndex : rs.findColumn(colName));
	}

	@Override
	public String toString() {
		return ParamStr.format("{P} from column {P}", type, (colName == null) ? colIndex : colName);
	}

}	// End ColumnMapper
