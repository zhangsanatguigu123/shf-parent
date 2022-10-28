package com.atguigu.service.impl;

import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class DictServiceImpl implements DictService {
     @Autowired
    DictDao dictDao;

     /*
     * [
     *   {
     *     id:
     *     name:
     *     isParent:true
     *   }
     * ]
     *
     * */
    @Override
    public List<Map> findZnodes(Long id) {
          //当前id的子节点
        List<Dict> dictList=  dictDao.findZnodesByParentId(id);


        //封装前端需要的集合
        List<Map> mapList=new ArrayList<>();
        for (Dict dict : dictList) {
            Map map=new HashMap();
             map.put("id",dict.getId());
             map.put("name",dict.getName());
              //查询当前节点是不是父节点(根据当前节点的id查询子节点的个数)
             Integer count=  dictDao.getCountByParentId(dict.getId());
             map.put("isParent",count>0?true:false);
            mapList.add(map);
        }


        return mapList;
    }

    @Override
    public List<Dict> getAreaListByCode(String beijing) {
       Dict dict = dictDao.getAreaListByCode(beijing);
        Long id = dict.getId();
        List<Dict> areaList = dictDao.findZnodesByParentId(id);

        return areaList;
    }

    @Override
    public List<Dict> findListByParentId(Long areaId) {
        List<Dict>  plateList = dictDao.findZnodesByParentId(areaId);
        return plateList;
    }

    @Override
    public String getNameById(Long id) {
        return dictDao.getNameById(id);
    }
}
