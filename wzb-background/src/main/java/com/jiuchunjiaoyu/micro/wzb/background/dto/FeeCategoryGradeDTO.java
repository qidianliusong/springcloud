package com.jiuchunjiaoyu.micro.wzb.background.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 班费类别年级关系表
 */
public class FeeCategoryGradeDTO {

    private Long id;
    @ApiModelProperty(value = "年级id")
    private Long gradeId;
    @ApiModelProperty(value = "年级名称")
    private String gradeName;
    private Long feeCategoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Long getFeeCategoryId() {
        return feeCategoryId;
    }

    public void setFeeCategoryId(Long feeCategoryId) {
        this.feeCategoryId = feeCategoryId;
    }
}
