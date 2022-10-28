package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.CommuntyDao;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Community;
import com.atguigu.service.CommuntyService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@DubboService
public class CommuntyServiceImpl extends BaseServiceImpl<Community> implements CommuntyService {
    @Autowired
    CommuntyDao communtyDao;
    @Autowired
    DictDao dictDao;
    @Override
    public BaseDao<Community> getBaseDao() {
        return communtyDao;
    }

    @Override
    public PageInfo<Community> findPage(Map<String, Object> filters) {
        int pageNum = CastUtil.castInt(filters.get("pageNum"));
        int pageSize = CastUtil.castInt(filters.get("pageSize"));
        PageHelper.startPage(pageNum,pageSize);
        List<Community> page = getBaseDao().findPage(filters);
        for (Community community : page) {
            Long areaId = community.getAreaId();
            Long plateId = community.getPlateId();
            String areaName= dictDao.getNameById(areaId);
            String plateName= dictDao.getNameById(plateId);
            community.setAreaName(areaName);
            community.setPlateName(plateName);
        }

        return new PageInfo<>(page,5);
    }

    @Override
    public Community findById(Serializable id) {
        Community community = communtyDao.findById(id);
        String areaName = dictDao.getNameById(community.getAreaId());
        String plateName = dictDao.getNameById(community.getPlateId());
         community.setAreaName(areaName);
         community.setPlateName(plateName);
         return  community;
    }
}
