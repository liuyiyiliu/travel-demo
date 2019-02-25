package cn.ly.travel.web;

import cn.ly.travel.model.Favorite;
import cn.ly.travel.model.PageBean;
import cn.ly.travel.model.User;
import cn.ly.travel.service.FavoriteService;
import cn.ly.travel.service.RouteService;
import cn.ly.travel.service.impl.FavoriteServiceImpl;
import cn.ly.travel.service.impl.RouteServiceImpl;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ly
 * @date 2019/2/16 11:07
 */
@WebServlet(name = "RouteServlet",urlPatterns = "/routeServlet")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    public void chooseRoute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //直接调用service获取所有的类型信息
        String json = routeService.chooseRoute();

        res.getWriter().write(json);


    }
    public void findRouteByPage(HttpServletRequest request,HttpServletResponse response) throws IOException{

        //1.获取当前页码数和指定的分类信息
        int currentPage = 1;
        String currentPageStr = request.getParameter("currentPage");
        if(!StringUtils.isEmpty(currentPageStr)){
            currentPage = Integer.parseInt(currentPageStr);
        }
        int cid = Integer.parseInt(request.getParameter("cid"));
        PageBean pageBean = routeService.findRouteByPage(cid, currentPage);
        String json = JSON.toJSONString(pageBean);
        response.getWriter().write(json);

    }

    public void searchRoute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //1.获取当前页码数和指定的分类信息
        int currentPage = 1;
        String rname = req.getParameter("rname");
        int cid = 5;
        PageBean pageBean = routeService.findRouteByidAndname(cid, rname, currentPage);
        String json = JSON.toJSONString(pageBean);
        res.getWriter().write(json);
    }

    public void findRouteDetailByRid(HttpServletRequest req, HttpServletResponse res) throws IOException {

        //获取页面的rid
        int rid = 0;
        String ridStr = req.getParameter("rid");
        if(!StringUtils.isEmpty(ridStr)){
            rid = Integer.parseInt(ridStr);
        }
        String json = routeService.findRouteDetailsByRid(rid);
        res.getWriter().write(json);
    }

    //判断是否显示收藏按钮
    public void isShowFavorite(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //首先判断用户是否登录
        User user = (User) req.getSession().getAttribute("user");
        String json = "";
        if(user==null){
            //如果未登录，isShow显示为no,不显示收藏按钮
            Map<String,String> map = new HashMap<>();
            map.put("isShow","no");
            json = JSON.toJSONString(map);
        }else{
            //如果已经登录，那么判断当前是否已经被收藏。
            //获取rid和uid
            int rid = Integer.parseInt(req.getParameter("rid"));
            json  = favoriteService.findFavoriteByUidAndRid(rid, user.getUid());

        }
        //响应给浏览器
        res.getWriter().write(json);

    }

    //添加收藏
    public void addFavorite(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //首先判断是否登录
        User user = (User) req.getSession().getAttribute("user");
        String json = "";
        if(user == null){
            //未登录
            Map<String,String> map = new HashMap<>();
            map.put("message","noLogin");
            json = JSON.toJSONString(map);
        }else {
            //已经登录
            //获取需要收藏的旅游线路的id
            int rid = Integer.parseInt(req.getParameter("rid"));

            //调用service
            json = favoriteService.insertFavorite(rid, user.getUid());
        }
        res.getWriter().write(json);
    }

    public void findMyFavoriteRouteByPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //首先判断是否登录 未登录 先登录
        User user = (User)req.getSession().getAttribute("user");
        String json = "";
        if(user==null){
            //未登录
            Map<String,String> map = new HashMap<>();
            map.put("isLogin","no");
            json = JSON.toJSONString(map);
            res.getWriter().write(json);
            return;
        }
        //如果已经登录，调用service查询分页信息
        //获取需要查询指定的页码数的数据
        int currentPage = 1;
        String currentPageStr = req.getParameter("currentPage");
        if(!StringUtils.isEmpty(currentPageStr)){
            currentPage = Integer.parseInt(currentPageStr);
        }
        //调用service完成分页数据的查询
        PageBean pageBean = favoriteService.findMyFavoriteRouteByPage(currentPage, user.getUid());
        json = JSON.toJSONString(pageBean);
        res.getWriter().write(json);

    }
    public void favoriteRankByPage(HttpServletRequest request,HttpServletResponse response) throws IOException {
        int currentpPage = 1;
        int currentPageStr = Integer.parseInt(request.getParameter("currentPage"));
        if(!StringUtils.isEmpty(currentPageStr)){
            currentpPage = currentPageStr;
        }

        PageBean pageBean = routeService.favoriteRankByPage(currentpPage);
        String json = JSON.toJSONString(pageBean);
        response.getWriter().write(json);
    }

    /**
     * 通过路径名称和最低价格和最高价格，以分页的形式展示收藏排名
     * @param request
     * @param response
     * @throws IOException
     */
    public void favoriteRankByPageByLike(HttpServletRequest request,HttpServletResponse response) throws IOException {
        int currentPage = 1;
        int currentPageStr = Integer.parseInt(request.getParameter("currentPage"));
        if(!StringUtils.isEmpty(currentPageStr)){
            currentPage = currentPageStr;
        }
        //获取搜索的条件
        String routeName = request.getParameter("routeName");

        double startPrice = 0;
        String startPriceStr = request.getParameter("startPrice");
        if(!StringUtils.isEmpty(startPriceStr)){
            startPrice = Double.parseDouble(startPriceStr);

        }

        double endPrice = 0;
        String endPriceStr = request.getParameter("endPrice");
        if(!StringUtils.isEmpty(endPriceStr)){
            endPrice = Double.parseDouble(endPriceStr);

        }
        PageBean pageBean = routeService.favoriteRankByPageLike(currentPage, routeName, startPrice, endPrice);
        String json = JSON.toJSONString(pageBean);
        response.getWriter().write(json);

    }

}
