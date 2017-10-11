package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.*;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.Constant;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.AccountFlowMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.ClassAccountMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/query/accountFlow")
@Api(tags = "账户流水查询相关接口")
public class AccountFlowController extends CommonBaseController {

    @Resource
    private AccountFlowMng accountFlowMng;

    @Resource
    private ClassAccountMng classAccountMng;

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
    public BaseResult<Page<AccountFlow>> getPage(String province, Long schoolId, Long classId, Integer type, Date startDate, Date endDate,
                                                 @RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        if (pageSize > Constant.MAX_PAGE_SIZE)
            pageSize = Constant.MAX_PAGE_SIZE;
        BaseResult<Page<AccountFlow>> baseResult = new BaseResult<>();
        baseResult.setData(accountFlowMng.getPage(province, schoolId, classId,type, startDate, endDate, pageNo, pageSize));
        return baseResult;
    }

    @ApiOperation(value = "获取流水账户统计信息", notes = "获取流水账户统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "省", name = "province", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "开始日期（yyyy-MM-dd HH:ss:mm）", name = "startDate", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "结束日期（yyyy-MM-dd HH:ss:mm）", name = "endDate", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getStatistics", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getStatistics(String province, Long schoolId, Long classId, Date startDate, Date endDate) {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();
        if (startDate != null && endDate == null)
            endDate = new Date();
        Map<String, Object> resultMap = new HashMap<>();
        BigDecimal in = accountFlowMng.getStatistics(province, schoolId, classId, AccountFlow.TYPE_IN, startDate, endDate);
        resultMap.put("inAmount", in == null ? 0 : in);
        BigDecimal out = accountFlowMng.getStatistics(province, schoolId, classId, AccountFlow.TYPE_OUT, startDate, endDate);
        resultMap.put("outAmount", out == null ? 0 : out);
        baseResult.setData(resultMap);
        return baseResult;
    }

    @ApiOperation(value = "获取总账余额信息", notes = "获取总账余额信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/getGeneralAmount", method = RequestMethod.GET)
    public BaseResult<BigDecimal> getGeneralAmount() {
        BaseResult<BigDecimal> baseResult = new BaseResult<>();
        GeneralAccount generalAccount = accountFlowMng.getGeneralAccount();
        if (generalAccount == null) {
            baseResult.setData(new BigDecimal(0));
            return baseResult;
        }
        baseResult.setData(generalAccount.getAmount());
        return baseResult;
    }

    @ApiOperation(value = "获取相应余额信息", notes = "获取相应余额信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "省", name = "province", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getAmount", method = RequestMethod.GET)
    public BaseResult<BigDecimal> getAmount(String province, Long schoolId, Long classId) {
        BaseResult<BigDecimal> baseResult = new BaseResult<>();
        if (StringUtils.isBlank(province) && schoolId == null && classId == null) {
            return getGeneralAmount();
        }
        if (classId != null) {
            ClassAccount classAccount = classAccountMng.findByClassId(classId);
            if (classAccount == null) {
                baseResult.setData(new BigDecimal(0));
                return baseResult;
            }
            baseResult.setData(classAccount.getAmount());
            return baseResult;
        }
        if (schoolId != null) {
            SchoolFlowAccount schoolFlowAccount = accountFlowMng.getSchoolFlowAccount(schoolId);
            if (schoolFlowAccount == null) {
                baseResult.setData(new BigDecimal(0));
                return baseResult;
            }
            baseResult.setData(schoolFlowAccount.getAmount());
            return baseResult;
        }
        if (StringUtils.isNotBlank(province)) {

            ProvinceFlowAccount provinceFlowAccount = accountFlowMng.getProvinceFlowAccount(province);
            if (provinceFlowAccount == null) {
                baseResult.setData(new BigDecimal(0));
                return baseResult;
            }
            baseResult.setData(provinceFlowAccount.getAmount());
            return baseResult;
        }
        baseResult.setData(new BigDecimal(0));
        return baseResult;
    }

    @ApiOperation(value = "获取省流水账户列表", notes = "获取省流水账户列表")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/getProvinceFlowAccountList", method = RequestMethod.GET)
    public BaseResult<List<ProvinceFlowAccount>> getProvinceFlowAccountList() {
        BaseResult<List<ProvinceFlowAccount>> baseResult = new BaseResult<>();
        baseResult.setData(accountFlowMng.getProvinceFlowAccountList());
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
    public BaseResult<Page<SchoolFlowAccount>> getSchoolFlowAccountPage(String province, String schoolName,
                                                                        @RequestParam(defaultValue = "0") Integer pageNo,
                                                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        BaseResult<Page<SchoolFlowAccount>> baseResult = new BaseResult<>();
        baseResult.setData(accountFlowMng.getSchoolFlowAccountPage(province, schoolName, pageNo, pageSize));
        return baseResult;
    }
}
