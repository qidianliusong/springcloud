package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * 微信认证信息
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WxAuthDTO implements Serializable{

    private String openid;

    private String unionid;


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

}
