package cn.ly.travel.service.impl;

import cn.ly.travel.dao.FavoriteDao;
import cn.ly.travel.dao.Impl.FavoriteDaoImpl;
import cn.ly.travel.dao.Impl.RouteDaoImpl;
import cn.ly.travel.dao.RouteDao;
import cn.ly.travel.model.Favorite;
import cn.ly.travel.model.PageBean;
import cn.ly.travel.model.Route;
import cn.ly.travel.model.User;
import cn.ly.travel.service.FavoriteService;
import cn.ly.travel.util.JDBCUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ly
 * @date 2019/2/20 10:16
 */
public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private RouteDao routeDao = new RouteDaoImpl();

    @Override
    public String findFavoriteByUidAndRid(int rid, int uid) {
        Favorite favorite = favoriteDao.findFavoriteByUidAndRid(rid, uid);
        //声明一个Map保存数据
        Map<String, String> map = new HashMap<>();
        if (favorite == null) {
            //未收藏
            map.put("isShow", "yes");
        } else {
            map.put("isShow", "no");
        }
        String json = JSON.toJSONString(map);
        return json;
    }

    @Override
    public String insertFavorite(int rid, int uid) {
        //获取jdbcTemplete对象
        // JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

        //启动事务管理器(实现连接对象和当前线程的绑定)
        TransactionSynchronizationManager.initSynchronization();
        //使用工具类获取与当前想成绑定的连接对象
        Connection connection = DataSourceUtils.getConnection(JDBCUtils.getDataSource());
        Map<String, String> map = new HashMap<>();
        //开启事物
        try {
            connection.setAutoCommit(false);
            //1.更新收藏的数量
            routeDao.updateRouteCount(rid);
            //插入收藏记录
            Favorite favorite = new Favorite();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = dateFormat.format(new Date());
            favorite.setDate(date);
            Route route = new Route();
            route.setRid(rid);
            favorite.setRoute(route);
            User user = new User();
            user.setUid(uid);
            favorite.setUser(user);

            //调用favoriteDao
            favoriteDao.insertFavorite(favorite);
            //提交事务
            connection.commit();

        } catch (SQLException e) {
            //事物回滚
            try {
                connection.rollback();

                //给出响应,表示服务器异常
                map.put("message", "no");
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            //释放资源
            try {
                //重新开启自动提交,再放回连接池
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //清空当前连接和线程的绑定,并且放回连接池
            TransactionSynchronizationManager.clearSynchronization();
        }
        //3.调用dao再次查询当前旅游线路信息的收藏数量
        Route route = routeDao.findRouteDetailsByRid(rid);
        map.put("message", route.getCount() + "");
        String json = JSON.toJSONString(map);

        return json;
    }

    @Override
    public PageBean findMyFavoriteRouteByPage(int currentPage, int uid) {
        PageBean pageBean = new PageBean();
        //当前页
        pageBean.setCurrentPage(currentPage);
        pageBean.setPrePage(currentPage - 1);
        pageBean.setNextPage(currentPage + 1);
        //每页显示数据大小
        int pageSize = 2;
        pageBean.setPageSize(pageSize);

        //查询当前页需要的线路信息
        int start = (currentPage - 1) * pageSize;
        List<Route> myFavoriteRouteList = favoriteDao.findMyFavoriteRouteByPage(uid, start, pageSize);
        pageBean.setRouteList(myFavoriteRouteList);

        //查询个人收藏旅游线路总数量
        int totalCount = favoriteDao.findMyFavoriteRouteCount(uid);
        pageBean.setTotalCount(totalCount);

        //总页数
        int totalPage = (int) Math.ceil(1.0 * totalCount / pageSize);
        pageBean.setTotalPage(totalPage);
        return pageBean;
    }
}
