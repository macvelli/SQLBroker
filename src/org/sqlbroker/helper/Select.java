package org.sqlbroker.helper;

import root.lang.StringExtractor;

public final class Select extends Conditional<Select> {

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Select() {}

	public Select(final String stub) {
		buf = new StringExtractor(stub.length() + 256);
		buf.append("SELECT ").append(stub);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final Select select(final String... cols) {
		buf = new StringExtractor(325 + (cols.length << 4));
		buf.append("SELECT ");
		append(cols);
		return this;
	}

	public final Select selectAll() {
		buf = new StringExtractor(325);
		buf.append("SELECT *");
		return this;
	}

	public final Select selectDistinct(final String... cols) {
		buf = new StringExtractor(335 + (cols.length << 4));
		buf.append("SELECT DISTINCT ");
		append(cols);
		return this;
	}

	public final Select from(final String table) {
		buf.append(" FROM ").append(table);
		return this;
	}

	public final Select orderBy(final String... cols) {
		buf.append(" ORDER BY ");
		append(cols);
		return this;
	}

	//  <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private void append(final String[] cols) {
		if (cols.length > 0) {
			buf.append(cols[0]);

			for (int i=1; i < cols.length; i++) {
				buf.addSeparator().append(cols[i]);
			}
		}
	}

	/**
	 * TODO: Delete this method
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		final Select stmt = new Select();

		stmt.select("ENTITYITEMNUM", "DESCRTEXT")
			.from("ACRPRICE.ACRPRICE_ITM")
			.where("RECRDTYPE = 'ITM'")
			.and("RECRDSTATUSCODE = 'A'")
			.and("INVITEMIND = 'Y'")
			.and("ENTITYCODE").equalTo("PHA")
			.and("ENTITYITEMNUM").like("30452%")
			.and("DESCRTEXT").like("CH%")
			.orderBy("DESCRTEXT");

		System.out.println(stmt);
	}

} // End Select
