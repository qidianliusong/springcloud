package com.jiuchunjiaoyu.micro.wzb.background.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.background.dto.AccountFlowDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.ClassAccountDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.ProvinceFlowAccountDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.SchoolFlowAccountDTO;
import com.jiuchunjiaoyu.micro.wzb.background.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.wzb.background.exception.WzbBackgroundException;
import com.jiuchunjiaoyu.micro.wzb.background.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.wzb.background.manager.AccountFlowMng;
import com.jiuchunjiaoyu.micro.wzb.background.util.FeignUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wzb-background/accountFlow")
@Api(tags = "账户流水统计")
public class AccountFlowController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(AccountFlowController.class);

    @Resource
    private AccountFlowMng accountFlowMng;

    @Resource
    private WzbDataReadFeign readFeign;

    @ApiOperation(value = "获取流水账户统计信息", notes = "获取流水账户统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "省", name = "province", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "开始日期（yyyy-MM-dd HH:ss:mm）", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期（yyyy-MM-dd HH:ss:mm）", name = "endDate", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getStatistics", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getStatistics(String province, Long schoolId, Long classId, String startDate, String endDate) throws WzbBackgroundException {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();
        BaseResult<BigDecimal> amountResult = readFeign.getAmount(province, schoolId, classId);
        FeignUtil.validateFeignResult(amountResult, "readFeign.getAmount", logger);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("amount", amountResult.getData());
        Map<String, BigDecimal> statistics = accountFlowMng.getStatistics(province, schoolId, classId, startDate, endDate);
        resultMap.putAll(statistics);
        baseResult.setData(resultMap);
        return baseResult;
    }

    @ApiOperation(value = "按照给定天数获取统计信息", notes = "按照给定天数获取统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "省", name = "province", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "天数(从0开始，0为今天，6为7天)", name = "days", required = true, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getStatisticsByDays", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getStatisticsByDays(String province, Long schoolId, Long classId, @RequestParam int days) throws WzbBackgroundException {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();
        Map<String, Object> resultMap = new HashMap<>();

        BaseResult<BigDecimal> amountResult = readFeign.getAmount(province, schoolId, classId);
        FeignUtil.validateFeignResult(amountResult, "readFeign.getAmount", logger);

        resultMap.put("amount", amountResult.getData());

        BigDecimal in = new BigDecimal(0);
        BigDecimal out = new BigDecimal(0);
        Map<String, BigDecimal> statistics0 = accountFlowMng.getStatistics(province, schoolId, classId, getStartTimeBeforeDays(0), getEndTimeBeforeDays(0));
        in = in.add(statistics0.get("inAmount"));
        out = out.add(statistics0.get("outAmount"));

        if (days == 0) {
            resultMap.put("inAmount", in);
            resultMap.put("outAmount", out);
            baseResult.setData(resultMap);
            return baseResult;
        }
        Map<String, BigDecimal> statistics = accountFlowMng.getStatisticsUseCache(province, schoolId, classId, getStartTimeBeforeDays(days), getEndTimeBeforeDays(1));
        in = in.add(statistics.get("inAmount"));
        out = out.add(statistics.get("outAmount"));
        resultMap.put("inAmount", in);
        resultMap.put("outAmount", out);
        baseResult.setData(resultMap);
        return baseResult;
    }



    @ApiOperation(value = "获取省流水账户列表", notes = "获取省流水账户列表")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/getProvinceFlowAccountList", method = RequestMethod.GET)
    public BaseResult<List<ProvinceFlowAccountDTO>> getProvinceFlowAccount() throws WzbBackgroundException {
        BaseResult<List<ProvinceFlowAccountDTO>> baseResult = new BaseResult<>();
        BaseResult<List<ProvinceFlowAccountDTO>> provinceFlowAccountList = readFeign.getProvinceFlowAccountList();
        FeignUtil.validateFeignResult(provinceFlowAccountList, "readFeign.getProvinceFlowAccountList", logger);
        baseResult.setData(provinceFlowAccountList.getData());
        return baseResult;
    }

    @ApiOperation(value = "获取学校流水账户列表", notes = "获取学校流水账户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "省", name = "province", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "学校名称（模糊查询）", name = "schoolName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getSchoolFlowAccountPage", method = RequestMethod.GET)
    public BaseResult<PageDTO<SchoolFlowAccountDTO>> getSchoolFlowAccountPage(String province, String schoolName,
                                                                              @RequestParam(defaultValue = "1") Integer pageNo,
                                                                              @RequestParam(defaultValue = "10") Integer pageSize) throws WzbBackgroundException {
        BaseResult<PageDTO<SchoolFlowAccountDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<SchoolFlowAccountDTO>> schoolFlowAccountPage = readFeign.getSchoolFlowAccountPage(province, schoolName, pageNo - 1, pageSize);
        FeignUtil.validateFeignResult(schoolFlowAccountPage, "readFeign.getSchoolFlowAccountPage", logger);
        baseResult.setData(schoolFlowAccountPage.getData());
        return baseResult;
    }

    @ApiOperation(value = "获取账户流水分页信息", notes = "获取账户流水分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "省", name = "province", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "开始日期（yyyy-MM-dd HH:ss:mm）", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期（yyyy-MM-dd HH:ss:mm）", name = "endDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "类型，0为收入，1为支出", name = "type", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getPage", method = RequestMethod.GET)
    public BaseResult<PageDTO<AccountFlowDTO>> getPage(String province, Long schoolId, Long classId, Integer type, String startDate, String endDate,
                                                       @RequestParam(defaultValue = "1") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize) throws WzbBackgroundException {
        BaseResult<PageDTO<AccountFlowDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<AccountFlowDTO>> accountFlowPage = readFeign.getAccountFlowPage(province, schoolId, classId, type, startDate, endDate, pageNo - 1, pageSize);
        FeignUtil.validateFeignResult(accountFlowPage, "readFeign.getAccountFlowPage", logger);
        baseResult.setData(accountFlowPage.getData());
        return baseResult;
    }

    @ApiOperation(value = "获取班级账户分页信息", notes = "获取班级账户分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "班级名称（模糊查询）", name = "className", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getClassAccountPage", method = RequestMethod.GET)
    public BaseResult<PageDTO<ClassAccountDTO>> getClassAccountPage(@RequestParam(required = true) long schoolId, String className, @RequestParam(defaultValue = "1") Integer pageNo,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        BaseResult<PageDTO<ClassAccountDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<ClassAccountDTO>> classAccountPage = readFeign.getClassAccountPage(schoolId, className, pageNo - 1, pageSize);
        baseResult.setData(classAccountPage.getData());
        return baseResult;
    }

}