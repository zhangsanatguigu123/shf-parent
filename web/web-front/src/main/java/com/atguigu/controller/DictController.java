package com.atguigu.controller;

import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController //@Controller + responseBody
@RequestMapping("/dict")
public class DictController {
      @DubboReference
    DictService dictService;


      //查询子节点包装为zTree
    @RequestMapping("/findZnodes")
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0") Long id){
      List<Map> mapList=  dictService.findZnodes(id);
        return Result.ok(mapList);
    }
    //查询子节点的列表
    @RequestMapping("/findListByParentId/{areaId}")
    public Result findListByParentId(@PathVariable("areaId") Long areaId){
        List<Dict> dictList=  dictService.findListByParentId(areaId);
        return Result.ok(dictList);
    }



    //根据code查询所有子节点列表信息
    @RequestMapping("/findListByDictCode/{code}")
    public Result findListByDictCode(@PathVariable("code") String code){
        List<Dict> dictList=  dictService.getAreaListByCode(code);
        return Result.ok(dictList);
    }



}
