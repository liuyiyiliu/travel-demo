package cn.ly.travel.dao.Impl;

import cn.ly.travel.dao.RouteDao;
import cn.ly.travel.model.*;
import cn.ly.travel.util.JDBCUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ly
 * @date 2019/2/16 11:19
 */
public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public List<Route> findHumanRoute() {
        String sql = "select * from tab_route order by count DESC limit 0,4";
        List<Route> query = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        return query;
    }


    @Override
    public List<Route> findNewRoute() {
        String sql = "select * from tab_route order by rdate desc limit 0,4";
        List<Route> query = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        return query;
    }

    @Override
    public List<Route> findThemeRoute() {
        String sql = "select * from tab_route where isThemeTour = 1 limit 0,4";
        List<Route> query = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        return query;
    }


    @Override
    public int findRouteCountByid(int cid) {
        int totalCount = 0;
        String sql = "select count(*) from tab_route where cid = ?";
        totalCount = template.queryForObject(sql, Integer.class, cid);
        return totalCount;
    }

    @Override
    public List<Route> findRouteByPageAndCid(int start, int length, int cid) {
        List<Route> routeList = null;
        String sql = "select * from tab_route where cid = ? limit ?,?";
        routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class),cid,start,length);
        return routeList;
    }

    @Override
    public int findRouteCountByidAndname(int cid, String rname) {
        System.out.println("cid:"+cid+";"+"rname:"+rname);
        int totalCount = 0;
        String sql = "select count(*) from tab_route where  cid = ? and rname like ? ";
         totalCount = template.queryForObject(sql, Integer.class, cid, "%"+rname+"%");
        return totalCount;
    }

    @Override
    public List<Route> findRouteBynameAndCid(int start, int length, int cid, String rname) {
        List<Route> routeList = null;
        String sql = "select * from tab_route where cid = ? and rname like ? limit ?,? ";
        routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), cid, "%"+rname+"%",start, length);
        return routeList;
    }

    @Override
    public Route findRouteDetailsByRid(int rid) {
        String sql = "select * from tab_route r,tab_seller s,tab_category c where r.sid=s.sid and "
                + "r.cid = c.cid and rid = ?";
        Map<String,Object> map = template.queryForMap(sql,rid);
        /**
         * 一条数据封装成一个Map
         *  Map.key --> 列名
         *  Map.value -->值
         */
        Route route = new Route();
        Category category = new Category();
        Seller seller = new Seller();
        //封装map集合数据到javaBean
        try {
            BeanUtils.populate(route,map);
            BeanUtils.populate(category,map);
            BeanUtils.populate(seller,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //将商家信息和类型信息封装到 route对象中
        route.setCategory(category);
        route.setSeller(seller);
        return route;
    }

    @Override
    public List<RouteImg> findRouteImgsByRid(int rid) {
        String sql = "select * from tab_route_img where rid = ?";
        List<RouteImg> images = template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
        return images;
    }

    @Override
    public void updateRouteCount(int rid) {
        String sql = "update tab_route set count = count + 1 where rid = ?";
        template.update(sql, rid);
    }

    @Override
    public int findRouteCount() {
        String sql = "select count(*) from tab_route";
        Integer integer = template.queryForObject(sql, Integer.class);
        return integer;
    }

    @Override
    public List<Route> findRouteFavoriteRankByPage(int start, int length) {
        String sql = "select * from tab_route order by count desc limit ?,?";
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), start, length);
        return routeList;
    }

    @Override
    public int findRouteCountByLike(String routeName, double startPrice, double endPrice) {
        String sql = "select count(*) from tab_route where 1=1";
        List<Object> paramsList = new ArrayList<>();
        if(!StringUtils.isEmpty(routeName)){
            //如果名称不为空,sql语句加上名称条件
            sql+=" and rname like ?";
            paramsList.add("%"+routeName+"%");
        }
        //如果起始价格不为空,加上起始价格
        if(startPrice>0){
            sql+=" and price>? ";
            paramsList.add(startPrice);
        }
        //如果最大价格不为空,那么加上最大价格的条件
        if(endPrice>0){
            sql+= " and price<?";
            paramsList.add(endPrice);
        }
        System.out.println("查询数量的sql:"+sql);
        Object[] params = paramsList.toArray();
        Integer count = template.queryForObject(sql, Integer.class, params);

        return count;
    }

    @Override
    public List<Route> findRouteFavoriteRankByPageLike(int start, int length, String routeName, double
            startPrice, double endPrice) {
        String sql = "select * from tab_route where 1=1";
        List<Object> paramsList = new ArrayList<>();
        if(!StringUtils.isEmpty(routeName)){
            sql+=" and rname like ?";
            paramsList.add("%"+routeName+"%");
        }
        if(startPrice>0){
            sql+=" and price>?";
            paramsList.add(startPrice);
        }
        if(endPrice>0){
            sql+=" and price<?";
            paramsList.add(endPrice);
        }
        paramsList.add(start);
        paramsList.add(length);
        sql+=" order by count desc limit ?,?";
        System.out.println("查询数量的sql:"+sql);
        Object[] params = paramsList.toArray();
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params);
        return routeList;
    }
}
