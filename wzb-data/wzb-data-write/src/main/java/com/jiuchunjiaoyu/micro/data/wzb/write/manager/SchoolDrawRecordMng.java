package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;

/**
 * 学校后台取款记录manager
 */
public interface SchoolDrawRecordMng extends BaseMng<SchoolDrawRecord> {

    /**
     * 审核通过学校取款请求
     * @param schoolDrawRecord
     * @return
     * @throws WzbDataException
     */
    SchoolDrawRecord passSchoolDrawRecord(SchoolDrawRecord schoolDrawRecord) throws WzbDataException;

    void deleteSchoolDrawRecord(Long schoolDrawRecordId) throws WzbDataException;
}
