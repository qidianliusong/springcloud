package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;

public interface FeeDrawRecordMng extends BaseMng<FeeDrawRecord>{

    FeeDrawRecord saveFeeDrawRecord(FeeDrawRecord feeDrawRecord) throws WzbDataException;

    void afterPay(Long feeDrawRecordId) throws WzbDataException;
}
