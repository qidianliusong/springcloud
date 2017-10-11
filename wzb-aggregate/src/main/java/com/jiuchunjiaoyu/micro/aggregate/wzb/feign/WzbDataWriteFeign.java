package com.jiuchunjiaoyu.micro.aggregate.wzb.feign;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.*;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.config.AuthFeignConfiguration;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用微智宝写入服务的feign接口
 */
@FeignClient(value = "WZB-DATA-WRITE-SERVER", configuration = AuthFeignConfiguration.class)
public interface WzbDataWriteFeign {

    @RequestMapping(value = "/test/11", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public BaseResult<String> test1();

    @RequestMapping(value = "/operate/feePay/initFeeAgent", method = RequestMethod.POST)
    public BaseResult<FeeAgentDTO> initFeeAgent(FeeAgentDTO feeAgentDTO);

    @RequestMapping(value = "/operate/feePay/deleteFeeAgent", method = RequestMethod.DELETE)
    public BaseResult<Void> deleteFeeAgent(@RequestParam(required = true, value = "feeAgentId") Long feeAgentId);

    @RequestMapping(value = "/operate/feePay/updateFeeAgent", method = RequestMethod.POST)
    public BaseResult<Void> updateFeeAgent(FeeAgentDTO feeAgent);

    @RequestMapping(value = "/operate/feePay/afterFeeAgent", method = RequestMethod.POST)
    public BaseResult<Void> afterFeeAgent(@RequestParam(required = true, value = "userId") Long userId,
                                          @RequestParam(required = true, value = "feeAgentId") Long feeAgentId);

    @RequestMapping(value = "/operate/classAccount/save", method = RequestMethod.POST)
    public BaseResult<Void> saveClassAccount(ClassAccountDTO classAccountDTO);

    @RequestMapping(value = "/operate/feeTake/saveOrUpdate", method = RequestMethod.POST)
    public BaseResult<FeeTakeDTO> saveOrUpdateFeeTake(FeeTakeDTO feeTake);

    @RequestMapping(value = "/operate/feeDetail/save", method = RequestMethod.POST)
    public BaseResult<FeeDetailDTO> saveFeeDetail(FeeDetailDTO feeDetailDTO);

    @RequestMapping(value = "/operate/feeDetail/update", method = RequestMethod.POST)
    public BaseResult<FeeDetailDTO> updateFeeDetail(FeeDetailDTO feeDetailDTO);

    @RequestMapping(value = "/operate/feePay/updateFeePay", method = RequestMethod.POST)
    public BaseResult<FeePayDTO> updateFeePay(FeePayDTO feePayDTO);

    @RequestMapping(value = "/operate/feePay/afterPay", method = RequestMethod.POST)
    public BaseResult<Void> afterFeePay(@RequestParam(required = true, value = "userId") Long userId,
                                        @RequestParam(required = true, value = "childId") Long childId,
                                        @RequestParam(required = true, value = "feePayId") Long feePayId);

    @RequestMapping(value = "/operate/feeDrawRecord/save", method = RequestMethod.POST)
    public BaseResult<FeeDrawRecordDTO> saveOrUpdateFeeDrawRecord(FeeDrawRecordDTO feeDrawRecord);

    @RequestMapping(value = "/operate/feeDrawRecord/afterPay", method = RequestMethod.POST)
    BaseResult<Void> afterDraw(@RequestParam(value = "feeDrawRecordId", required = true) Long feeDrawRecordId);

    @RequestMapping(value = "/operate/feeDetail/changeStatus", method = RequestMethod.POST)
    BaseResult<Void> changeFeeDetailStatus(@RequestParam(value = "feeDetailId", required = true) Long feeDetailId);

    @RequestMapping(value = "/operate/feeTake/delete", method = RequestMethod.DELETE)
    BaseResult<Void> deleteFeeTake(@RequestParam(value = "feeTakeId", required = true) Long feeTakeId);

    /**
     * 新增收费类目
     *
     * @param feeCategory
     * @return
     */
    @RequestMapping(value = "/operate/feeCategory/add", method = RequestMethod.POST)
    BaseResult<FeeCategoryDTO> saveFeeCategory(FeeCategoryDTO feeCategory);

    /**
     * 修改收费类目
     *
     * @param feeCategory
     * @return
     */
    @RequestMapping(value = "/operate/feeCategory/update", method = RequestMethod.POST)
    BaseResult<FeeCategoryDTO> updateFeeCategory(FeeCategoryDTO feeCategory);

    /**
     * 新建学校账户
     * @param schoolAccount
     * @return
     */
    @RequestMapping(value = "/operate/schoolAccount/save", method = RequestMethod.POST)
    BaseResult<SchoolAccountDTO> saveSchoolAccount(SchoolAccountDTO schoolAccount);

}
