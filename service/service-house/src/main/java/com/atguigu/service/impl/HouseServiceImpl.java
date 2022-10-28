package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@DubboService
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
     @Autowired
    HouseDao houseDao;
     @Autowired
    DictDao dictDao;
    @Override
    public BaseDao<House> getBaseDao() {
        return houseDao;
    }

    @Override
    public House findById(Serializable id) {
        House house = houseDao.findById(id);
        //到字典表查询的条件（id）
        Long buildStructureId = house.getBuildStructureId();
        Long decorationId = house.getDecorationId();
        Long directionId = house.getDirectionId();
        Long floorId = house.getFloorId();
        Long houseTypeId = house.getHouseTypeId();
        Long houseUseId = house.getHouseUseId(); 
        
        //根据id查询的内容 是name
        String buildStructureName = dictDao.getNameById(buildStructureId);
        String decorationName = dictDao.getNameById(decorationId);
        String directionName = dictDao.getNameById(directionId);
        String floorName = dictDao.getNameById(floorId);
        String houseTypeName = dictDao.getNameById(houseTypeId);
        String houseUseName = dictDao.getNameById(houseUseId);

        //给所有对应字典的name复制
        house.setHouseUseName(houseUseName);
        house.setDirectionName(directionName);
        house.setDecorationName(decorationName);
        house.setHouseTypeName(houseTypeName);
        house.setFloorName(floorName);
        house.setBuildStructureName(buildStructureName);

        return house;
    }

    @Override
    public PageInfo<HouseVo> findListByPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        PageHelper.startPage(pageNum,pageSize);
        List<HouseVo> houseVoList= houseDao.findListByPage(houseQueryVo);

        for (HouseVo houseVo : houseVoList) {
            houseVo.setHouseTypeName(dictDao.getNameById(houseVo.getHouseTypeId()));
            houseVo.setFloorName(dictDao.getNameById(houseVo.getFloorId()));
            houseVo.setDirectionName(dictDao.getNameById(houseVo.getDirectionId()));
        }

        return new PageInfo<>(houseVoList,5);
    }
}
