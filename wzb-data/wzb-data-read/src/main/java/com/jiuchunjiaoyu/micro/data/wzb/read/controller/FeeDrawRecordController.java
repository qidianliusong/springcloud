package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.Constant;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeDrawRecordMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/query/feeDrawRecord")
@Api("取班费记录查询")
public class FeeDrawRecordController {

    @Resource
    private FeeDrawRecordMng feeDrawRecordMng;

    @ApiOperation(value = "获取取款记录", notes = "根据id获取取款记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "取款记录id", name = "feeDrawRecordId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/findByID", method = RequestMethod.GET)
    public BaseResult<FeeDrawRecord> findById(@RequestParam(required = true) Long feeDrawRecordId) {
        BaseResult<FeeDrawRecord> baseResult = new BaseResult<>();
        baseResult.setData(feeDrawRecordMng.findOne(feeDrawRecordId));
        return baseResult;
    }

    @ApiOperation(value = "获取取款记录分页信息", notes = "根据班级id获取取款记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    public BaseResult<Page<FeeDrawRecord>> getPage(@RequestParam(required = true) Long classId, @RequestParam(defaultValue = "1") Integer status,
                                                   @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        if(pageSize > Constant.MAX_PAGE_SIZE)
            pageSize = Constant.MAX_PAGE_SIZE;
        BaseResult<Page<FeeDrawRecord>> baseResult = new BaseResult<>();
        baseResult.setData(feeDrawRecordMng.getPage(classId, new Integer[]{status}, pageNo, pageSize));
        return baseResult;
    }

    @ApiOperation(value = "获取预支付状态取款记录", notes = "获取预支付状态取款记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "起始时间", name = "startTime", required = true, dataType = "Date", paramType = "query")
    })
    @RequestMapping(value = "/getPrePayRecords", method = RequestMethod.GET)
    public BaseResult<List<FeeDrawRecord>> getPage(Date startTime) {
        BaseResult<List<FeeDrawRecord>> baseResult = new BaseResult<>();
        if (startTime == null)
            startTime = new Date();
        baseResult.setData(feeDrawRecordMng.getListByStatus(FeeDrawRecord.STATUS_NOT_PAY, startTime));
        return baseResult;
    }

    @ApiOperation(value = "获取预支付状态取款记录数量", notes = "获取预支付状态取款记录数量")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "起始时间", name = "startTime", required = true, dataType = "Date", paramType = "query")
    })
    @RequestMapping(value = "/prePayRecordsCount", method = RequestMethod.GET)
    public BaseResult<Integer> prePayRecordsCount(Date startTime,@RequestParam(required = true) Long classId ) {
        BaseResult<Integer> baseResult = new BaseResult<>();
        if (startTime == null)
            startTime = new Date();
        baseResult.setData(feeDrawRecordMng.countByStatus(FeeDrawRecord.STATUS_NOT_PAY, startTime,classId));
        return baseResult;
    }
}
