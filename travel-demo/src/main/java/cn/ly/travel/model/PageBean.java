package cn.ly.travel.model;

import java.util.List;

/**
 * @author ly
 * @date 2019/2/18 8:45
 */
public class PageBean {
    /**
     * 分页代码
     */
    private int currentPage;
    private int prePage;
    private int nextPage;
    private int pageSize;
    private int totalPage;
    private int totalCount;
    private List<Route> routeList;

    @Override
    public String toString() {
        return "PageBean{" +
                "currentPage=" + currentPage +
                ", prePage=" + prePage +
                ", nextPage=" + nextPage +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", totalCount=" + totalCount +
                ", routeList=" + routeList +
                '}';
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public PageBean() {
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}
