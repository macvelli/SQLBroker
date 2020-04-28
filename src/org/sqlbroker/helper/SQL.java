package org.sqlbroker.helper;

import org.sqlbroker.Parameters;

import root.lang.StringExtractor;

public abstract class SQL {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	StringExtractor		buf;
	final Parameters	params;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	SQL() {
		params = new Parameters(8);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final Parameters getParams() {
		return params;
	}

	@Override
	public String toString() {
		return buf.toString();
	}

	//  <><><><><><><><><><><><><>< Package Methods ><><><><><><><><><><><><><>

	final void appendParams(final int numParams) {
		if (numParams > 0) {
			buf.append('?');

			for (int i=1; i < numParams; i++) {
				buf.addSeparator().append('?');
			}
		}
	}

} // End SQL
