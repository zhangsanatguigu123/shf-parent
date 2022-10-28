package com.atguigu.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
//公共的接口
public interface BaseDao<T> {
    //查询所有的角色信息
    List<T> findAll();

    void insert(T t);

    T findById(Serializable id);

    void update(T t);

    void delete(Serializable id);

    List<T> findPage(Map<String, Object> filters);

}
