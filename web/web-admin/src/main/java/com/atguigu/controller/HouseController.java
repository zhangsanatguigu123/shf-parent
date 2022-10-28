package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";

    private static final String PAGE_SUCCESS="common/successPage";
    private static final String PAGE_EDIT="house/edit";
    private static final String PAGE_SHOW="house/show";
    private static final String LIST_ACTION="redirect:/house";
    @DubboReference
    HouseService houseService;
    @DubboReference
    CommuntyService communtyService;
    @DubboReference
    DictService dictService;
    @DubboReference
    HouseImageService houseImageService;
    @DubboReference
    HouseUserService houseUserService;
    @DubboReference
    HouseBrokerService houseBrokerService;
    @RequestMapping
    @PreAuthorize("hasAuthority('house.show')")
    public String index(Model model, HttpServletRequest request){
        Map<String, Object> filters = getFilters(request);
        PageInfo<House> page = houseService.findPage(filters);

        List<Community> communityList = communtyService.findAll();

        List<Dict> houseTypeList = dictService.getAreaListByCode("houseType");
        List<Dict> floorList = dictService.getAreaListByCode("floor");
        List<Dict> buildStructureList = dictService.getAreaListByCode("buildStructure");
        List<Dict> decorationList = dictService.getAreaListByCode("decoration");
        List<Dict> directionList = dictService.getAreaListByCode("direction");
        List<Dict> houseUseList = dictService.getAreaListByCode("houseUse");
        model.addAttribute("filters",filters);
        model.addAttribute("page",page);
        model.addAttribute("communityList",communityList);
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("floorList",floorList);
        model.addAttribute("buildStructureList",buildStructureList);
        model.addAttribute("directionList",directionList);
        model.addAttribute("decorationList",decorationList);
        model.addAttribute("houseUseList",houseUseList);
        return PAGE_INDEX;
    }

    @RequestMapping("/create")
    public String create(Model model){

        List<Community> communityList = communtyService.findAll();

        List<Dict> houseTypeList = dictService.getAreaListByCode("houseType");
        List<Dict> floorList = dictService.getAreaListByCode("floor");
        List<Dict> buildStructureList = dictService.getAreaListByCode("buildStructure");
        List<Dict> decorationList = dictService.getAreaListByCode("decoration");
        List<Dict> directionList = dictService.getAreaListByCode("direction");
        List<Dict> houseUseList = dictService.getAreaListByCode("houseUse");
        model.addAttribute("communityList",communityList);
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("floorList",floorList);
        model.addAttribute("buildStructureList",buildStructureList);
        model.addAttribute("directionList",directionList);
        model.addAttribute("decorationList",decorationList);
        model.addAttribute("houseUseList",houseUseList);
        return PAGE_CREATE;
    }
    @RequestMapping("/save")
    public String save(House house){
        houseService.insert(house);
        return PAGE_SUCCESS;
    }



    @RequestMapping("/edit/{houseId}")
    public String edit(Model model, @PathVariable("houseId") Long houseId){
        House house = houseService.findById(houseId);
        List<Community> communityList = communtyService.findAll();

        List<Dict> houseTypeList = dictService.getAreaListByCode("houseType");
        List<Dict> floorList = dictService.getAreaListByCode("floor");
        List<Dict> buildStructureList = dictService.getAreaListByCode("buildStructure");
        List<Dict> decorationList = dictService.getAreaListByCode("decoration");
        List<Dict> directionList = dictService.getAreaListByCode("direction");
        List<Dict> houseUseList = dictService.getAreaListByCode("houseUse");
        model.addAttribute("communityList",communityList);
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("floorList",floorList);
        model.addAttribute("buildStructureList",buildStructureList);
        model.addAttribute("directionList",directionList);
        model.addAttribute("decorationList",decorationList);
        model.addAttribute("houseUseList",houseUseList);
        model.addAttribute("house",house);
        return PAGE_EDIT;
    }


    @RequestMapping("/update")
    public String update(House house){
        houseService.update(house);
        return PAGE_SUCCESS;
    }



    @RequestMapping("/delete/{houseId}")
    public String delete(Model model, @PathVariable("houseId") Long houseId){
       houseService.delete(houseId);
        return LIST_ACTION;
    }

    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long houseId,@PathVariable("status") Integer status){
        House house=new House();
        house.setId(houseId);
        house.setStatus(status);
        houseService.update(house);
        return LIST_ACTION;
    }

    @RequestMapping("/{id}")
    public String show(@PathVariable("id") Long houseId,Model model){
        //根据房源的id查询房源信息
        House house = houseService.findById(houseId);
        //根据房源的id查询小区的信息
        Community community = communtyService.findById(house.getCommunityId());
        //根据房源的id查询房源/房产图片
        List<HouseImage> houseImageList1= houseImageService.findListByHouseId(houseId,1);
        List<HouseImage> houseImageList2= houseImageService.findListByHouseId(houseId,2);
        //根据房源的id查询经纪人
        List<HouseBroker> houseBrokerList= houseBrokerService.findListByHouseId(houseId);
        //根据房源的id查询房东信息
        List<HouseUser> houseUserList= houseUserService.findListByHouseId(houseId);


        model.addAttribute("house",house);
        model.addAttribute("community",community);
        model.addAttribute("houseImage1List",houseImageList1);
        model.addAttribute("houseImage2List",houseImageList2);
        model.addAttribute("houseBrokerList",houseBrokerList);
        model.addAttribute("houseUserList",houseUserList);

        return PAGE_SHOW;
    }

}
