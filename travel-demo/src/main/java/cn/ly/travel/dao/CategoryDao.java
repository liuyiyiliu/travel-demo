package cn.ly.travel.dao;

import cn.ly.travel.model.Category;

import java.util.List;

/**
 * @author ly
 * @date 2019/2/15 13:26
 */
public interface CategoryDao {
    public List<Category> findAllCategorys();
}
