package cn.ly.travel.dao.Impl;

import cn.ly.travel.dao.CategoryDao;
import cn.ly.travel.model.Category;
import cn.ly.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author ly
 * @date 2019/2/15 13:27
 */
public class CategoryDaoImpl implements CategoryDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Category> findAllCategorys() {
        String sql = "select * from tab_category order by cid";
        List<Category> categories = template.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
        return categories;
    }
}
