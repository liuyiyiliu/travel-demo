package cn.ly.travel.service;

import cn.ly.travel.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    public String findUserByphone(String phone);
    public String register(User user);
    public String login(String phone, String password,HttpServletRequest request);
}
