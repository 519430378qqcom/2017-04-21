package cn.youmi.framework.http.parsers;

import java.util.ArrayList;

public class ListResult<E> {
    private int currentPage;
    private int toalPage;
    private int rows;
    private int row;
    private ArrayList<E> items;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getToalPage() {
        return toalPage;
    }

    public int getRows() {
        return rows;
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

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setItems(ArrayList<E> items) {
        this.items = items;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isLoadsEnd() {
        return currentPage == toalPage;

    }

    public boolean isEmptyData() {
        return rows == 0 && rows == 0;
    }

}