package org.sqlbroker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import root.util.Jdbc;

class ResultSetHandler implements InvocationHandler {

	/** The {@link Connection} that executed the SQL statement		*/
	private final Connection	con;

	/** The {@link Statement} that created the {@link ResultSet}	*/
	private final Statement		stmt;

	/** The wrapped {@link ResultSet}								*/
	private final ResultSet		rs;

	ResultSetHandler(final Connection con, final Statement stmt, final ResultSet rs) {
		this.con = con;
		this.stmt = stmt;
		this.rs = rs;
	}

	public Object invoke(final Object proxy, final Method m, final Object[] args) throws Throwable {
		if (m.getName().equals("close")) {
			Jdbc.close(con, stmt, rs);
			return null;
		}

		return m.invoke(rs, args);
	}

}	// End ResultSetHandler
