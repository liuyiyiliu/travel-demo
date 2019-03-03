package cn.ly.travel.web;

import cn.ly.travel.BeansFactory;
import cn.ly.travel.service.impl.CategoryServiceImpl;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ly
 * @date 2019/2/15 13:17
 */
@WebServlet(name = "CategoryServlet", urlPatterns = "/categoryServlet")
public class CategoryServlet extends BaseServlet {

    private CategoryServiceImpl categoryService = (CategoryServiceImpl) BeansFactory
            .getBeans("categoryService");

    public void findAllCategorys(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //直接调用service获取所有的类型信息
        String json = categoryService.findAllCategorys();

        res.getWriter().write(json);


    }
}
