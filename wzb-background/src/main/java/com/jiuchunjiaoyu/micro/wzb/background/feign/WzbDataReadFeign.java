package com.jiuchunjiaoyu.micro.wzb.background.feign;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.background.dto.*;
import com.jiuchunjiaoyu.micro.wzb.background.dto.page.PageDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 调用微智宝读服务的feign接口
 */
@FeignClient("WZB-DATA-READ-SERVER")
public interface WzbDataReadFeign {

    @RequestMapping(value = "/query/feeCategory/listForBack", method = RequestMethod.GET)
    BaseResult<List<FeeCategoryDTO>> getFeeCategoryList();

    @RequestMapping(value = "/query/feeCategory/findById", method = RequestMethod.GET)
    BaseResult<FeeCategoryDTO> findFeeCategoryById(@RequestParam(value = "feeCategoryId", required = true) Long feeCategoryId);

    @RequestMapping(value = "/query/accountFlow/getStatistics", method = RequestMethod.GET)
    BaseResult<Map<String, BigDecimal>> getAccountFlowStatistics(@RequestParam(value = "province", required = true) String province,
                                                                 @RequestParam(value = "schoolId", required = true) Long schoolId,
                                                                 @RequestParam(value = "classId", required = true) Long classId,
                                                                 @RequestParam(value = "startDate", required = true) String startDate,
                                                                 @RequestParam(value = "endDate", required = true) String endDate);

    @RequestMapping(value = "/query/accountFlow/getAmount", method = RequestMethod.GET)
    BaseResult<BigDecimal> getAmount(@RequestParam(value = "province", required = true) String province,
                                     @RequestParam(value = "schoolId", required = true) Long schoolId,
                                     @RequestParam(value = "classId", required = true) Long classId);

    @RequestMapping(value = "/query/accountFlow/getProvinceFlowAccountList", method = RequestMethod.GET)
    BaseResult<List<ProvinceFlowAccountDTO>> getProvinceFlowAccountList();

    @RequestMapping(value = "/query/accountFlow/getSchoolFlowAccountPage", method = RequestMethod.GET)
    BaseResult<PageDTO<SchoolFlowAccountDTO>> getSchoolFlowAccountPage(@RequestParam(value = "province", required = true) String province,
                                                                       @RequestParam(value = "schoolName", required = true) String schoolName,
                                                                       @RequestParam(value = "pageNo", required = true) Integer pageNo,
                                                                       @RequestParam(value = "pageSize", required = true) Integer pageSize);

    @RequestMapping(value = "/query/accountFlow/getPage", method = RequestMethod.GET)
    BaseResult<PageDTO<AccountFlowDTO>> getAccountFlowPage(@RequestParam(value = "province", required = true) String province,
                                                           @RequestParam(value = "schoolId", required = true) Long schoolId,
                                                           @RequestParam(value = "classId", required = true) Long classId,
                                                           @RequestParam(value = "type", required = true) Integer type,
                                                           @RequestParam(value = "startDate", required = true) String startDate,
                                                           @RequestParam(value = "endDate", required = true) String endDate,
                                                           @RequestParam(value = "pageNo", required = true) Integer pageNo,
                                                           @RequestParam(value = "pageSize", required = true) Integer pageSize);

    @RequestMapping(value = "/query/classAccount/getClassAccountPage", method = RequestMethod.GET)
    BaseResult<PageDTO<ClassAccountDTO>> getClassAccountPage(@RequestParam(value = "schoolId", required = true) Long schoolId,
                                                             @RequestParam(value = "className", required = true) String className,
                                                             @RequestParam(value = "pageNo", required = true) Integer pageNo,
                                                             @RequestParam(value = "pageSize", required = true) Integer pageSize);

    @RequestMapping(value = "/query/schoolDrawRecord/getPage", method = RequestMethod.GET)
    BaseResult<PageDTO<SchoolDrawRecordDTO>> getSchoolDrawRecordPage(@RequestParam(value = "schoolName", required = true) String schoolName,
                                                                     @RequestParam(value = "startDate", required = true) String startDate,
                                                                     @RequestParam(value = "endDate", required = true) String endDate,
                                                                     @RequestParam(value = "status", required = true) Integer status,
                                                                     @RequestParam(value = "pageNo", required = true) Integer pageNo,
                                                                     @RequestParam(value = "pageSize", required = true) Integer pageSize);

    @RequestMapping(value = "/query/generalAccount/getGeneralAccount", method = RequestMethod.GET)
    BaseResult<GeneralAccountDTO> getGeneralAccount();

    @RequestMapping(value = "/query/schoolDrawRecord/getStatistics", method = RequestMethod.GET)
    BaseResult<Map<String, Object>> getSchoolDrawRecordStatistics(@RequestParam(value = "startDate", required = true) String startDate,
                                                                  @RequestParam(value = "endDate", required = true) String endDate);

    @RequestMapping(value = "/query/schoolDrawRecord/findById", method = RequestMethod.GET)
    BaseResult<SchoolDrawRecordDTO> findSchoolDrawRecordById(@RequestParam(value = "schoolDrawRecordId", required = true) long schoolDrawRecordId);

}
