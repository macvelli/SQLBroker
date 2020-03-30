/*
 * Open source license text goes here.
 */

package org.sqlbroker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.sqlbroker.BatchResult;
import org.sqlbroker.SQLBroker;

import root.jdbc.DatabaseException;
import root.jdbc.PooledConnection;
import root.jdbc.PooledDataSource;
import root.log.Log;

/**
 * Allows constraints to be placed on SQLScriptRunner by database name and/or
 * database hostname. You haven't lived until you blow away the entire
 * production database and lose 16 hours worth of user data not yet backed up
 * because you accidentally executed the DAO JUnits against the wrong data
 * source. If you are lucky, take a look at using these constraints after
 * nearly losing your job.
 *
 * @author Edward Smith
 */
public final class SQLScriptRunner {

	// <><><><><><><><><><><><><>< Static Artifacts ><><><><><><><><><><><><><>

	private static final Log log = new Log(SQLScriptRunner.class);

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private String				dbName;
	private String				dbURL;
	private String				dbNameConstraint;
	private String				dbHostConstraint;
	private final SQLBroker		broker;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SQLScriptRunner(final PooledDataSource dataSource) {
		this.setDBMetaData(dataSource);
		broker = new SQLBroker(dataSource);
	}

	public SQLScriptRunner(final String driverName, final String conURL, final String username, final String password) {
		this(new PooledDataSource(driverName, conURL, username, password, 1));
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Constrains SQLScriptRunner from running on any database other than the
	 * one named here.
	 *
	 * @param dbName	The database name to set.
	 */
	public final void setDbName(final String dbNameConstraint) {
		this.dbNameConstraint = dbNameConstraint.toUpperCase();
	}

	/**
	 * Constrains SQLScriptRunner from running against any host other than the
	 * one specified here.
	 *
	 * @param dbHost	The database host to set.
	 */
	public final void setDbHost(final String dbHostConstraint) {
		this.dbHostConstraint = dbHostConstraint.toUpperCase();
	}

	public final void execute(final String sqlFile) throws IOException {
		if ((dbNameConstraint != null && !dbName.contains(dbNameConstraint))
			|| (dbHostConstraint != null && !dbURL.contains(dbHostConstraint))) {
			throw new DatabaseException("Database contstraint violation: Attempting to execute against " + dbName + "/" + dbURL + " instead of " + dbNameConstraint + "/" + dbHostConstraint);
		}

		final String[] stmts = this.readScript(new File(sqlFile));
		final BatchResult results = broker.batch(stmts);

		log.debug("Script execution results:\n{P}", results);
	}

//	***************************** Private Methods ****************************

	private void setDBMetaData(final PooledDataSource dataSource) {
		final PooledConnection con = dataSource.getConnection();

		try {
			dbName = con.getMetaData().getDatabaseProductName().toUpperCase();
			dbURL = con.getMetaData().getURL().toUpperCase();
			log.debug("Database Name = {P}, Database URL = {P}", dbName, dbURL);
		} catch (SQLException e) {
			log.warn("Could not retrieve database information", e);
		} finally {
			con.close();
		}
	}

	private String[] readScript(final File f) throws IOException {
		int		cmtIdx;
		String	line;
		final StringBuilder builder = new StringBuilder((int)(f.length() + 1));
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				cmtIdx = line.indexOf("--");			// Handle SQL comments
				if (cmtIdx != 0)
					builder.append((cmtIdx > 0) ? line.substring(0, cmtIdx).trim() : line);
			}
		} finally {
			if (br != null)
				br.close();
		}

		return builder.toString().split(";");
	}

}
