package cn.ly.travel.service.impl;

import cn.ly.travel.BeansFactory;
import cn.ly.travel.dao.Impl.UserDaoImpl;
import cn.ly.travel.dao.UserDao;
import cn.ly.travel.model.User;
import cn.ly.travel.service.UserService;
import cn.ly.travel.util.Md5Util;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDaoImpl userDao = (UserDaoImpl) BeansFactory.getBeans("userDao");

    @Override
    public String findUserByphone(String phone) {

        String json = "";
        User user = null;

        //调用dao层的方法，根据手机号去数据库查询用户信息
        user = userDao.findUserByPhone(phone);
        Map<String, String> map = new HashMap<String, String>();
        if (user == null) {
            map.put("isExist", "no");
        } else {
            map.put("isExist", "yes");
        }

        //将map转换为json
        json = JSON.toJSONString(map);
        return json;
    }

    @Override
    public String register(User user) {
        //声明一个map集合，保存返回值信息
        Map<String, String> map = new HashMap<String, String>();
        String json = "";
        //验证手机号不能为空

        if (StringUtils.isEmpty(user.getTelephone())) {
            map.put("message", "手机号码为空");
            json = JSON.toJSONString(map);
            return json;

        }
        if (StringUtils.isEmpty(user.getPassword())) {
            map.put("message", "密码为空");
            json = JSON.toJSONString(map);
            return json;
        }
        //验证手机号没有被注册过
        User userByPhone = userDao.findUserByPhone(user.getTelephone());
        if (userByPhone != null) {
            map.put("message", "手机号码已被注册");
            json = JSON.toJSONString(map);
            return json;

        }

        //校验成功，添加用户信息到数据库

        try {
            //用户密码使用md5加密
            user.setPassword(Md5Util.encodeByMd5(user.getPassword()));
            userDao.save(user);
            map.put("message", "yes");
            json = JSON.toJSONString(map);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public String login(String phone, String password, HttpServletRequest request) {
        try {
            //md5加密
            Md5Util.encodeByMd5(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //声明Map集合保存user信息
        Map<String, String> map = new HashMap<String, String>();
        String json = "";
        //调用dao方法根据手机号去数据库查询用户信息
        User user = userDao.findUserByPhone(phone);
        //将用户的信息存放到session中
        request.getSession().setAttribute("user", user);
        if (user == null) {
            map.put("message", "手机号不存在");
            json = JSON.toJSONString(map);
            return json;
        }
        if (user.getPassword().equals(password)) {
            map.put("message", "密码不正确");
            json = JSON.toJSONString(map);
            return json;
        }
        map.put("message", "yes");
        json = JSON.toJSONString(map);
        return json;
    }
}
