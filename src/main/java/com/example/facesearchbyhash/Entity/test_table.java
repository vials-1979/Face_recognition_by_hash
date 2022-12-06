package com.example.facesearchbyhash.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class test_table {
    private Integer id;
    private String imgname;
    private String Hash;
    private String imgaddress;
    private String humanflag;
}
