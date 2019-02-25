package cn.ly.travel.dao;

import cn.ly.travel.model.Category;
import cn.ly.travel.model.User;

import java.util.List;

public interface UserDao {

    public User findUserByPhone(String phone);
    public void save(User user);

}
