-- 人脸的数据信息
create table if not exists HashFace.`test_table`
(
`imgname` varchar(256) not null comment '图片名',
`id` int not null comment '图片id' primary key,
`Hash` varchar(256) not null comment '图片Hash',
`imgaddress` varchar(256) not null comment '图片服务器地址',
`humanflag`  varchar(10) not null comment  '图片人物标识'
) comment '人脸的数据信息';


