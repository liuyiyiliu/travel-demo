package cn.ly.travel.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(name = "BaseServlet")
public class BaseServlet extends HttpServlet {
    /**
     * 1.如何按照功能模块划分，提高servlet类的复用性？
     * 解决：根据不同的参数，调用不同的功能方法。
     * 2.如果全部使用if条件判断，太麻烦。如何提高查找方法的效率。
     * 解决：反射机制
     * 3.每个servlet都需要写这样反射机制，代码冗余。
     * 解决：抽取父类 BaseServlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的methodName参数
        String methodName = req.getParameter("methodName");

      /*  if("login".equals(methodName)){
            login(request,response);
        }else if("register".equals(methodName)){
            register(request,response);
        }*/

        try {
            Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            if (method != null) {
                //执行找到的方法
                method.invoke(this, req, resp);
            } else {
                //找不到方法，重定向到首页
                resp.sendRedirect(req.getContextPath() + "/index.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*//登录方法    http://localhost:8080/web13_estore/CheckPhoneServlet?methodName=login
    public void login(HttpServletRequest request,
            HttpServletResponse response){

    }
    //注册的方法    http://localhost:8080/web13_estore/CheckPhoneServlet?methodName=register
    public void register(HttpServletRequest request,
            HttpServletResponse response){

    }*/

        /*//登录方法    http://localhost:8080/web13_estore/CheckPhoneServlet?methodName=login
    public void login(HttpServletRequest request,
            HttpServletResponse response){

    }
    //注册的方法    http://localhost:8080/web13_estore/CheckPhoneServlet?methodName=register
    public void register(HttpServletRequest request,
            HttpServletResponse response){

    }*/

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
