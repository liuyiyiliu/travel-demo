package cn.ly.travel.service;

import cn.ly.travel.model.PageBean;

/**
 * @author ly
 * @date 2019/2/20 10:15
 */
public interface FavoriteService {
    public String findFavoriteByUidAndRid(int rid, int uid);
    public String insertFavorite(int rid,int uid);
    public PageBean findMyFavoriteRouteByPage(int currentPage,int uid);
}
