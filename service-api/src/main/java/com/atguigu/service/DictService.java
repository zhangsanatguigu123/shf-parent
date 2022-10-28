package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {
    List<Map> findZnodes(Long id);

    List<Dict> getAreaListByCode(String beijing);

    List<Dict> findListByParentId(Long areaId);

    String getNameById(Long id);
}
