package com.atguigu.base;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T> {
    //查询所有的角色信息
    List<T> findAll();

    void insert(T t);

    T findById(Serializable id);

    void update(T t);

    void delete(Serializable id);

    PageInfo<T> findPage(Map<String, Object> filters);
}
