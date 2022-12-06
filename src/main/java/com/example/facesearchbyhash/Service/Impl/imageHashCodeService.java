package com.example.facesearchbyhash.Service.Impl;

import java.awt.image.BufferStrategy;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.facesearchbyhash.DeleteFile;
import com.example.facesearchbyhash.Entity.test_table;
import com.example.facesearchbyhash.IMAGE;
import com.example.facesearchbyhash.ImageProcessor;
import com.example.facesearchbyhash.Mapper.ImageMapper;
import com.example.facesearchbyhash.Recognizer;
import com.example.facesearchbyhash.Service.IimageHashCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;


@Service
public class imageHashCodeService implements IimageHashCodeService {

    @Autowired
    private  ImageMapper imgMapper;
 public     Map<String,String> imageHashCode() throws IOException {
        String path = "C:/Users/vials/Desktop/Hash_to_Face/Hash_Search/FaceSearchByHash/src/main/resources/Upload/target/";
        // 获取source目录下的所有图片名
        ArrayList imagesName = new ArrayList();
        getImages(path, imagesName);

        // 创建IMAGE对象数组
        IMAGE[] images = new IMAGE[imagesName.size()];
        for (int i = 0; i < imagesName.size(); ++i) {
            images[i] = new IMAGE(imagesName.get(i).toString());
        }

        // 批量处理图片
        ImageProcessor imageProcessor = new ImageProcessor();
        imageProcessor.batchProcess(path, images);

        /*
         * 计算32位哈希
         * 将生成的64位转换为4进制以压缩至32位
         */
        for (int i = 0; i < images.length; ++i) {
            String hash64 = new String();
            StringBuffer hash32 = new StringBuffer();
            int temp;

            hash64 = images[i].getHashcode_64();
            for (int j = 0; j < 64 - 1; j+=2) {
                String str = hash64.substring(j,j+2);
                temp = Integer.parseInt(str,2);
                hash32.append(temp);
            }

            images[i].setHashcode_32(hash32.toString());
        }

     DeleteFile delfile=new DeleteFile();
        delfile.delete(path);

     Files.createDirectories(Paths.get(path));

     QueryWrapper<test_table> wrapper = new QueryWrapper<>();

     wrapper.select("imgname","Hash");
     List<test_table> imgHash = imgMapper.selectList(wrapper);
     Map<String,String>  hash_add=new HashMap<>();

     Recognizer recognizer = new Recognizer();
     double rate = 0;




     for (test_table i:imgHash) {
         rate = (32 - recognizer.CalculateHammingDistance(images[0].getHashcode_32(),i.getHash()))/32.0 * 100;

         if (rate >= 85) {
             if(rate==100.0){
                 hash_add.put("source",i.getImgname());
                 continue;
             }
             hash_add.put(i.getHash(),i.getImgname());
         }

 }





     return hash_add;





        //图片的hash码：images[i].getHashcode_32()
        // 计算比对结果
//        Recognizer recognizer = new Recognizer();
//        double rate = 0;



//        File fout = new File("out.txt");
//        FileOutputStream fos = new FileOutputStream(fout);
//        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));

        //相似度结果打印
//        for (int i = 0; i < imagesName.size(); ++i) {
//            for (int j = 0; j < imagesName.size(); ++j) {
//                rate = (32 - recognizer.CalculateHammingDistance(images[i].getHashcode_32(),images[j].getHashcode_32()))/32.0 * 100;
//                if (rate >= 85) {
//                    bufferedWriter.write(images[i].getImagePath() + "\t:\t" + images[j].getImagePath() + "\t\t相似度: " + String.format("%.2f", rate) + "%\n");
//                }
//            }
////            bufferedWriter.newLine();
//        }

//        File fout_hash = new File("out_hash.txt");
//        FileOutputStream fos_hash = new FileOutputStream(fout_hash);
//        BufferedWriter bufferedWriter_hash = new BufferedWriter(new OutputStreamWriter(fos_hash));

        //存hash
//        for (int i = 0; i < images.length; ++i) {
//            bufferedWriter_hash.write(images[i].getImagePath() + "\t" + images[i].getHashcode_32() + "\n");
//        }

//        bufferedWriter.close();
//        bufferedWriter_hash.close();
    }

    public  void  getImages(String path, ArrayList imagesName) {
        File file = new File(path);
        File[] array = file.listFiles();
        int imgCount;

        if (array != null) {
            for (imgCount = 0; imgCount < array.length; imgCount++) {
                if (array[imgCount].isFile()) {
                    imagesName.add(array[imgCount].getName());
                }
            }
        }
    }
}
