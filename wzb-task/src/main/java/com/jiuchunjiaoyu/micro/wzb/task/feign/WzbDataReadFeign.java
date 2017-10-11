package com.jiuchunjiaoyu.micro.wzb.task.feign;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.dto.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 调用微智宝读服务的feign接口
 */
@FeignClient("WZB-DATA-READ-SERVER")
public interface WzbDataReadFeign {
    @RequestMapping(value = "/query/feeAgent/getPreFeeAgents", method = RequestMethod.GET)
    BaseResult<List<FeeAgentDTO>> getPreFeeAgents(@RequestParam(value = "startTime", required = true) String startTime);

    @RequestMapping(value = "/query/feePay/getPreFeePays", method = RequestMethod.GET)
    BaseResult<List<FeePayDTO>> getPreFeePays(@RequestParam(value = "startTime", required = true) String startTime);

    @RequestMapping(value = "/query/feeDrawRecord/getPrePayRecords", method = RequestMethod.GET)
    BaseResult<List<FeeDrawRecordDTO>> getPrePayRecords(@RequestParam(value = "startTime", required = true) String startTime);

    @RequestMapping(value = "/query/feePay/getById", method = RequestMethod.GET)
    BaseResult<FeePayDTO> getFeePay(@RequestParam(required = true, value = "feePayId") Long feePayId);

    @RequestMapping(value = "/query/feeAgent/findById", method = RequestMethod.GET)
    BaseResult<FeeAgentDTO> findFeeAgentById(@RequestParam(required = true, value = "feeAgentId") Long feeAgentId);

    @RequestMapping(value = "/query/classAccount/getByClassId", method = RequestMethod.GET)
    BaseResult<ClassAccountDTO> queryClassAccountByClassId(@RequestParam(required = true, value = "classId") Long classId);

    @RequestMapping(value = "/query/schoolAccount/findBySchoolId", method = RequestMethod.GET)
    BaseResult<SchoolAccountDTO> findSchoolAccountBySchoolId(@RequestParam(required = true, value = "schoolId") Long schoolId);

    @RequestMapping(value = "/query/feeDetail/findById", method = RequestMethod.GET)
    BaseResult<FeeDetailDTO> findFeeDetailById(@RequestParam(required = true, value = "feeDetailId") Long feeDetailId);

    @RequestMapping(value = "/query/feeDrawRecord/findByID", method = RequestMethod.GET)
    BaseResult<FeeDrawRecordDTO> findFeeDrawRecordById(@RequestParam(required = true,value = "feeDrawRecordId") Long feeDrawRecordId);

    @RequestMapping(value = "/query/feeCategory/findById", method = RequestMethod.GET)
    BaseResult<FeeCategoryDTO> findFeeCategoryById(@RequestParam(value = "feeCategoryId", required = true) Long feeCategoryId);
}
