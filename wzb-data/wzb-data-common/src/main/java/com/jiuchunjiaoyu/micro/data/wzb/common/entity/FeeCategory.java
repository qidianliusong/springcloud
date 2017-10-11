package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 班费类别
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
@OptimisticLocking
public class FeeCategory {
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

    @Id
    @GeneratedValue
    private Long feeCategoryId;

    @NotNull
    @Column
    private String feeCategoryName;
    @Column
    private String feeCategoryDesc;
    @Column
    private Long schoolId;
    @Column
    private Long classId;

    @Column
    private Boolean allGrade;
    /**
     * 收费金额
     */
    @Column
    private BigDecimal amount;
    /**
     * 0为普通类型,1为学校后台配置固定项目
     */
    @NotNull
    @Column
    private Integer type;
    /**
     * 状态:0为正常状态
     */
    @Column
    private Integer status;
    @Column(updatable = false)
    private Date createDate;
    /**
     * 创建人id
     */
    @Column(updatable = false)
    private Long createUserId;
    /**
     * 最近一次修改时间
     */
    @Column
    private Date updateDate;
    /**
     * 最近一次修改的修改人
     */
    @Column
    private Long updateUserId;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY, mappedBy = "feeCategoryId")
    private Set<FeeCategoryGrade> feeCategoryGradeSet = new HashSet<>();

    @Transient
    private List<FeeCategoryGrade> feeCategoryGrades;

    private Integer priority;

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

    public Set<FeeCategoryGrade> getFeeCategoryGradeSet() {
        return feeCategoryGradeSet;
    }

    public void setFeeCategoryGradeSet(Set<FeeCategoryGrade> feeCategoryGradeSet) {
        this.feeCategoryGradeSet = feeCategoryGradeSet;
    }

    public List<FeeCategoryGrade> getFeeCategoryGrades() {
        return feeCategoryGrades;
    }

    public void setFeeCategoryGrades(List<FeeCategoryGrade> feeCategoryGrades) {
        this.feeCategoryGrades = feeCategoryGrades;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
