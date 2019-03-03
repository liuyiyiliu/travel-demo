package cn.ly.travel.dao.Impl;

import cn.ly.travel.dao.FavoriteDao;
import cn.ly.travel.model.Favorite;
import cn.ly.travel.model.Route;
import cn.ly.travel.util.JDBCUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author ly
 * @date 2019/2/20 10:13
 */
public class FavoriteDaoImpl implements FavoriteDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findFavoriteByUidAndRid(int rid, int uid) {
        String sql = "select * from tab_favorite where rid = ? and uid = ?";
        Favorite favorite = null;
        try {
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>
                    (Favorite.class), rid, uid);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }

        return favorite;
    }

    @Override
    public void insertFavorite(Favorite favorite) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql, favorite.getRoute().getRid(), favorite.getDate(), favorite.getUser().getUid());
    }

    @Override
    public List<Route> findMyFavoriteRouteByPage(int uid, int start, int length) {
        String sql = "select * from tab_route r,tab_favorite f where r.rid = f.rid and f.uid = ? limit ?,?";
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), uid, start, length);
        return routeList;
    }

    @Override
    public int findMyFavoriteRouteCount(int uid) {
        String sql = "select count(*) from tab_favorite where uid = ?";
        Integer integer = template.queryForObject(sql, Integer.class, uid);
        return integer;
    }
}
