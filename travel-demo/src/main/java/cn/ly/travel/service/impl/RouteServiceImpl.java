package cn.ly.travel.service.impl;

import cn.ly.travel.dao.Impl.RouteDaoImpl;
import cn.ly.travel.dao.RouteDao;
import cn.ly.travel.model.Favorite;
import cn.ly.travel.model.PageBean;
import cn.ly.travel.model.Route;
import cn.ly.travel.model.RouteImg;
import cn.ly.travel.service.RouteService;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ly
 * @date 2019/2/16 11:32
 */
public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();

    @Override
    public String chooseRoute() {
        //1.人气旅游，是每个旅游线路收藏数量的降序获取前4条数据进行显示；
        List<Route> humanRoute = routeDao.findHumanRoute();
        //2.最新旅游，是每个旅游线路上架时间的降序获取前4条数据进行显示；
        List<Route> newRoute = routeDao.findNewRoute();
        //3.主题旅游，是每个旅游线路过滤为主题旅游的获取前4条数据进行显示；
        List<Route> themeRoute = routeDao.findThemeRoute();
        Map<String, List<Route>> map = new HashMap<>();
        map.put("humanRoute", humanRoute);
        map.put("newRoute", newRoute);
        map.put("themeRoute", themeRoute);
        String json = JSON.toJSONString(map);

        return json;
    }

    @Override
    public PageBean findRouteByPage(int cid, int currentPage) {
        PageBean pageBean = new PageBean();
        //当前页码数
        pageBean.setCurrentPage(currentPage);
        //每页显示的数量
        int pageSize = 10;
        pageBean.setPageSize(pageSize);
        //上一页
        pageBean.setPrePage(currentPage - 1);
        //下一页
        pageBean.setNextPage(currentPage + 1);
        //总数量
        int totalCount = routeDao.findRouteCountByid(cid);
        pageBean.setTotalCount(totalCount);
        //总页码数
        int totalPage = (int) Math.ceil(1.0 * totalCount / pageSize);
        pageBean.setTotalPage(totalPage);
        //旅游路线图
        int start = (currentPage - 1) * pageSize;
        List<Route> routeList = routeDao.findRouteByPageAndCid(start, pageSize, cid);
        pageBean.setRouteList(routeList);
        return pageBean;
    }

    @Override
    public PageBean findRouteByidAndname(int cid, String rname, int currentPage) {
        PageBean pageBean = new PageBean();
        //当前页码数
        pageBean.setCurrentPage(currentPage);
        //每页显示的数量
        int pageSize = 5;
        pageBean.setPageSize(pageSize);
        //上一页
        pageBean.setPrePage(currentPage - 1);
        //下一页
        pageBean.setNextPage(currentPage + 1);
        //总数量
        int totalCount = routeDao.findRouteCountByidAndname(cid, rname);
        pageBean.setTotalCount(totalCount);
        //总页码数
        int totalPage = (int) Math.ceil(1.0 * totalCount / pageSize);
        pageBean.setTotalPage(totalPage);
        //旅游路线图
        int start = (currentPage - 1) * pageSize;
        List<Route> routeList = routeDao.findRouteBynameAndCid(start, pageSize, cid, rname);
        pageBean.setRouteList(routeList);

        return pageBean;
    }

    @Override
    public String findRouteDetailsByRid(int rid) {
        //调用dao方法，查询旅游线路和类型，和商家信息
        Route route = routeDao.findRouteDetailsByRid(rid);
        //调用dao方法，查询旅游线路的图片信息
        List<RouteImg> routeImgsList = routeDao.findRouteImgsByRid(rid);
        //将图片信息封装到route对象中
        route.setRouteImgList(routeImgsList);
        //将route转换成json
        String json = JSON.toJSONString(route);
        return json;
    }


    @Override
    public PageBean favoriteRankByPage(int currentPage) {
        PageBean pageBean = new PageBean();
        //当前页
        pageBean.setCurrentPage(currentPage);
        pageBean.setPrePage(currentPage - 1);
        pageBean.setNextPage(currentPage + 1);
        //每页显示数据大小
        int pageSize = 4;
        pageBean.setPageSize(pageSize);

        //查询当前页需要的线路信息
        int start = (currentPage - 1) * pageSize;
        List<Route> myFavoriteRouteList = routeDao.findRouteFavoriteRankByPage(start, pageSize);
        pageBean.setRouteList(myFavoriteRouteList);

        //查询个人收藏旅游线路总数量
        int totalCount = routeDao.findRouteCount();
        pageBean.setTotalCount(totalCount);

        //总页数
        int totalPage = (int) Math.ceil(1.0 * totalCount / pageSize);
        pageBean.setTotalPage(totalPage);

        return pageBean;

    }


    /**
     * 通过路径名称和最低价格和最高价格，以分页的形式展示收藏排名
     *
     * @param currentPage
     * @param routeName
     * @param startPrice
     * @param endPrice
     * @return
     */
    @Override
    public PageBean favoriteRankByPageLike(int currentPage, String routeName, double
            startPrice, double endPrice) {
        PageBean pageBean = new PageBean();
        //当前页
        pageBean.setCurrentPage(currentPage);

        //上一页
        pageBean.setPrePage(currentPage - 1);
        //下一页
        pageBean.setNextPage(currentPage + 1);
        //每页的显示数目
        int pageSize = 4;
        pageBean.setPageSize(pageSize);
        //查询当前页需要的线路信息
        int start = (currentPage - 1) * pageSize;
        List<Route> routeList = routeDao.findRouteFavoriteRankByPageLike(start, pageSize, routeName, startPrice, endPrice);
        pageBean.setRouteList(routeList);

        //查询个人收藏旅游线路总数量
        int totalCount = routeDao.findRouteCountByLike(routeName, startPrice, endPrice);
        pageBean.setTotalCount(totalCount);

        //总页数
        int totalPage = (int) Math.ceil(1.0 * totalCount / pageSize);
        pageBean.setTotalPage(totalPage);

        return pageBean;
    }
}
