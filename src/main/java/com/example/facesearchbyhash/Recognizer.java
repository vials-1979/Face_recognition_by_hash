package com.example.facesearchbyhash;

public class Recognizer {
    public int CalculateHammingDistance(String hashA, String hashB) {
        int distance = 0;

        for(int i = 0; i < hashA.length(); ++i) {
            if (hashA.charAt(i) != hashB.charAt(i))
                distance++;
        }

        return distance;
    }
}

