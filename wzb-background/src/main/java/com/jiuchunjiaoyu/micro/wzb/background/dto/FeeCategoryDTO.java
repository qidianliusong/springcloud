package com.jiuchunjiaoyu.micro.wzb.background.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 班费类别
 */
public class FeeCategoryDTO {
    /**
     * 普通类型
     */
    public static final int TYPE_COMMON = 0;
    /**
     * 后台配置类型
     */
    public static final int TYPE_BACKGROUND = 1;

    public static final int STATUS_OPEN = 0;//开启状态
    public static final int STATUS_CLOSE = 1;//关闭状态

    @ApiModelProperty("收费类目id")
    private Long feeCategoryId;

    @NotNull
    @ApiModelProperty(value = "类目名称", required = true)
    private String feeCategoryName;
    @ApiModelProperty(value = "类目简介")
    private String feeCategoryDesc;
    @NotNull
    @ApiModelProperty(value = "学校id", required = true)
    private Long schoolId;
    @ApiModelProperty(value = "班级id", readOnly = true)
    private Long classId;

    @ApiModelProperty(value = "是否为全部年级")
    private Boolean allGrade;
    /**
     * 收费金额
     */
    @NotNull
    @DecimalMin("0")
    @Digits(integer = 10, fraction = 2)
    @ApiModelProperty(value = "收费总金额", required = true)
    private BigDecimal amount;
    /**
     * 0为普通类型,1为学校后台配置固定项目
     */
    @ApiModelProperty(value = "收费类目类型", readOnly = true)
    private Integer type;
    /**
     * 状态:0为正常状态
     */
    @ApiModelProperty(value = "0为正常状态,1为关闭,2为删除", readOnly = true)
    private Integer status;
    @ApiModelProperty(value = "创建时间", readOnly = true)
    private Date createDate;
    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id", required = true)
    private Long createUserId;
    /**
     * 最近一次修改时间
     */
    @ApiModelProperty(value = "修改时间", readOnly = true)
    private Date updateDate;
    /**
     * 最近一次修改的修改人
     */
    @ApiModelProperty(value = "修改人id", required = true)
    private Long updateUserId;

    @ApiModelProperty(hidden = true)
    private Integer priority;

    private List<FeeCategoryGradeDTO> feeCategoryGrades;

    public Long getFeeCategoryId() {
        return feeCategoryId;
    }

    public void setFeeCategoryId(Long feeCategoryId) {
        this.feeCategoryId = feeCategoryId;
    }

    public String getFeeCategoryName() {
        return feeCategoryName;
    }

    public void setFeeCategoryName(String feeCategoryName) {
        this.feeCategoryName = feeCategoryName;
    }

    public String getFeeCategoryDesc() {
        return feeCategoryDesc;
    }

    public void setFeeCategoryDesc(String feeCategoryDesc) {
        this.feeCategoryDesc = feeCategoryDesc;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Boolean getAllGrade() {
        return allGrade;
    }

    public void setAllGrade(Boolean allGrade) {
        this.allGrade = allGrade;
    }

    public List<FeeCategoryGradeDTO> getFeeCategoryGrades() {
        return feeCategoryGrades;
    }

    public void setFeeCategoryGrades(List<FeeCategoryGradeDTO> feeCategoryGrades) {
        this.feeCategoryGrades = feeCategoryGrades;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
