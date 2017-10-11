package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.SchoolDrawRecordMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.SchoolAccountRepository;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.SchoolDrawRecordRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Transactional
@Service
public class SchoolDrawRecordMngImpl extends BaseMngImpl<SchoolDrawRecord> implements SchoolDrawRecordMng {

    @Resource
    private SchoolDrawRecordRepository schoolDrawRecordRepository;

    @Resource
    private SchoolAccountRepository schoolAccountRepository;

    @Override
    protected CrudRepository<SchoolDrawRecord, Long> getRepository() {
        return schoolDrawRecordRepository;
    }

    @Override
    public SchoolDrawRecord passSchoolDrawRecord(SchoolDrawRecord schoolDrawRecord) throws WzbDataException {

        SchoolAccount schoolAccount = schoolAccountRepository.findBySchoolId(schoolDrawRecord.getSchoolId());

        if (schoolAccount == null)
            throw new WzbDataException(WzbDataWriteErr.entity_not_exist.getCode(), "不存在的学校账户,id=" + schoolDrawRecord.getSchoolId());

        BigDecimal subAmount = schoolDrawRecord.getAmount();

        if (schoolDrawRecord.getSchoolDrawRecordId() != null) {
            SchoolDrawRecord schoolDrawRecordFromDB = schoolDrawRecordRepository.findOne(schoolDrawRecord.getSchoolDrawRecordId());
            subAmount = subAmount.subtract(schoolDrawRecordFromDB.getAmount());
        }

        if (schoolAccount.getSchoolAmount().compareTo(subAmount) < 0) {
            //余额不足
            throw new WzbDataException(WzbDataWriteErr.amount_not_enough.getCode(), "学校账户余额不足");
        }

        schoolAccount.setSchoolAmount(schoolAccount.getSchoolAmount().subtract(subAmount));

        schoolAccountRepository.save(schoolAccount);
        schoolDrawRecord.setLeftAmount(schoolAccount.getSchoolAmount());
        return schoolDrawRecordRepository.save(schoolDrawRecord);
    }

    @Override
    public void deleteSchoolDrawRecord(Long schoolDrawRecordId) throws WzbDataException {
        SchoolDrawRecord schoolDrawRecord = schoolDrawRecordRepository.findOne(schoolDrawRecordId);
        if (schoolDrawRecord == null)
            throw new WzbDataException(WzbDataWriteErr.entity_not_exist.getCode(), "不存在的学校取款记录,id=" + schoolDrawRecordId);

        SchoolAccount schoolAccount = schoolAccountRepository.findBySchoolId(schoolDrawRecord.getSchoolId());
        if (schoolAccount == null)
            throw new WzbDataException(WzbDataWriteErr.entity_not_exist.getCode(), "不存在的学校账户,id=" + schoolDrawRecord.getSchoolId());

        BigDecimal amount = schoolDrawRecord.getAmount();
        schoolAccount.setSchoolAmount(schoolAccount.getSchoolAmount().add(amount));
        schoolDrawRecordRepository.delete(schoolDrawRecordId);
        schoolAccountRepository.save(schoolAccount);
    }
}
