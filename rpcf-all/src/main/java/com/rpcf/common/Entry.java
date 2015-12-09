package com.rpcf.common;

public class Entry<R, C> {
	
	private R row;
	
	private C column;
	
	public R getRow() {
		return row;
	}
	public void setRow(R row) {
		this.row = row;
	}
	public C getColumn() {
		return column;
	}
	public void setColumn(C column) {
		this.column = column;
	}
	
	public static <R, C> Entry<R, C> getEntry(R r, C c) {
		Entry<R, C> entry = new Entry<R, C>();
		entry.setRow(r);
		entry.setColumn(c);
		return entry;
	}
}
