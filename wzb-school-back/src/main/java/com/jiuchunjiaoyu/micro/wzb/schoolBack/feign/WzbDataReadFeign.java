package com.jiuchunjiaoyu.micro.wzb.schoolBack.feign;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.*;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.dto.page.PageDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 调用微智宝读服务的feign接口
 */
@FeignClient("WZB-DATA-READ-SERVER")
public interface WzbDataReadFeign {

    @RequestMapping(value = "/query/feeCategory/getPageBySchoolId", method = RequestMethod.GET)
    BaseResult<PageDTO<FeeCategoryDTO>> getFeeCategoryPageBySchoolId(@RequestParam(value = "schoolId", required = true) Long schoolId,
                                                                     @RequestParam(value = "pageNo", required = true) Integer pageNo,
                                                                     @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                                                     @RequestParam(value = "startDate", required = true) String startDate,
                                                                     @RequestParam(value = "endDate", required = true) String endDate);

    @RequestMapping(value = "/query/feeCategory/findById", method = RequestMethod.GET)
    BaseResult<FeeCategoryDTO> findFeeCategoryById(@RequestParam(value = "feeCategoryId", required = true) Long feeCategoryId);


    @RequestMapping(value = "/query/feeDetail/getPageByCategoryId", method = RequestMethod.POST)
    BaseResult<PageDTO<FeeDetailDTO>> getFeeDetailPageByCategoryId(@RequestParam(required = true, value = "categoryId") Long categoryId,
                                                                   @RequestParam(required = true, value = "pageNo") Integer pageNo,
                                                                   @RequestParam(required = true, value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/query/feeDetail/getSumByCategoryId", method = RequestMethod.GET)
    BaseResult<BigDecimal> getFeeDetailSumByCategoryId(@RequestParam(required = true, value = "categoryId") Long categoryId);

    @RequestMapping(value = "/query/feePay/list", method = RequestMethod.GET)
    BaseResult<List<FeePayDTO>> getFeePayList(
            @RequestParam(required = true, value = "feeDetailId") Long feeDetailId,
            @RequestParam(required = true, value = "payStatus") Integer payStatus);

    @RequestMapping(value = "/query/feeDetail/findById", method = RequestMethod.GET)
    BaseResult<FeeDetailDTO> getFeeDetailById(
            @RequestParam(required = true, value = "feeDetailId") Long feeDetailId);

    @RequestMapping(value = "/query/schoolDrawRecord/getPageBySchoolId", method = RequestMethod.GET)
    BaseResult<PageDTO<SchoolDrawRecordDTO>> getSchoolDrawRecordPageBySchoolId(@RequestParam(value = "schoolId", required = true) long schoolId,
                                                                               @RequestParam(value = "startDate", required = true) String startDate,
                                                                               @RequestParam(value = "endDate", required = true) String endDate,
                                                                               @RequestParam(value = "status", required = true) Integer status,
                                                                               @RequestParam(value = "pageNo", required = true) Integer pageNo,
                                                                               @RequestParam(value = "pageSize", required = true) Integer pageSize);

    @RequestMapping(value = "/query/schoolAccount/findBySchoolId", method = RequestMethod.GET)
    BaseResult<SchoolAccountDTO> findSchoolAccountBySchoolId(@RequestParam(required = true, value = "schoolId") Long schoolId);
}
