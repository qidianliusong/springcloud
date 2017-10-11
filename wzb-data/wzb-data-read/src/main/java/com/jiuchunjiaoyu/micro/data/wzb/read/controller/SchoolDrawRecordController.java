package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.Constant;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.WzbDataReadErr;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.SchoolDrawRecordMng;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/query/schoolDrawRecord")
@Api(tags = "学校取款查询接口")
public class SchoolDrawRecordController {

    @Resource
    private SchoolDrawRecordMng schoolDrawRecordMng;

    @ApiOperation(value = "根绝学校id获取学校取款分页数据", notes = "根绝学校id获取学校取款分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "起始日期(yyyy-MM-dd HH:ss:mm)", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期(yyyy-MM-dd HH:ss:mm)", name = "endDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getPageBySchoolId", method = RequestMethod.GET)
    public BaseResult<Page<SchoolDrawRecord>> getPageBySchoolId(Long schoolId, Date startDate, Date endDate, Integer status,
                                                                @RequestParam(defaultValue = "0") Integer pageNo,
                                                                @RequestParam(defaultValue = "10") Integer pageSize) {
        if (pageSize > Constant.MAX_PAGE_SIZE)
            pageSize = Constant.MAX_PAGE_SIZE;
        BaseResult<Page<SchoolDrawRecord>> baseResult = new BaseResult<>();
        if(status != null && status == -1)
            status = null;
        if (startDate == null) {
            baseResult.setData(schoolDrawRecordMng.getPageBySchoolId(schoolId, status, pageNo, pageSize));
            return baseResult;
        }
        if (endDate == null)
            endDate = new Date();

        baseResult.setData(schoolDrawRecordMng.getPageBySchoolId(schoolId, status, startDate, endDate, pageNo, pageSize));

        return baseResult;
    }

    @ApiOperation(value = "获取学校取款分页数据", notes = "获取学校取款分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校名称", name = "schoolName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "起始日期(yyyy-MM-dd HH:ss:mm)", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期(yyyy-MM-dd HH:ss:mm)", name = "endDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "状态（空为查询全部）", name = "status", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getPage", method = RequestMethod.GET)
    public BaseResult<Page<SchoolDrawRecord>> getPage(String schoolName, Date startDate, Date endDate, Integer status,
                                                      @RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        if (pageSize > Constant.MAX_PAGE_SIZE)
            pageSize = Constant.MAX_PAGE_SIZE;
        BaseResult<Page<SchoolDrawRecord>> baseResult = new BaseResult<>();
        baseResult.setData(schoolDrawRecordMng.getPage(schoolName, status, startDate, endDate, pageNo, pageSize));
        return baseResult;
    }

    @ApiOperation(value = "获取学校取款统计数据", notes = "获取学校取款统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "起始日期(yyyy-MM-dd HH:ss:mm)", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期(yyyy-MM-dd HH:ss:mm)", name = "endDate", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getStatistics", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getStatistics(Date startDate, Date endDate) {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();
        if (startDate == null)
            startDate = new Date();
        if (endDate == null)
            endDate = new Date();
        BigDecimal amount = schoolDrawRecordMng.getSum(startDate, endDate);
        Map<String, Object> resultMap = new HashMap<>();
        if(amount == null)
            amount = new BigDecimal(0);
        resultMap.put("outAmount", amount);
        baseResult.setData(resultMap);
        return baseResult;
    }

    @ApiOperation(value = "根据id查询取款记录", notes = "根据id查询取款记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "schoolDrawRecordId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public BaseResult<SchoolDrawRecord> findById(@RequestParam long schoolDrawRecordId){
        BaseResult<SchoolDrawRecord> baseResult = new BaseResult<>();
        SchoolDrawRecord schoolDrawRecord = schoolDrawRecordMng.findOne(schoolDrawRecordId);
        if(schoolDrawRecord == null){
            baseResult.setCode(WzbDataReadErr.entity_not_exist.getCode());
            baseResult.setMessage("不存在的学校取款记录，id="+schoolDrawRecordId);
            return baseResult;
        }
        baseResult.setData(schoolDrawRecord);
        return baseResult;
    }
}
