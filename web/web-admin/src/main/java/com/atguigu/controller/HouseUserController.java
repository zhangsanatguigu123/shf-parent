package com.atguigu.controller;

import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController {
      @DubboReference
    HouseUserService houseUserService;
  private static final String PAGE_CREATE="houseUser/create";
  private static final String PAGE_EDIT="houseUser/edit";
  private static final String PAGE_SUCCESS="common/successPage";
    @RequestMapping("/create/{houseId}")
    public String create(@PathVariable("houseId") Long houseId, Model model){
         model.addAttribute("houseId",houseId);
         return PAGE_CREATE;
    }

    @RequestMapping("/save")
    public String save(HouseUser houseUser, Model model){
        houseUserService.insert(houseUser);
        return PAGE_SUCCESS;
    }



    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        HouseUser houseUser = houseUserService.findById(id);
        model.addAttribute("houseUser",houseUser);
        return PAGE_EDIT;
    }


    @RequestMapping("/update")
    public String update(HouseUser houseUser){
        houseUserService.update(houseUser);
        return PAGE_SUCCESS;
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("id") Long id,@PathVariable("houseId") Long houseId){
         houseUserService.delete(id);
        return "redirect:/house/"+houseId;
    }

}
