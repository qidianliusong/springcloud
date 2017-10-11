package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolDrawRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolDrawRecordRepository extends CrudRepository<SchoolDrawRecord, Long> {

}
