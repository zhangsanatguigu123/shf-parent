package com.atguigu.controller;

import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
    @DubboReference
    HouseImageService houseImageService;

    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId,
                             @PathVariable("type") Integer type, Model model) {
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return "house/upload";
    }


    //multipartFile 封装了文件的信息
    @RequestMapping("/upload/{houseId}/{type}")
    @ResponseBody
    public Result upload(
            @PathVariable("houseId") Long houseId,
            @PathVariable("type") Integer type,
            @RequestParam("file") MultipartFile[] multipartFiles
    ) throws IOException {

        for (MultipartFile multipartFile : multipartFiles) {
      /*      String name = multipartFile.getName();
            String filename = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            long size = multipartFile.getSize();
            System.out.println(filename);
            System.out.println(name);*/
            //1.完成七牛云图片上传
            String filename = multipartFile.getOriginalFilename();
            //1.1 截取后缀名字 *.jpg
            String suffixName = filename.substring(filename.lastIndexOf("."));
            //1.2拼接新的文件名字
            String newFileName = UUID.randomUUID() + "_" + System.currentTimeMillis() + suffixName;

            QiniuUtil.upload2Qiniu(multipartFile.getBytes(), newFileName);
            //2.数据库保存图片信息
            HouseImage houseImage = new HouseImage();
            String url = "http://rk4v93v4p.hd-bkt.clouddn.com/" + newFileName;
            houseImage.setHouseId(houseId);
            houseImage.setType(type);
            houseImage.setImageName(newFileName);
            houseImage.setImageUrl(url);
            houseImageService.insert(houseImage);
        }

        return Result.ok();
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(
            @PathVariable("houseId") Long houseId,
            @PathVariable("id") Long id
    ){
         //查询id对应的图片
        HouseImage houseImage = houseImageService.findById(id);
        //七牛云删除图片
        QiniuUtil.deleteFileFromQiniu(houseImage.getImageName());
        //数据库删除图片信息
       houseImageService.delete(id);
       return "redirect:/house/"+houseId;
    }

}
