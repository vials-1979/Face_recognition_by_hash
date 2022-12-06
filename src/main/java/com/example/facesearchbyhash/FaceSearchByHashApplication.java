package com.example.facesearchbyhash;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan("com.example.facesearchbyhash.Mapper")
//@ComponentScan(basePackages = "com.example.facesearchbyhash.Mapper")
//@SpringBootApplication(scanBasePackages="Controller")
public class FaceSearchByHashApplication {


    public static void main(String[] args) {
        SpringApplication.run(FaceSearchByHashApplication.class, args);
    }

}

