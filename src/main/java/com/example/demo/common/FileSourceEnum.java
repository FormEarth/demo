package com.example.demo.common;

/**
 * 上传文件渠道
 * @author raining_heavily
 * @date 2019/11/14 12:47
 **/
public enum FileSourceEnum {
    //文章
    ARTICLE("/article/"),
    //图集
    ATLAS("/atlas/"),
    //头像
    AVATAR("/avatar/"),
    //用户背景图
    FRONT_COVER("/front_cover/");

    private String dynamic;
    FileSourceEnum(String dynamic){
        this.dynamic = dynamic;
    }

    public String getDynamic() {
        return dynamic;
    }
}
