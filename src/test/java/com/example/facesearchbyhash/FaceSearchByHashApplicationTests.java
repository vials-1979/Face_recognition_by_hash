package com.example.facesearchbyhash;

import com.example.facesearchbyhash.Entity.test_table;
import com.example.facesearchbyhash.Mapper.ImageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FaceSearchByHashApplicationTests {
    @Autowired  //需要配置SpringBoot包扫描，否则此处使用@Autowired会报警告

    //@Resource
    private ImageMapper imgMapper;

    @Test
    void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<test_table> userList = imgMapper.selectList(null);

        for(test_table user:userList) {
            System.out.println(user);
        }
    }
}
