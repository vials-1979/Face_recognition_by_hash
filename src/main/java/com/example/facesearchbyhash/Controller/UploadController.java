package com.example.facesearchbyhash.Controller;


import com.example.facesearchbyhash.Entity.test_table;
import com.example.facesearchbyhash.Mapper.ImageMapper;
import com.example.facesearchbyhash.Service.IimageInfoService;
import com.example.facesearchbyhash.Service.Impl.imageHashCodeService;
import com.example.facesearchbyhash.Service.Impl.imageInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
public class UploadController {
    @Autowired
    imageInfoService imageInfo;

    @Autowired
    imageHashCodeService img_0;

    @Value("${file.upload.path}")
    private String path;

    @GetMapping("/")
    public String uploadPage() {
        return "upload";
    }

//    上传图片
    @PostMapping("/upload")
    public String create(@RequestPart MultipartFile file,Model model) throws IOException, IOException {
        String fileName = file.getOriginalFilename();
        String filePath = path + fileName;
        String img="img";
        int i=1;

        File dest = new File(filePath);
        Files.copy(file.getInputStream(), dest.toPath());

        Map<String,String> imgResult=img_0.imageHashCode();


        model.addAttribute("img0",imgResult.get("source"));

//        System.out.println(imgResult.get("source"));
//
//        System.out.println("---------------");

        if(null==imgResult||imgResult.isEmpty()){
            model.addAttribute("flag",1);

        }
        else{
            for (String value : imgResult.values()) {
                System.out.println(value);
                img=img+i;
//                System.out.println(img);
                i++;
                model.addAttribute(img,value);
                img="img";
            }

        }

        return "img";
    }

}
