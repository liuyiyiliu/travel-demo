package cn.ly.travel.dao;

import cn.ly.travel.model.Favorite;
import cn.ly.travel.model.Route;
import cn.ly.travel.model.RouteImg;

import java.util.List;

/**
 * @author ly
 * @date 2019/2/16 11:17
 */
public interface RouteDao {
    public List<Route> findHumanRoute();
    public List<Route> findNewRoute();
    public List<Route> findThemeRoute();

    public int findRouteCountByid(int cid);

    public List<Route> findRouteByPageAndCid(int start,int length,int cid);

    public int findRouteCountByidAndname(int cid,String rname);

    public List<Route> findRouteBynameAndCid(int start,int length,int cid,String rname);

    public Route findRouteDetailsByRid(int rid);

    public List<RouteImg> findRouteImgsByRid(int rid);

    public void updateRouteCount(int rid);

    public int findRouteCount();

    public List<Route> findRouteFavoriteRankByPage(int start,int length);

    public int findRouteCountByLike(String routeName,double startPrice,double endPrice);

    public List<Route> findRouteFavoriteRankByPageLike(int start,int length,String routeName,
                                                       double startPrice,double endPrice);
}
