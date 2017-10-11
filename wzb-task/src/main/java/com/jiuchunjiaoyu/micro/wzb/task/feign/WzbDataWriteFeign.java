package com.jiuchunjiaoyu.micro.wzb.task.feign;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.dto.*;
import com.jiuchunjiaoyu.micro.wzb.task.feign.config.AuthFeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 调用微智宝写入服务的feign接口
 */
@FeignClient(value = "WZB-DATA-WRITE-SERVER", configuration = AuthFeignConfiguration.class)
public interface WzbDataWriteFeign {

    @RequestMapping(value = "/operate/feePay/initFeeAgent", method = RequestMethod.POST)
    public BaseResult<FeeAgentDTO> initFeeAgent(FeeAgentDTO feeAgentDTO);

    @RequestMapping(value = "/operate/feePay/deleteFeeAgent", method = RequestMethod.DELETE)
    public BaseResult<Void> deleteFeeAgent(@RequestParam(required = true, value = "feeAgentId") Long feeAgentId);

    @RequestMapping(value = "/operate/feePay/updateFeeAgent", method = RequestMethod.POST)
    public BaseResult<Void> updateFeeAgent(FeeAgentDTO feeAgent);

    @RequestMapping(value = "/operate/feePay/afterFeeAgent", method = RequestMethod.POST)
    public BaseResult<Void> afterFeeAgent(@RequestParam(required = true, value = "userId") Long userId,
                                          @RequestParam(required = true, value = "feeAgentId") Long feeAgentId);

    @RequestMapping(value = "/operate/feePay/updateFeePay", method = RequestMethod.POST)
    public BaseResult<FeePayDTO> updateFeePay(FeePayDTO feePayDTO);

    @RequestMapping(value = "/operate/feePay/afterPay", method = RequestMethod.POST)
    public BaseResult<Void> afterFeePay(@RequestParam(required = true, value = "userId") Long userId,
                                        @RequestParam(required = true, value = "childId") Long childId,
                                        @RequestParam(required = true, value = "feePayId") Long feePayId);

    @RequestMapping(value = "/operate/feeDrawRecord/save", method = RequestMethod.POST)
    BaseResult<FeeDrawRecordDTO> saveOrUpdateFeeDrawRecord(FeeDrawRecordDTO feeDrawRecord);

    @RequestMapping(value = "/operate/feeDrawRecord/afterPay", method = RequestMethod.POST)
    BaseResult<Void> afterCompanyPay(Long feeDrawRecordId);

    @RequestMapping(value = "/operate/accountFlow/save", method = RequestMethod.POST)
    BaseResult<AccountFlowDTO> saveAccountFlow(AccountFlowDTO accountFlow);

    @RequestMapping(value = "/operate/generalAccount/addSchoolAmount", method = RequestMethod.POST)
    BaseResult<GeneralAccountDTO> addSchoolAmount(@RequestParam(required = true, value = "amount") BigDecimal amount);

}
