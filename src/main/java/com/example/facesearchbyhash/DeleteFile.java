package com.example.facesearchbyhash;



import java.io.File;

public class DeleteFile {

    public boolean delete(String path){
        File file = new File(path);
        if(!file.exists()){
            return false;
        }
        if(file.isFile()){
            return file.delete();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if(f.isFile()){
                if(!f.delete()){
                    System.out.println(f.getAbsolutePath()+" delete error!");
                    return false;
                }
            }else{
                if(!this.delete(f.getAbsolutePath())){
                    return false;
                }
            }
        }
        return file.delete();
    }



}