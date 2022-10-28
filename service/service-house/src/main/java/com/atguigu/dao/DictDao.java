package com.atguigu.dao;

import com.atguigu.entity.Dict;

import java.util.List;

public interface DictDao {
    List<Dict> findZnodesByParentId(Long id);

    Integer getCountByParentId(Long id);

    Dict getAreaListByCode(String beijing);

    String getNameById(Long areaId);
}
