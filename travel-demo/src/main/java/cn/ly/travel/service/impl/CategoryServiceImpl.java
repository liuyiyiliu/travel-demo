package cn.ly.travel.service.impl;

import cn.ly.travel.BeansFactory;
import cn.ly.travel.dao.Impl.CategoryDaoImpl;
import cn.ly.travel.model.Category;
import cn.ly.travel.service.CategoryService;
import cn.ly.travel.util.JedisUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author ly
 * @date 2019/2/15 13:29
 */
public class CategoryServiceImpl implements CategoryService{
    private CategoryDaoImpl categoryDao = (CategoryDaoImpl) BeansFactory.getBeans("categoryDao");
    @Override
    public String findAllCategorys() {
        long time1 = System.currentTimeMillis();
        //首先从redis里面取,取不到,再从数据库里面找
        Jedis jedis = JedisUtils.getJedis();
        String json = jedis.get("categorys");
        long time2 = System.currentTimeMillis();
        //如果redis中没获取到，那么从数据库获取数据
        if(StringUtils.isEmpty(json)){
            long time3 = System.currentTimeMillis();
            List<Category> categoryList = categoryDao.findAllCategorys();
            json = JSON.toJSONString(categoryList);
            long time4 = System.currentTimeMillis();
            System.out.println("从数据库耗时:"+ (time4-time3));
            jedis.set("categorys",json);

        }else{
            System.out.println("从Redis耗时:"+(time2-time1));
        }
        jedis.close();
        return json;
    }
}
