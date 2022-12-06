import os
import re

sql='insert into HashFace.`test_table` (`imgname`, `id`, `Hash`, `imgaddress`,`humanflag`) values (\'{}\',{},\'{}\',\'{}\',\'{}\');'
i=1
path='C:/Users/vials/Desktop/Hash_to_Face/Hash_Search/FaceSearchByHash/src/main/resources/Upload/img/'
f1=open('./sql.sql',"a",encoding="utf-8")
with open('./out_hash.txt',"r",encoding="utf-8") as f:
    for ann in f.readlines():
        ann = ann.strip('\n').split()      #分割字符
        sql1=sql.format(ann[0],i,ann[1],path+ann[0],re.findall('\d+',ann[0])[0])
        i+=1
        f1.write(sql1+"\n")

f1.close()




