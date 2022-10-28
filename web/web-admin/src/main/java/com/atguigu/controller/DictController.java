package com.atguigu.controller;

import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {
      @DubboReference
    DictService dictService;
     private static final String PAGE_INDEX="dict/index";
    @RequestMapping
    public String index(){
        return PAGE_INDEX;
    }

    @RequestMapping("/findZnodes")
    @ResponseBody
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0") Long id){
      List<Map> mapList=  dictService.findZnodes(id);
        return Result.ok(mapList);
    }
    @RequestMapping("/findListByParentId/{areaId}")
    @ResponseBody
    public Result findListByParentId(@PathVariable("areaId") Long areaId){
        List<Dict> dictList=  dictService.findListByParentId(areaId);
        return Result.ok(dictList);
    }



}
