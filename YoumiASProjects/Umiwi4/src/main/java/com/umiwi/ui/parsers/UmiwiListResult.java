package com.umiwi.ui.parsers;

import java.util.ArrayList;

public class UmiwiListResult<E> {
	private int currentPage;
	private int toalPage;
	private int totalRows;
	private int total;
	private ArrayList<E> items;

	public int getCurrentPage() {
		return currentPage;
	}

	public int getToalPage() {
		return toalPage;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getTotal() {
		return total;
	}

	public ArrayList<E> getItems() {
		return items;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setToalPage(int toalPage) {
		this.toalPage = toalPage;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setItems(ArrayList<E> items) {
		this.items = items;
	}

	public boolean isLoadsEnd() {
		return currentPage == toalPage;

	}

	public boolean isEmptyData() {
		return totalRows == 0 && total == 0;
	}

}