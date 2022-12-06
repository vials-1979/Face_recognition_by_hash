package com.example.facesearchbyhash.Service.Impl;

import com.example.facesearchbyhash.Entity.test_table;
import com.example.facesearchbyhash.Mapper.ImageMapper;
import com.example.facesearchbyhash.Service.IimageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class imageInfoService implements IimageInfoService {
    @Autowired
    private  ImageMapper imgMapper;
   public List<test_table> ImageInfo(){
       List<test_table> userList = imgMapper.selectList(null);
       return userList;

    }
}
