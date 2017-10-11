package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import com.jiuchunjiaoyu.micro.data.wzb.write.dto.SchoolDrawAuditDTO;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.SchoolDrawRecordMng;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 班级账户控制器
 */
@RestController
@RequestMapping("/operate/schoolDrawRecord")
public class SchoolDrawRecordController extends CommonBaseController {

    @Resource
    private SchoolDrawRecordMng schoolDrawRecordMng;

    @ApiOperation(value = "新建学校取款记录", notes = "新建学校取款记录")
    @ApiImplicitParams({})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<SchoolDrawRecord> save(@RequestBody @Validated SchoolDrawRecord schoolDrawRecord, BindingResult bindingResult) throws WzbDataException {
        BaseResult<SchoolDrawRecord> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult))
            return baseResult;
        if (schoolDrawRecord.getSchoolDrawRecordId() != null)
            schoolDrawRecord.setSchoolDrawRecordId(null);
        schoolDrawRecord.setCreateTime(new Date());
        schoolDrawRecord.setStatus(SchoolDrawRecord.STATUS_INIT);
        SchoolDrawRecord save = schoolDrawRecordMng.save(schoolDrawRecord);
        baseResult.setData(save);
        return baseResult;
    }

    @ApiOperation(value = "处理学校取款申请", notes = "处理学校取款申请")
    @ApiImplicitParams({})
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public BaseResult<Void> audit(@RequestBody @Validated SchoolDrawAuditDTO schoolDrawAuditDTO, BindingResult bindingResult) throws WzbDataException {
        BaseResult<Void> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult))
            return baseResult;
        SchoolDrawRecord schoolDrawRecord = schoolDrawRecordMng.findOne(schoolDrawAuditDTO.getSchoolDrawRecordId());
        if (schoolDrawRecord == null) {
            baseResult.setCode(WzbDataWriteErr.entity_not_exist.getCode());
            baseResult.setMessage("不存在的学校取款记录，id=" + schoolDrawAuditDTO.getSchoolDrawRecordId());
            return baseResult;
        }
        if(schoolDrawRecord.getStatus() != SchoolDrawRecord.STATUS_INIT){
            baseResult.setCode(WzbDataWriteErr.operation_not_permitted.getCode());
            baseResult.setMessage(WzbDataWriteErr.operation_not_permitted.getMessage());
            return baseResult;
        }
        //处理驳回
        if (schoolDrawAuditDTO.getStatus() == SchoolDrawRecord.STATUS_REJECT) {
            schoolDrawRecord.setStatus(SchoolDrawRecord.STATUS_REJECT);
            schoolDrawRecord.setRejectTime(new Date());
            schoolDrawRecord.setAuditorUserId(schoolDrawAuditDTO.getAuditorUserId());
            schoolDrawRecord.setAuditorUserName(schoolDrawAuditDTO.getAuditorUserName());
            schoolDrawRecord.setRejectReason(schoolDrawAuditDTO.getMessage());
            schoolDrawRecordMng.save(schoolDrawRecord);
            return baseResult;
        }
        if (schoolDrawAuditDTO.getStatus() == SchoolDrawRecord.STATUS_PASS) {
            schoolDrawRecord.setStatus(SchoolDrawRecord.STATUS_PASS);
            schoolDrawRecord.setDrawTime(new Date());
            schoolDrawRecord.setAuditorUserId(schoolDrawAuditDTO.getAuditorUserId());
            schoolDrawRecord.setAuditorUserName(schoolDrawAuditDTO.getAuditorUserName());
            schoolDrawRecordMng.passSchoolDrawRecord(schoolDrawRecord);
            return baseResult;
        }
        baseResult.setCode(SystemCode.illegal_paramter.getCode());
        baseResult.setMessage("status参数错误");
        return baseResult;
    }

    @ApiOperation(value = "删除学校取款记录", notes = "删除学校取款记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校取款记录id", name = "schoolDrawRecordId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public BaseResult<Void> delete(@RequestParam long schoolDrawRecordId) throws WzbDataException {
        BaseResult<Void> baseResult = new BaseResult<>();
        SchoolDrawRecord schoolDrawRecord = schoolDrawRecordMng.findOne(schoolDrawRecordId);
        if(schoolDrawRecord == null){
            baseResult.setCode(WzbDataWriteErr.entity_not_exist.getCode());
            baseResult.setMessage("不存在的学校取款记录，id="+schoolDrawRecordId);
            return baseResult;
        }
        if(schoolDrawRecord.getStatus() != SchoolDrawRecord.STATUS_PASS){
            schoolDrawRecordMng.delete(schoolDrawRecordId);
            return baseResult;
        }
        schoolDrawRecordMng.deleteSchoolDrawRecord(schoolDrawRecordId);
        return baseResult;
    }
}
