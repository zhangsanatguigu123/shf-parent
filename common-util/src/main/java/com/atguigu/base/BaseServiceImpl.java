package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
    public abstract BaseDao<T> getBaseDao();

    @Override
    public List<T> findAll() {
        return getBaseDao().findAll();
    }

    @Override
    public void insert(T t) {
        getBaseDao().insert(t);
    }

    @Override
    public T findById(Serializable id) {
        return getBaseDao().findById(id);
    }

    @Override
    public void update(T t) {
        getBaseDao().update(t);
    }

    @Override
    public void delete(Serializable id) {
        getBaseDao().delete(id);
    }

    @Override
    public PageInfo<T> findPage(Map<String, Object> filters) {
        int pageNum = CastUtil.castInt(filters.get("pageNum"));
        int pageSize = CastUtil.castInt(filters.get("pageSize"));
        PageHelper.startPage(pageNum,pageSize);
        List<T> page = getBaseDao().findPage(filters);
        return new PageInfo<>(page,5);
    }
}
