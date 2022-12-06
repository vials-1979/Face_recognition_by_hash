package com.example.facesearchbyhash.Service;

import com.example.facesearchbyhash.Entity.test_table;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IimageHashCodeService {
    Map<String,String> imageHashCode() throws IOException;
   void getImages(String path, ArrayList imagesName);
}
