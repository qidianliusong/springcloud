package com.jiuchunjiaoyu.micro.wzb.task.task;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.dto.*;
import com.jiuchunjiaoyu.micro.wzb.task.exception.TaskException;
import com.jiuchunjiaoyu.micro.wzb.task.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.wzb.task.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.wzb.task.util.FeignUtil;
import com.jiuchunjiaoyu.micro.wzb.task.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 支付消息处理
 */
@Component
@RabbitListener(queues = "wzb.pay.message", containerFactory = "payMessageTaskContainerFactory")
public class PayMessageHandler {

    private static Logger logger = LoggerFactory.getLogger(PayMessageHandler.class);

    @Resource
    private WzbDataWriteFeign writeFeign;

    @Resource
    private WzbDataReadFeign readFeign;

    @RabbitHandler
    public void process(String msg) throws TaskException {
        PayMessageDTO payMessageDTO = GsonUtil.GSON.fromJson(msg, PayMessageDTO.class);
        if (payMessageDTO == null) {
            return;
        }
        switch (payMessageDTO.getType()) {
            case PayMessageDTO.TYPE_FEE_PAY:
                handleFeePay(payMessageDTO);
                break;
            case PayMessageDTO.TYPE_FEE_AGENT:
                handleFeeAgent(payMessageDTO);
                break;
            case PayMessageDTO.TYPE_DRAW:
                handleDraw(payMessageDTO);
                break;
            case PayMessageDTO.TYPE_SCHOOL_DRAW:
                handleSchoolDraw(payMessageDTO);
                break;
            default:
                break;
        }
    }

    /**
     * 学校取款处理
     *
     * @param payMessageDTO
     */
    private void handleSchoolDraw(PayMessageDTO payMessageDTO) throws TaskException {
        BaseResult<GeneralAccountDTO> generalAccountDTOBaseResult = writeFeign.addSchoolAmount(payMessageDTO.getAmount());
        FeignUtil.validateFeignResult(generalAccountDTOBaseResult, "writeFeign.addSchoolAmount", logger);
    }

    /**
     * 取款处理
     *
     * @param payMessageDTO
     */
    private void handleDraw(PayMessageDTO payMessageDTO) throws TaskException {
        if (payMessageDTO == null || payMessageDTO.getId() == null)
            return;

        AccountFlowDTO accountFlowDTO = new AccountFlowDTO();

        BaseResult<FeeDrawRecordDTO> feeDrawRecordById = readFeign.findFeeDrawRecordById(payMessageDTO.getId());

        FeignUtil.validateFeignResult(feeDrawRecordById, "readFeign.findFeeDrawRecordById", logger);
        FeeDrawRecordDTO feeDrawRecordDTO = feeDrawRecordById.getData();

        BaseResult<ClassAccountDTO> classAccountDTOBaseResult = readFeign.queryClassAccountByClassId(feeDrawRecordDTO.getClassId());
        FeignUtil.validateFeignResult(classAccountDTOBaseResult, "readFeign.queryClassAccountByClassId", logger);
        ClassAccountDTO classAccountDTO = classAccountDTOBaseResult.getData();
        BaseResult<SchoolAccountDTO> schoolAccountBySchoolId = readFeign.findSchoolAccountBySchoolId(classAccountDTO.getSchoolId());
        SchoolAccountDTO schoolAccountDTO = schoolAccountBySchoolId.getData();

        accountFlowDTO.setAmount(feeDrawRecordDTO.getAmount());
        accountFlowDTO.setClassId(feeDrawRecordDTO.getClassId());
        accountFlowDTO.setCity(schoolAccountDTO.getCity());
        accountFlowDTO.setClassName(classAccountDTO.getClassName());
        accountFlowDTO.setCounty(schoolAccountDTO.getCounty());
        accountFlowDTO.setFlowTime(feeDrawRecordDTO.getDrawTime() == null ? new Date() : feeDrawRecordDTO.getDrawTime());
        accountFlowDTO.setProvince(schoolAccountDTO.getProvince());
        accountFlowDTO.setSchoolId(schoolAccountDTO.getSchoolId());
        accountFlowDTO.setSchoolName(schoolAccountDTO.getSchoolName());
        accountFlowDTO.setTown(schoolAccountDTO.getTown());
        accountFlowDTO.setUserName(feeDrawRecordDTO.getUserName());
        accountFlowDTO.setUserId(feeDrawRecordDTO.getUserId());
        accountFlowDTO.setType(AccountFlowDTO.TYPE_OUT);
        accountFlowDTO.setTradeNo(feeDrawRecordDTO.getPartnerTradeNo());
        accountFlowDTO.setMessage(feeDrawRecordDTO.getReason());

        BaseResult<AccountFlowDTO> accountFlowDTOBaseResult = writeFeign.saveAccountFlow(accountFlowDTO);

        FeignUtil.validateFeignResult(accountFlowDTOBaseResult, "writeFeign.saveAccountFlow", logger);
    }

    /**
     * 代缴处理
     *
     * @param payMessageDTO
     */
    private void handleFeeAgent(PayMessageDTO payMessageDTO) throws TaskException {
        if (payMessageDTO == null || payMessageDTO.getId() == null)
            return;

        AccountFlowDTO accountFlowDTO = null;
        boolean isSchoolBack = false;
        BaseResult<FeeAgentDTO> feeAgentById = readFeign.findFeeAgentById(payMessageDTO.getId());
        FeignUtil.validateFeignResult(feeAgentById, "readFeign.findFeeAgentById", logger);
        FeeAgentDTO feeAgentDTO = feeAgentById.getData();

        BaseResult<FeeDetailDTO> feeDetailById = readFeign.findFeeDetailById(feeAgentDTO.getFeeDetailId());
        FeignUtil.validateFeignResult(feeDetailById, "readFeign.findFeeDetailById", logger);
        FeeDetailDTO feeDetailDTO = feeDetailById.getData();
        BaseResult<ClassAccountDTO> classAccountDTOBaseResult = readFeign.queryClassAccountByClassId(feeDetailDTO.getClassId());
        FeignUtil.validateFeignResult(classAccountDTOBaseResult, "readFeign.queryClassAccountByClassId", logger);
        ClassAccountDTO classAccountDTO = classAccountDTOBaseResult.getData();
        BaseResult<SchoolAccountDTO> schoolAccountBySchoolId = readFeign.findSchoolAccountBySchoolId(classAccountDTO.getSchoolId());
        SchoolAccountDTO schoolAccountDTO = schoolAccountBySchoolId.getData();

        BaseResult<FeeCategoryDTO> feeCategoryById = readFeign.findFeeCategoryById(feeDetailDTO.getFeeCategoryId());
        FeeCategoryDTO feeCategoryDTO = feeCategoryById.getData();

        if (feeCategoryDTO.getType() == FeeCategoryDTO.TYPE_COMMON) {
            accountFlowDTO = new AccountFlowDTO();
            accountFlowDTO.setAmount(feeAgentDTO.getTotalAmount());
            accountFlowDTO.setClassId(feeDetailDTO.getClassId());
            accountFlowDTO.setCity(schoolAccountDTO.getCity());
            accountFlowDTO.setClassName(classAccountDTO.getClassName());
            accountFlowDTO.setCounty(schoolAccountDTO.getCounty());
            accountFlowDTO.setFeeCategoryName(feeDetailDTO.getFeeCategoryName());
            accountFlowDTO.setFeeCategoryId(feeDetailDTO.getFeeCategoryId());
            accountFlowDTO.setFlowTime(feeAgentDTO.getPayTime() == null ? new Date() : feeAgentDTO.getPayTime());
            accountFlowDTO.setProvince(schoolAccountDTO.getProvince());
            accountFlowDTO.setSchoolId(schoolAccountDTO.getSchoolId());
            accountFlowDTO.setSchoolName(schoolAccountDTO.getSchoolName());
            accountFlowDTO.setTown(schoolAccountDTO.getTown());
            accountFlowDTO.setUserName(feeAgentDTO.getUserId() + "");
            accountFlowDTO.setUserId(feeAgentDTO.getUserId());
            accountFlowDTO.setType(AccountFlowDTO.TYPE_IN);
            accountFlowDTO.setTradeNo(feeAgentDTO.getOutTradeNo());
        } else if (feeCategoryDTO.getType() == FeeCategoryDTO.TYPE_BACKGROUND) {
            isSchoolBack = true;
        }


        if (accountFlowDTO != null) {
            BaseResult<AccountFlowDTO> accountFlowDTOBaseResult = writeFeign.saveAccountFlow(accountFlowDTO);
            FeignUtil.validateFeignResult(accountFlowDTOBaseResult, "writeFeign.saveAccountFlow", logger);
        }

        if (isSchoolBack) {
            BaseResult<GeneralAccountDTO> generalAccountDTOBaseResult = writeFeign.addSchoolAmount(payMessageDTO.getAmount());
            FeignUtil.validateFeignResult(generalAccountDTOBaseResult, "writeFeign.addSchoolAmount", logger);
        }

    }

    /**
     * 缴费处理
     *
     * @param payMessageDTO
     */
    private void handleFeePay(PayMessageDTO payMessageDTO) throws TaskException {
        if (payMessageDTO == null || payMessageDTO.getId() == null)
            return;

        AccountFlowDTO accountFlowDTO = null;
        boolean isSchoolBack = false;

        BaseResult<FeePayDTO> feePay = readFeign.getFeePay(payMessageDTO.getId());
        FeignUtil.validateFeignResult(feePay, "readFeign.getFeePay", logger);
        FeePayDTO feePayDTO = feePay.getData();

        BaseResult<FeeDetailDTO> feeDetailById = readFeign.findFeeDetailById(feePayDTO.getFeeDetailId());
        FeignUtil.validateFeignResult(feeDetailById, "readFeign.findFeeDetailById", logger);
        FeeDetailDTO feeDetailDTO = feeDetailById.getData();
        BaseResult<ClassAccountDTO> classAccountDTOBaseResult = readFeign.queryClassAccountByClassId(feeDetailDTO.getClassId());
        FeignUtil.validateFeignResult(classAccountDTOBaseResult, "readFeign.queryClassAccountByClassId", logger);
        ClassAccountDTO classAccountDTO = classAccountDTOBaseResult.getData();
        BaseResult<SchoolAccountDTO> schoolAccountBySchoolId = readFeign.findSchoolAccountBySchoolId(classAccountDTO.getSchoolId());
        SchoolAccountDTO schoolAccountDTO = schoolAccountBySchoolId.getData();

        BaseResult<FeeCategoryDTO> feeCategoryById = readFeign.findFeeCategoryById(feeDetailDTO.getFeeCategoryId());
        FeeCategoryDTO feeCategoryDTO = feeCategoryById.getData();

        if (feeCategoryDTO.getType() == FeeCategoryDTO.TYPE_COMMON) {
            accountFlowDTO = new AccountFlowDTO();
            accountFlowDTO.setAmount(feePayDTO.getAmount());
            accountFlowDTO.setClassId(feeDetailDTO.getClassId());
            accountFlowDTO.setCity(schoolAccountDTO.getCity());
            accountFlowDTO.setClassName(classAccountDTO.getClassName());
            accountFlowDTO.setCounty(schoolAccountDTO.getCounty());
            accountFlowDTO.setFeeCategoryName(feeDetailDTO.getFeeCategoryName());
            accountFlowDTO.setFeeCategoryId(feeDetailDTO.getFeeCategoryId());
            accountFlowDTO.setFlowTime(feePayDTO.getPayTime() == null ? new Date() : feePayDTO.getPayTime());
            accountFlowDTO.setProvince(schoolAccountDTO.getProvince());
            accountFlowDTO.setSchoolId(schoolAccountDTO.getSchoolId());
            accountFlowDTO.setSchoolName(schoolAccountDTO.getSchoolName());
            accountFlowDTO.setTown(schoolAccountDTO.getTown());
            accountFlowDTO.setUserName(feePayDTO.getUserName());
            accountFlowDTO.setUserId(feePayDTO.getUserId());
            accountFlowDTO.setType(AccountFlowDTO.TYPE_IN);
            accountFlowDTO.setTradeNo(feePayDTO.getOutTradeNo());
        } else if (feeCategoryDTO.getType() == FeeCategoryDTO.TYPE_BACKGROUND) {
            isSchoolBack = true;
        }

        if (accountFlowDTO != null) {
            BaseResult<AccountFlowDTO> accountFlowDTOBaseResult = writeFeign.saveAccountFlow(accountFlowDTO);
            FeignUtil.validateFeignResult(accountFlowDTOBaseResult, "writeFeign.saveAccountFlow", logger);
        }
        if (isSchoolBack) {
            BaseResult<GeneralAccountDTO> generalAccountDTOBaseResult = writeFeign.addSchoolAmount(payMessageDTO.getAmount());
            FeignUtil.validateFeignResult(generalAccountDTOBaseResult, "writeFeign.addSchoolAmount", logger);
        }

    }

}
