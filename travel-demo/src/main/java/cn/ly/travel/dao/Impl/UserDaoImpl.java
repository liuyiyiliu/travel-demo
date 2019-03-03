package cn.ly.travel.dao.Impl;

import cn.ly.travel.dao.UserDao;
import cn.ly.travel.model.Category;
import cn.ly.travel.model.User;
import cn.ly.travel.util.JDBCUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDaoImpl implements UserDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findUserByPhone(String phone) {

        String sql = "select * from tab_user where telephone = ?";
        User user = null;
        try {
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), phone);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("没有查询到用户信息");
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void save(User user) {

        String sql = "insert into tab_user values(?,?,?,?,?,?,?,?)";
        template.update(sql, user.getUid(), user.getUsername(), user.getPassword(), user.getName(),
                user.getBirthday(), user.getSex(), user.getTelephone(), user.getEmail());
    }


}
