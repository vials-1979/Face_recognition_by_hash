package com.example.facesearchbyhash;

public class IMAGE {
    private String imagePath;
    private String hashcode_64;
    private String hashcode_32;

    public IMAGE() {
    }

    public IMAGE(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setHashcode_64(String hashcode) {
        this.hashcode_64 = hashcode;
    }

    public String getHashcode_64() {
        return this.hashcode_64;
    }

    public String getHashcode_32() {
        return hashcode_32;
    }

    public void setHashcode_32(String hashcode_32) {
        this.hashcode_32 = hashcode_32;
    }
}
