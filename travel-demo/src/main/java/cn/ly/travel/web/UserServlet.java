package cn.ly.travel.web;

import cn.ly.travel.BeansFactory;
import cn.ly.travel.model.User;
import cn.ly.travel.service.UserService;
import cn.ly.travel.service.impl.UserServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONToken;
import com.aliyuncs.reader.JsonReader;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@WebServlet(name = "UserServlet",urlPatterns = "/userServlet")
public class UserServlet extends BaseServlet {

    private UserServiceImpl userService = (UserServiceImpl)BeansFactory.getBeans("userService");

    protected void validatephone(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phone = req.getParameter("phone");
        String json = userService.findUserByphone(phone);
        resp.getWriter().write(json);

    }

    protected void sendMessage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取需要发送验证码的手机号
        String phone = req.getParameter("phone");
        //2.生成随机的验证码
        String serverCode = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
             serverCode+=random.nextInt(10);
        }
        //将验证码存放到session中
        HttpSession session = req.getSession();
        session.setAttribute("serverCode",serverCode);

    }

    public void register(HttpServletRequest req,HttpServletResponse res)throws Exception{

        //首先校验验证码
        String userCode = req.getParameter("check");
        String serverCode = (String) req.getSession().getAttribute("serverCode");

        //验证码不通过，返回验证码不正确的错误信息
        if(StringUtils.isNotEmpty(serverCode)&&!serverCode.equals(userCode)){
            Map<String,String > map = new HashMap<>();
            map.put("message","验证码不正确");
            String json = JSON.toJSONString(map);
            res.getWriter().write(json);
            return;
        }
        //1.获取请求的参数
        Map<String,String[]> map = req.getParameterMap();

        //获取页面的sex值,解决获取前端页面中文乱码问题
        // String sex = req.getParameter("sex");
        // sex = new String(sex.getBytes("ISO-8859-1"),"utf-8");

        //2.从页面获取的参数封装Bean中
        User user = new User();
        BeanUtils.populate(user,map);

        String json = userService.register(user);
        res.getWriter().write(json);


    }

    //登录网站
    public void login(HttpServletRequest req,HttpServletResponse res)throws Exception{
        //1.获取用户名和密码
        String phone = req.getParameter("username");
        String password = req.getParameter("password");
        //2.调用service完成登录的逻辑
        String json = userService.login(phone, password, req);
        //3.给出响应
        res.getWriter().write(json);

    }

    //获取当前用户状态
    public void getCurrentUser(HttpServletRequest req,HttpServletResponse res)throws Exception{
        //获取session中的用户信息
        User user = (User) req.getSession().getAttribute("user");
        //创建Map存储返回信息
        Map<String,String> map = new HashMap<String,String>();
        String json = "";
        if(user==null){
            //用户未登录
            map.put("message","no");
        }else{
            map.put("message",user.getUsername());
        }
        //响应给浏览器
        json = JSON.toJSONString(map);
        res.getWriter().write(json);
    }

    //退出
    public void loginOut(HttpServletRequest req,HttpServletResponse res)throws Exception{
        req.getSession().invalidate();
        User user = (User) req.getSession().getAttribute("user");
        //创建Map存储返回信息
        Map<String,String> map = new HashMap<String,String>();
        String json = "";
        if(user==null){
            map.put("message","no");
        }else{
            map.put("message",user.getUsername());
        }
        //响应给浏览器
        json = JSON.toJSONString(map);
        res.getWriter().write(json);
    }
}
