package cn.ly.travel.dao;

import cn.ly.travel.model.Favorite;
import cn.ly.travel.model.Route;

import java.util.List;

/**
 * @author ly
 * @date 2019/2/20 10:13
 */
public interface FavoriteDao {
    public Favorite findFavoriteByUidAndRid(int rid, int uid);
    public void insertFavorite(Favorite favorite);
    public List<Route> findMyFavoriteRouteByPage(int uid,int start,int length);
    public int findMyFavoriteRouteCount(int uid);
}
