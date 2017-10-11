package com.jiuchunjiaoyu.micro.wzb.schoolBack.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 收费详情
 *
 */
public class FeeDetailDTO implements Serializable{

    public static final int STATUS_OPEN = 0;//开启状态
    public static final int STATUS_CLOSE = 1;//关闭状态
    public static final int STATUS_DEL = 2;//删除状态

	private static final long serialVersionUID = 1132530621582704703L;
	@NotNull(groups = { Update.class })
	private Long feeDetailId;
	@NotNull
	private Long classId;

	private String className;

	private Long schoolId;
	@ApiModelProperty(readOnly = true)
	private Long releaseUserId;
	@NotNull
	@ApiModelProperty("收费类目名称")
	private String feeCategoryName;
	@ApiModelProperty("收费类目ID")
	private Long feeCategoryId;
	@ApiModelProperty(readOnly = true)
	private String releaseUserName;
	private String feeDetailName;
	@Length(min=4,max=5000)
	@ApiModelProperty("收费原因描述")
	private String feeDetailDesc;
	@NotNull
	@Digits(integer=10,fraction=2)
	@ApiModelProperty("收费金额（单价）")
	private BigDecimal amount;

    @ApiModelProperty(value = "已收费金额总计",readOnly = true)
	private BigDecimal totalAmount;
	private Date createTime;
	private Date updateTime;
	private Integer studentCount;
	private Integer paidCount = 0;
	@ApiModelProperty("状态：0为开启，1为关闭，2为删除")
	private Integer status;
	@ApiModelProperty("用于数据库乐观锁，update时需要把上次获取的值发给服务端")
	private int version;

    @ApiModelProperty("当前用户是否已经支付")
	private Boolean paid = false;

	@ApiModelProperty("发布人头像")
	private String releaseUserImg;//发布人头像
	
	private List<FeePayDTO> FeePays;

	public Long getFeeDetailId() {
		return feeDetailId;
	}

	public void setFeeDetailId(Long feeDetailId) {
		this.feeDetailId = feeDetailId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getReleaseUserId() {
		return releaseUserId;
	}

	public void setReleaseUserId(Long releaseUserId) {
		this.releaseUserId = releaseUserId;
	}

	public String getFeeCategoryName() {
		return feeCategoryName;
	}

	public void setFeeCategoryName(String feeCategoryName) {
		this.feeCategoryName = feeCategoryName;
	}

	public Long getFeeCategoryId() {
		return feeCategoryId;
	}

	public void setFeeCategoryId(Long feeCategoryId) {
		this.feeCategoryId = feeCategoryId;
	}

	public String getReleaseUserName() {
		return releaseUserName;
	}

	public void setReleaseUserName(String releaseUserName) {
		this.releaseUserName = releaseUserName;
	}

	public String getFeeDetailName() {
		return feeDetailName;
	}

	public void setFeeDetailName(String feeDetailName) {
		this.feeDetailName = feeDetailName;
	}

	public String getFeeDetailDesc() {
		return feeDetailDesc;
	}

	public void setFeeDetailDesc(String feeDetailDesc) {
		this.feeDetailDesc = feeDetailDesc;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Integer getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	public Integer getPaidCount() {
		return paidCount;
	}

	public void setPaidCount(Integer paidCount) {
		this.paidCount = paidCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<FeePayDTO> getFeePays() {
		return FeePays;
	}

	public void setFeePays(List<FeePayDTO> feePays) {
		FeePays = feePays;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

	public String getReleaseUserImg() {
		return releaseUserImg;
	}

	public void setReleaseUserImg(String releaseUserImg) {
		this.releaseUserImg = releaseUserImg;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public static interface Update {

	}
}
