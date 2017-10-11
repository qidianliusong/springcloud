package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;
/**
 * 用户
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserDTO implements Serializable {

    public static final int COME_FORM_TEACHER = 2;
    public static final int COME_FROM_PARENT = 1;
    private static final long serialVersionUID = -3803789160611664164L;

    private Long id;
    private String name;
    private Date createAt;
    private Date updateAt;
    private Date deleteAt;
    private String pOpenid;
    private String nickname;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String phone;
    private Integer gender;
    private String realname;
    private Date birth;
    private Date jobTime;
    private String privilege;
    private String edu;
    private String artvarImg;
    private String job;
    private Integer isVip;
    private Integer comeFrom;
    private String tOpenid;
    private Integer isVest;
    private Integer isDisable;

    @ApiModelProperty("用户称谓")
    @JsonProperty("Kinsfolk")
    private String kinsfolk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getpOpenid() {
        return pOpenid;
    }

    public void setpOpenid(String pOpenid) {
        this.pOpenid = pOpenid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Date getJobTime() {
        return jobTime;
    }

    public void setJobTime(Date jobTime) {
        this.jobTime = jobTime;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public String getArtvarImg() {
        return artvarImg;
    }

    public void setArtvarImg(String artvarImg) {
        this.artvarImg = artvarImg;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(Integer comeFrom) {
        this.comeFrom = comeFrom;
    }

    public String gettOpenid() {
        return tOpenid;
    }

    public void settOpenid(String tOpenid) {
        this.tOpenid = tOpenid;
    }

    public Integer getIsVest() {
        return isVest;
    }

    public void setIsVest(Integer isVest) {
        this.isVest = isVest;
    }

    public Integer getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public String getKinsfolk() {
        return kinsfolk;
    }

    public void setKinsfolk(String kinsfolk) {
        this.kinsfolk = kinsfolk;
    }
}
