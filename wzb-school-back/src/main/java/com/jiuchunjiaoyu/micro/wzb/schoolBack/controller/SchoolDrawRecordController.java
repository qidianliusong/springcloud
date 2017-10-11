package com.jiuchunjiaoyu.micro.wzb.schoolBack.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.SchoolAccountDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.SchoolDrawRecordDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.exception.WzbBackgroundException;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.util.FeignUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 收费类目接口
 */
@Api(tags = "学校取款相关接口")
@RestController
@RequestMapping("/wzb-school/schoolDrawRecord")
public class SchoolDrawRecordController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(SchoolDrawRecordController.class);

    @Resource
    private WzbDataReadFeign readFeign;

    @Resource
    private WzbDataWriteFeign writeFeign;

    @ApiOperation(value = "根绝学校id获取学校取款分页数据", notes = "根绝学校id获取学校取款分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "起始日期(yyyy-MM-dd HH:ss:mm)", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期(yyyy-MM-dd HH:ss:mm)", name = "endDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "状态：0为待审中，1为被驳回，2为审批通过，不传该参数为查询所有", name = "status", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getPageBySchoolId", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getPageBySchoolId(@RequestParam long schoolId, String startDate, String endDate, Integer status,
                                                             @RequestParam(defaultValue = "1") Integer pageNo,
                                                             @RequestParam(defaultValue = "10") Integer pageSize) throws WzbBackgroundException {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();
        Map<String, Object> resultMap = new HashMap<>();
        BaseResult<PageDTO<SchoolDrawRecordDTO>> schoolDrawRecordPageBySchoolId = readFeign.getSchoolDrawRecordPageBySchoolId(schoolId, startDate, endDate, status, pageNo - 1, pageSize);
        FeignUtil.validateFeignResult(schoolDrawRecordPageBySchoolId, "readFeign.getSchoolDrawRecordPageBySchoolId", logger);
        resultMap.put("page", schoolDrawRecordPageBySchoolId.getData());
        BaseResult<SchoolAccountDTO> schoolAccountBySchoolId = readFeign.findSchoolAccountBySchoolId(schoolId);
        FeignUtil.validateFeignResult(schoolAccountBySchoolId, "readFeign.findSchoolAccountBySchoolId", logger);
        resultMap.put("schoolAmount", schoolAccountBySchoolId.getData().getSchoolAmount());
        baseResult.setData(resultMap);
        return baseResult;
    }

    @ApiOperation(value = "新建取款记录（发起取款申请）", notes = "新建取款记录（发起取款申请）")
    @ApiImplicitParams({})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<SchoolDrawRecordDTO> save(@Validated SchoolDrawRecordDTO schoolDrawRecord, BindingResult bindingResult) throws WzbBackgroundException {
        BaseResult<SchoolDrawRecordDTO> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult))
            return baseResult;
        BaseResult<SchoolAccountDTO> schoolAccountBySchoolId = readFeign.findSchoolAccountBySchoolId(schoolDrawRecord.getSchoolId());
        FeignUtil.validateFeignResult(schoolAccountBySchoolId, "readFeign.findSchoolAccountBySchoolId", logger);
        schoolDrawRecord.setLeftAmount(schoolAccountBySchoolId.getData().getSchoolAmount());
        BaseResult<SchoolDrawRecordDTO> schoolDrawRecordDTOBaseResult = writeFeign.saveSchoolDrawRecord(schoolDrawRecord);
        FeignUtil.validateFeignResult(schoolDrawRecordDTOBaseResult, "writeFeign.saveOrUpdateSchoolDrawRecord", logger);
        baseResult.setData(schoolDrawRecordDTOBaseResult.getData());
        return baseResult;
    }

    @ApiOperation(value = "删除学校取款记录", notes = "删除学校取款记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校取款记录id", name = "schoolDrawRecordId", required = true, dataType = "Long", paramType = "query")
    })
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResult<Void> delete(@RequestParam long schoolDrawRecordId) throws WzbBackgroundException {
        BaseResult<Void> baseResult = new BaseResult<>();
        BaseResult<Void> deleteSchoolDrawRecord = writeFeign.deleteSchoolDrawRecord(schoolDrawRecordId);
        FeignUtil.validateFeignResult(deleteSchoolDrawRecord, "writeFeign.deleteSchoolDrawRecord", logger);
        return baseResult;
    }

}
