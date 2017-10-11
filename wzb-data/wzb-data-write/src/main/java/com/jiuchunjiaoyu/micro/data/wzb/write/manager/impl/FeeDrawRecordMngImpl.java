package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.ClassAccountRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeDrawRecordMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.FeeDrawRecordRepository;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class FeeDrawRecordMngImpl extends BaseMngImpl<FeeDrawRecord> implements FeeDrawRecordMng {

    @Resource
    private FeeDrawRecordRepository feeDrawRecordRepository;

    @Resource
    private ClassAccountRepository classAccountRepository;

    @Override
    protected CrudRepository<FeeDrawRecord, Long> getRepository() {
        return feeDrawRecordRepository;
    }

    @Override
    public FeeDrawRecord saveFeeDrawRecord(FeeDrawRecord feeDrawRecord) throws WzbDataException {
        ClassAccount classAccount = classAccountRepository.findByClassId(feeDrawRecord.getClassId());
        if (classAccount.getAmount().compareTo(feeDrawRecord.getAmount()) < 0)
            throw new WzbDataException(WzbDataWriteErr.amount_not_enough.getCode(), WzbDataWriteErr.amount_not_enough.getMessage());
        FeeDrawRecord save = this.save(feeDrawRecord);
        return save;
    }

    @Override
    public void afterPay(Long feeDrawRecordId) throws WzbDataException {
        FeeDrawRecord feeDrawRecord = feeDrawRecordRepository.findOne(feeDrawRecordId);
        if (feeDrawRecord == null)
            throw new WzbDataException(WzbDataWriteErr.fee_draw_record_not_exist.getCode(), WzbDataWriteErr.fee_draw_record_not_exist.getMessage());
        feeDrawRecord.setStatus(FeeDrawRecord.STATUS_PAY);
        feeDrawRecord.setDrawTime(new Date());
        this.save(feeDrawRecord);
        ClassAccount classAccount = classAccountRepository.findByClassId(feeDrawRecord.getClassId());
        classAccount.setAmount(classAccount.getAmount().subtract(feeDrawRecord.getAmount()));
        if(classAccount.getUsedAmount()==null)
            classAccount.setUsedAmount(new BigDecimal(0));
        classAccount.setUsedAmount(classAccount.getUsedAmount().add(feeDrawRecord.getAmount()));
        classAccountRepository.save(classAccount);
    }
}
