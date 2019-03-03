package cn.ly.travel.service;

import cn.ly.travel.model.Favorite;
import cn.ly.travel.model.PageBean;

/**
 * @author ly
 * @date 2019/2/16 11:30
 */

public interface RouteService {

    public String chooseRoute();

    public PageBean findRouteByPage(int cid, int currentPage);

    public PageBean findRouteByidAndname(int cid, String rname, int currentPage);

    public String findRouteDetailsByRid(int rid);

    public PageBean favoriteRankByPage(int currentPage);

    public PageBean favoriteRankByPageLike(int currentPage, String routeName, double
            startPrice, double endPrice);
}
