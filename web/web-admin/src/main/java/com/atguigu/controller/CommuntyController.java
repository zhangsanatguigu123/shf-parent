package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommuntyService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommuntyController extends BaseController {
    @DubboReference
    CommuntyService communtyService;
    @DubboReference
    DictService dictService;
    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";

     private static final String PAGE_SUCCESS="common/successPage";
     private static final String PAGE_EDIT="community/edit";
     private static final String LIST_ACTION="redirect:/community";
    @RequestMapping
    public String index(Model model, HttpServletRequest request) {
        Map<String, Object> filters = getFilters(request);
        PageInfo<Community> page = communtyService.findPage(filters);
        List<Dict> areaList = dictService.getAreaListByCode("beijing");
        model.addAttribute("filters", filters);
        model.addAttribute("page", page);
        model.addAttribute("areaList", areaList);
        //model.addAttribute("plateList",null);
        return PAGE_INDEX;
    }

    //跳转到create页面
    @RequestMapping("/create")
    public String create(Model model) {
        List<Dict> areaList = dictService.getAreaListByCode("beijing");
        model.addAttribute("areaList",areaList);
        return PAGE_CREATE;
    }
    @RequestMapping("/save")
    public String save(Community community){
      communtyService.insert(community);
      return PAGE_SUCCESS;
    }




    //跳转到edit页面
    @RequestMapping("/edit/{communityId}")
    public String edit(Model model, @PathVariable("communityId") Long communityId) {
        List<Dict> areaList = dictService.getAreaListByCode("beijing");
        Community community = communtyService.findById(communityId);
        model.addAttribute("areaList",areaList);
        model.addAttribute("community",community);
        return PAGE_EDIT;
    }
    @RequestMapping("/update")
    public String update(Community community){
        communtyService.update(community);
        return PAGE_SUCCESS;
    }



    @RequestMapping("/delete/{communityId}")
    public String delete(Model model, @PathVariable("communityId") Long communityId) {
       communtyService.delete(communityId);
        return LIST_ACTION;
    }
}
