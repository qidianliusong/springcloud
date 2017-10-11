package com.jiuchunjiaoyu.micro.wzb.background.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.background.constant.WzbBackgroundErr;
import com.jiuchunjiaoyu.micro.wzb.background.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.wzb.background.dto.GeneralAccountDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.PayMessageDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.SchoolDrawAuditDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.SchoolDrawRecordDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.wzb.background.entity.SysUser;
import com.jiuchunjiaoyu.micro.wzb.background.exception.WzbBackgroundException;
import com.jiuchunjiaoyu.micro.wzb.background.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.wzb.background.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.wzb.background.util.FeignUtil;
import com.jiuchunjiaoyu.micro.wzb.background.util.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wzb-background/schoolDrawRecord")
@Api(tags = "学校取款查询接口")
public class SchoolDrawRecordController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(SchoolDrawRecordController.class);

    @Resource
    private WzbDataReadFeign readFeign;

    @Resource
    private WzbDataWriteFeign writeFeign;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @ApiOperation(value = "获取学校取款分页数据", notes = "获取学校取款分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校名称", name = "schoolName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "起始日期(yyyy-MM-dd HH:ss:mm)", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期(yyyy-MM-dd HH:ss:mm)", name = "endDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "状态（空为查询全部,0为待审批，1为被驳回，2为通过）", name = "status", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getPage", method = RequestMethod.GET)
    public BaseResult<PageDTO<SchoolDrawRecordDTO>> getPage(String schoolName, String startDate, String endDate, Integer status,
                                                            @RequestParam(defaultValue = "1") Integer pageNo,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) throws WzbBackgroundException {
        BaseResult<PageDTO<SchoolDrawRecordDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<SchoolDrawRecordDTO>> schoolDrawRecordPage = readFeign.getSchoolDrawRecordPage(schoolName, startDate, endDate, status, pageNo - 1, pageSize);
        FeignUtil.validateFeignResult(schoolDrawRecordPage, "readFeign.getSchoolDrawRecordPage", logger);
        baseResult.setData(schoolDrawRecordPage.getData());
        return baseResult;
    }

    @ApiOperation(value = "根据天数获取学校取款分页数据", notes = "获取学校取款分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校名称", name = "schoolName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "天数(从0开始，0为今天，6为7天)", name = "days", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "状态（空为查询全部,0为待审批，1为被驳回，2为通过）", name = "status", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getPageByDays", method = RequestMethod.GET)
    public BaseResult<PageDTO<SchoolDrawRecordDTO>> getPageByDays(String schoolName, @RequestParam int days, Integer status,
                                                                  @RequestParam(defaultValue = "1") Integer pageNo,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize) throws WzbBackgroundException {
        BaseResult<PageDTO<SchoolDrawRecordDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<SchoolDrawRecordDTO>> schoolDrawRecordPage = readFeign.getSchoolDrawRecordPage(schoolName, getStartTimeBeforeDays(days),
                getEndTimeBeforeDays(0), status, pageNo - 1, pageSize);
        FeignUtil.validateFeignResult(schoolDrawRecordPage, "readFeign.getSchoolDrawRecordPage", logger);
        baseResult.setData(schoolDrawRecordPage.getData());
        return baseResult;
    }

    @ApiOperation(value = "审批学校取款申请", notes = "审批学校取款申请")
    @ApiImplicitParams({})
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public BaseResult<Void> audit(@Validated SchoolDrawAuditDTO schoolDrawAuditDTO, BindingResult bindingResult) throws WzbBackgroundException {
        BaseResult<Void> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult))
            return baseResult;
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getSession().getAttribute(WzbConstant.SESSION_USER_KEY);
        if (sysUser == null) {
            baseResult.setCode(WzbBackgroundErr.session_time_out.getCode());
            baseResult.setMessage(WzbBackgroundErr.session_time_out.getMessage());
            return baseResult;
        }
        schoolDrawAuditDTO.setAuditorUserId(sysUser.getUid());
        schoolDrawAuditDTO.setAuditorUserName(sysUser.getName());
        BaseResult<Void> auditSchoolDrawResult = writeFeign.auditSchoolDraw(schoolDrawAuditDTO);
        FeignUtil.validateFeignResult(auditSchoolDrawResult, "writeFeign.auditSchoolDraw", logger);
        if (schoolDrawAuditDTO.getStatus() == SchoolDrawRecordDTO.STATUS_PASS) {
            BaseResult<SchoolDrawRecordDTO> schoolDrawRecordById = readFeign.findSchoolDrawRecordById(schoolDrawAuditDTO.getSchoolDrawRecordId());
            SchoolDrawRecordDTO schoolDrawRecordDTO = schoolDrawRecordById.getData();
            //发送消息到mq
            PayMessageDTO payMessageDTO = new PayMessageDTO();
            payMessageDTO.setAmount(schoolDrawRecordDTO.getAmount());
            payMessageDTO.setId(schoolDrawRecordDTO.getSchoolDrawRecordId());
            payMessageDTO.setType(PayMessageDTO.TYPE_SCHOOL_DRAW);
            rabbitTemplate.convertAndSend("wzb.topic", "wzb.pay.message", GsonUtil.GSON.toJson(payMessageDTO));
        }
        return baseResult;
    }

    @ApiOperation(value = "获取学校取款统计数据", notes = "获取学校取款统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "起始日期(yyyy-MM-dd HH:ss:mm)", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期(yyyy-MM-dd HH:ss:mm)", name = "endDate", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getStatistics", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getStatistics(String startDate, String endDate) throws WzbBackgroundException {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();
        BaseResult<GeneralAccountDTO> generalAccount = readFeign.getGeneralAccount();
        FeignUtil.validateFeignResult(generalAccount, "readFeign.getGeneralAccount", logger);
        GeneralAccountDTO generalAccountDTO = generalAccount.getData();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("amount", generalAccountDTO.getSchoolAmount());
        BaseResult<Map<String, Object>> schoolDrawRecordStatistics = readFeign.getSchoolDrawRecordStatistics(startDate, endDate);
        FeignUtil.validateFeignResult(schoolDrawRecordStatistics, "readFeign.getSchoolDrawRecordStatistics", logger);
        resultMap.putAll(schoolDrawRecordStatistics.getData());
        baseResult.setData(resultMap);
        return baseResult;
    }

    @ApiOperation(value = "根据天数获取学校取款统计数据", notes = "获取学校取款统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "天数(从0开始，0为今天，6为7天)", name = "days", required = true, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getStatisticsByDays", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getStatisticsByDays(@RequestParam int days) throws WzbBackgroundException {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();
        BaseResult<GeneralAccountDTO> generalAccount = readFeign.getGeneralAccount();
        FeignUtil.validateFeignResult(generalAccount, "readFeign.getGeneralAccount", logger);
        GeneralAccountDTO generalAccountDTO = generalAccount.getData();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("amount", generalAccountDTO.getSchoolAmount());
        BaseResult<Map<String, Object>> schoolDrawRecordStatistics = readFeign.getSchoolDrawRecordStatistics(getStartTimeBeforeDays(days), getEndTimeBeforeDays(0));
        FeignUtil.validateFeignResult(schoolDrawRecordStatistics, "readFeign.getSchoolDrawRecordStatistics", logger);
        resultMap.putAll(schoolDrawRecordStatistics.getData());
        baseResult.setData(resultMap);
        return baseResult;
    }
}
